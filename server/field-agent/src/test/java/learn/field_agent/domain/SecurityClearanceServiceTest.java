package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldNotAddWhenInvalid() {
        // Should not add `null`.
        Result<SecurityClearance> result = service.add(null);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not add if securityClearanceId is greater than 0
        SecurityClearance securityClearance = makeSecurityClearance();
        result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not add if name is null
        securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(0);
        securityClearance.setName(null);
        result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not add if name is empty
        securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(0);
        securityClearance.setName("");
        result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAddWhenValid() {
        SecurityClearance expected = makeSecurityClearance();
        SecurityClearance arg = makeSecurityClearance();
        arg.setSecurityClearanceId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<SecurityClearance> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        // Should not update `null`
        Result<SecurityClearance> result = service.update(null);
        assertEquals(ResultType.INVALID, result.getType());

        //securityClearanceId is not set.
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(0);
        result = service.update(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

        //name is null.
        securityClearance = makeSecurityClearance();
        securityClearance.setName(null);
        result = service.update(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

        //name is empty.
        securityClearance = makeSecurityClearance();
        securityClearance.setName("");
        result = service.update(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdateWhenValid() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setName("Top Secret");

        when(repository.update(securityClearance)).thenReturn(true);
        Result<SecurityClearance> result = service.update(securityClearance);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDeleteWhenNotFound() {
        when(repository.deleteById(4)).thenReturn(false);
        assertFalse(service.deleteById(4));
    }

    @Test
    void shouldDelete() {
        when(repository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
    }

    private SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName("Top Secret");
        return securityClearance;
    }
}