package learn.field_agent.controllers;

import learn.field_agent.domain.Result;
import learn.field_agent.domain.SecurityClearanceService;
import learn.field_agent.models.SecurityClearance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/security/clearance")
public class SecurityClearanceController {
    private final SecurityClearanceService service;

    public SecurityClearanceController(SecurityClearanceService service) {
        this.service = service;
    }

    @GetMapping
    public List<SecurityClearance> findAll() {
        return service.findAll();
    }

    @GetMapping("/{securityClearanceId}")
    public ResponseEntity<Object> findById(@PathVariable int securityClearanceId) {
        SecurityClearance sc = service.findById(securityClearanceId);
        if(sc == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sc);
    }
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SecurityClearance securityClearance){
        Result<SecurityClearance> result = service.add(securityClearance);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{securityClearanceId}")
    public ResponseEntity<Object> update(@PathVariable int securityClearanceId, @RequestBody SecurityClearance securityClearance){
        if(securityClearanceId != securityClearance.getSecurityClearanceId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<SecurityClearance> result = service.update(securityClearance);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{securityClearanceId}")
    public ResponseEntity<Object> deleteById(@PathVariable int securityClearanceId) {
        //check if in use
        if (service.isSecurityClearanceInUse(securityClearanceId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Security clearance is in use and cannot be deleted");
        }

        //it not in use, attempt to delete
        boolean isDeleted = service.deleteById(securityClearanceId);

        //if successful, return 204
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        //if failed, return 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Security clearance not found");
    }
}