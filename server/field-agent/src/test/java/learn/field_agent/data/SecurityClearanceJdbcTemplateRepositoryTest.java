package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(3);
        assertEquals(null, actual);
    }
    @Test
    void shouldFindAll() {
        List<SecurityClearance> all = repository.findAll();
        assertEquals(2, all.size());
    }
    @Test
    void shouldFindByName() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance actual = repository.findByName("Secret");
        assertEquals(secret, actual);
    }
    @Test
    void shouldAdd() {
        SecurityClearance clearance = new SecurityClearance();
        clearance.setName("New Security");
        SecurityClearance actual = repository.add(clearance);
        assertNotNull(actual);
        assertEquals("New Security", actual.getName());
    }

    @Test
    void shouldUpdate() {
        SecurityClearance secret = repository.findById(1);
        secret.setName("Updated Secret");
        assertTrue(repository.update(secret));
        assertEquals("Updated Secret", repository.findById(1).getName());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));
        assertNull(repository.findById(1));
    }

    @Test
    void shouldGetUsageCount() {
        int count = repository.getUsageCount(1);
        assertEquals(6, count);
    }
}