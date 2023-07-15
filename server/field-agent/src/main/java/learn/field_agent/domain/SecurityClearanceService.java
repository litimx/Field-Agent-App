package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {

    //Fields
    private final SecurityClearanceRepository repository;

    //Constructor
    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    //Methods
    public List<SecurityClearance> findAll() {
        return repository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId) {
        return repository.findById(securityClearanceId);
    }
    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        //Run through validation
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        // Check if same name already exists
        SecurityClearance existing = repository.findByName(securityClearance.getName());
        if (existing != null) {
            result.addMessage("SecurityClearance with the same name already exists", ResultType.INVALID);
            return result;
        }

        //If passes validation, make sure it doesn't have an existing id
        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("SecurityClearanceId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        securityClearance = repository.add(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("SecurityClearanceId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        SecurityClearance existing = repository.findByName(securityClearance.getName());
        if (existing != null && existing.getSecurityClearanceId() != securityClearance.getSecurityClearanceId()) {
            result.addMessage("Security clearance name already exists", ResultType.INVALID);
            return result;
        }

        if (!repository.update(securityClearance)) {
            String msg = String.format("SecurityClearanceId: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public boolean isSecurityClearanceInUse(int securityClearanceId) {
        return repository.getUsageCount(securityClearanceId) > 0;
    }

    public boolean deleteById(int securityClearanceId) {
        if (!isSecurityClearanceInUse(securityClearanceId)) {
            return repository.deleteById(securityClearanceId);
        }
        return false;
    }

    //Helper methods
    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();
        if (securityClearance == null) {
            result.addMessage("SecurityClearance cannot be null", ResultType.INVALID);
            return result;
        }
        if (securityClearance.getName() == null || securityClearance.getName().isBlank()) {
            result.addMessage("Name is required", ResultType.INVALID);
        }
        return result;
    }
}