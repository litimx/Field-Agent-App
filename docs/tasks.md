
# Field Agent API Assessment

## Tasks

## We Do

### Set Up

* [X] create a repo
* [X] add project files, assessment plan, hint doc, and http tests
* [X] update git ignore
      ```
      # IntelliJ
      .idea/
      out/

      # Maven
      target/

      # npm
      node_modules/

      # macOS
      .DS_Store
      ```
* [X] Update the POM file
* [X] set up application.properties file
* [X]  Configure environment variables within IntelliJ's run configurations
  * [X] Spring Boot run configuration
  * [X] JUnit run configuration
* [X] set up SpringApplication in App class - build project 


### Database 

* [X] Update known good state stored procedure
  * [ ] Reset and seed security clearances
  * [ ] Reset and seed aliases
  * [ ] Use Workbench to test the known good state stored procedure

```sql
use field_agent_test;
set SQL_SAFE_UPDATES = 0;
call set_known_good_state();
set SQL_SAFE_UPDATES = 1;
```

 * [ ] add some test inserts to production code so that we have a little bit of starter data 

### Alias

* [X] Add the `Alias` model class
  * [X] Add what's missing on `Agent` model class (field for list of aliases)

* [X] Find an agent with their aliases attached
  * Update the `AgentJdbcTemplateRepository.findById()` method to load aliases for the specified agent
  * Update the `AgentJdbcTemplateRepository.deleteById()` method to delete from the `alias` table

* [X] Create the `AliasMapper implements RowMapper<Alias>`

* [ ] Implement aliases CRUD
  * [ ] Features
    * Add an alias
    * Update an alias
    * Delete an alias

* [X] Add the `AliasRepository` interface
    * `List<Alias> findByName(String name)` (to support alias validation)
    * `Alias add(Alias alias)`
    * `boolean update(Alias alias)`
    * `boolean deleteById(int aliasId)`

  * [X] Add the `AliasJdbcTemplateRepository` class
    * `List<Alias> findByName(String name)` (to support alias validation)
    * `Alias add(Alias alias)`
    * `boolean update(Alias alias)`
    * `boolean deleteById(int aliasId)`

  * [X] Add the `AliasJdbcTemplateRepositoryTest` class
  * [X] Extract the `AliasRepository` interface

  * [X] Add the `AliasService` class
    * `Result<Alias> add(Alias alias)`
    * `Result<Alias> update(Alias alias)`
    * `boolean deleteById(int aliasId)`

  * [ ] Add the `AliasServiceTest` class

  * [X] Add the `AliasController` class
    * `@PostMapping` `ResponseEntity<Object> add(@RequestBody Alias alias)`
    * `@PutMapping("/{aliasId}")` `ResponseEntity<Object> update(@PathVariable int aliasId, @RequestBody Alias alias)`
    * `@DeleteMapping("/{aliasId}")` `ResponseEntity<Void> deleteById(@PathVariable int aliasId)`


**** STOP ****

## You Do 

* [X] create a branch named development `git checkout -b development`

### Security Clearance 

* [ ] Implement security clearance CRUD
  * [ ] Features
    * Find all security clearances
    * Find a security clearance by id
    * Add a security clearance
    * Update a security clearance
    * Delete a security clearance (challenge)
  * [X] Add the following methods to the `SecurityClearanceRepository` interface and `SecurityClearanceJdbcTemplateRepository` class
    * `List<SecurityClearance> findAll()`
    * `SecurityClearance findById(int securityClearanceId)`
    * `SecurityClearance add(SecurityClearance securityClearance)`
    * `boolean update(SecurityClearance securityClearance)`
    * `boolean deleteById(int securityClearanceId)`
    * `int getUsageCount(int securityClearanceId)`
  * [ ] Update the `SecurityClearanceJdbcTemplateRepositoryTest` class with additional test casess
  * [X] Add the `SecurityClearanceService` class
    * `List<SecurityClearance> findAll()`
    * `SecurityClearance findById(int securityClearanceId)`
    * `Result<SecurityClearance> add(SecurityClearance securityClearance)`
    * `Result<SecurityClearance> update(SecurityClearance securityClearance)`
    * `Result<SecurityClearance> deleteById(int securityClearanceId)`
  * [X] Add the `SecurityClearanceServiceTest` classs
  * [X] Add the `SecurityClearanceController` class
    * `@GetMapping` `List<SecurityClearance> findAll()`
    * `@GetMapping("/{securityClearanceId}")` `ResponseEntity<Object> findById(@PathVariable int securityClearnaceId)`
    * `@PostMapping` `ResponseEntity<Object> add(@RequestBody SecurityClearance securityClearance)`
    * `@PutMapping("/{securityClearanceId}")` `ResponseEntity<Object> update(@PathVariable int securityClearanceId, @RequestBody SecurityClearance securityClearance)`
    * `@DeleteMapping("/{securityClearanceId}")` `ResponseEntity<Object> deleteById(@PathVariable int securityClearanceId)`

* [X] Implement global error handling
  * [X] Return a specific data integrity error message for data integrity issues
  * [X] Return a general error message for issues other than data integrity

* [ ] Run through the HTTP test plan and ensure that all test cases pass

* [ ] Review the prewritten plan and confirm that I implemented all of the requirements

## Requirements Checklist

* [ ] Find all security clearances
* [ ] Find a security clearance by id
* [ ] Add a security clearance
* [ ] Update a security clearance
* [ ] Delete a security clearance (challenge)
* [ ] Find agent with aliases
* [ ] Add an alias
* [ ] Update an alias
* [ ] Delete an alias
* [ ] Global Error Handling (correctly handles data and general errors differently)
* [ ] Test data components (all data components are tested with valuable tests)
* [ ] Test domain components (all domain components are tested with valuable tests)
* [ ] Java Idioms (excellent layering, class design, method responsibilities, and naming)

## Test Plan

_If the trainee followed instructions during kickoff, they should have an HTTP file with a good sequence of events for demonstrating CRUD capabilities._

### Security Clearance

* [ ] GET all security clearances
* [ ] GET a security clearance by ID
* [ ] For GET return a 404 if security clearance is not found
* [ ] POST a security clearance
* [ ] For POST return a 400 if the security clearance fails one of the domain rules
  * [ ] Security clearance name is required
  * [ ] Name cannot be duplicated
* [ ] PUT an existing security clearance
* [ ] For PUT return a 400 if the security clearance fails one of the domain rules
* [ ] DELETE a security clearance that is not in use by ID
* [ ] For DELETE return a 404 if the security clearance is not found
* [ ] For DELETE return a 400 if the security clearance is in use 

### Alias

* [ ] GET an agent record with aliases attached
* [ ] POST an alias
* [ ] For POST return a 400 if the alias fails one of the domain rules
  * [ ] Name is required
  * [ ] Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.
* [ ] PUT an alias
* [ ] For PUT return a 400 if the alias fails one of the domain rules
* [ ] DELETE an alias by ID
* [ ] For DELETE Return a 404 if the alias is not found

### Global Error Handling

* [ ] Return a specific data integrity error message for data integrity issues
* [ ] Return a general error message for issues other than data integrity