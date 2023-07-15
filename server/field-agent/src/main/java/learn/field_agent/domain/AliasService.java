package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AliasService {
    //fields
    private final AliasRepository repository;
    private final AgentRepository agentRepository;
    //constructor
    public AliasService(AliasRepository repository, AgentRepository agentRepository) {
        this.repository = repository;
        this.agentRepository = agentRepository;
    }
    //Methods
    public Result<Alias> add(Alias alias){
        // run it through our validation
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }
        // if it passes validation we will make sure it doesn't have an existing id
        if(alias.getAliasId() != 0){
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }
        // if we make it this far we will try to add
        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
      }


      public Result<Alias> update(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }

        if(alias.getAliasId() <= 0){
            result.addMessage("aliasId must be set for `update` operation", ResultType.INVALID);
            return result;
        }
        //checking to make sure it exists in our reposiotry
        if(!repository.update(alias)){
            String msg = String.format("aliasId: %s, does not exist", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
      }

      public boolean deleteById(int aliasId){
        return repository.deleteById(aliasId);
      }

      //helper methods
    private Result<Alias> validate(Alias alias){
        Result<Alias> result = new Result<>();
        if(alias == null){
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }
        if(alias.getName() == null || alias.getName().isBlank()){
            result.addMessage("name is required", ResultType.INVALID);
        }
        if(alias.getAgentId() == 0){
            result.addMessage("agentId is required", ResultType.INVALID);
        }else if(agentRepository.findById(alias.getAgentId()) == null){
            result.addMessage("agent must exist", ResultType.INVALID);
        }
        List<Alias> existingAliases = repository.findByName(alias.getName());

        // filter out the alias ID that we're validating
        existingAliases = existingAliases.stream()
                .filter(a -> a.getAliasId() != alias.getAliasId())
                .collect(Collectors.toList());
        if(existingAliases.size() > 0){
            if(alias.getPersona() == null || alias.getPersona().isBlank()){
                result.addMessage("persona is required if the alias name is already in use", ResultType.INVALID);
            }else if(existingAliases.stream().anyMatch(a -> a.getPersona() != null
            && a.getPersona().equalsIgnoreCase(alias.getPersona()))){
                result.addMessage("persona must be unique if alias name and person are already in use", ResultType.INVALID);
            }
        }
        return result;
    }
}
