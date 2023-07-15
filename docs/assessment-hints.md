
# Field Agent API Assessment Hints

_Here are some additional hints for the Field Agent assessment..._

## Updating known good state

It can be tricky to get the `set_known_good_state()` stored procedure updated correctly to support your repository unit tests.

* The `insert into` statements that insert data into the `security_clearance` table are outside of the `set_known_good_state()` stored procedure in the provided scripts. You'll want to move those inserts into the `set_known_good_state()` stored procedure.
* Pay close attention to the order of the `delete` statements. You can't delete a row whose ID is being referenced by a row in another table. Here's a trick that you can use: organize your deletes in the reverse order of the inserts.
* Be sure to re-run the entire `field-agent-schema-test.sql` script every time that you make a change to the `set_known_good_state()` stored procedure. This will drop and recreate the stored procedure so your code changes within the stored procedure will take effect.

## Using the expected column names between database column names and row mapper names

Resist the temptation to change any of the column names within any of the provided RowMappers! In particular, you'll likely run in an issue with the provided `SecurityClearanceMapper` in that it expects a column named `security_clearance_name` but the `security_clearance` table column name is `name`.

This is easy to forget, but you can easily alias a column name in your SQL query:

```sql
select security_clearance_id, name as security_clearance_name from security_clearance;
```

## Correctly handling the agent with aliases requirement

The assessment instructions ask you to "Fetch an individual agent with aliases attached." Fight the temptation to add a method to your `AliasController` class to implement this requirement.

**You do not need to add a new controller method to implement this requirement!**

Take a close look at the `AgentController.findById()` controller method. What service method does the controller method call? What repository method does the service call? Closely review the repository method that retrieves a single agent from the database. _Is there a way that you could extend this method to include the agent's aliases?_

## Correctly handling the validation to prevent in-use security clearances from being deleted

The provided assessment plan suggests that you handle this requirement as a domain level validation. Using this approach, you can return a specific error message to the client so that they know exactly why the delete request failed.

The assessment plan shows two different SQL query approaches: one that retrieves all of the data from the `agency_agent` table for the security clearance ID that you're about to delete and one that just returns the row count from the agency_agent table for the security clearance ID that you're about to delete:

```sql
select * from agency_agent where security_clearance_id = 1;
```

```sql
select count(*) from agency_agent where security_clearance_id = 1;
```

_Note: These queries will need to be updated to be used within a repository. The first query will need column names and possibly joins to other tables. Both queries will need the `security_clearance_id` values parameterized._

We didn't cover in the course how to use JdbcTemplate to retrieve a single value from the database. Here's a code example of how to do that:

```java
int count = jdbcTemplate.queryForObject(
  "select count(*) from agency_agent where security_clearance_id = ?;", Integer.class, securityClearanceId);
```

## Fixing failing controller tests after adding global error handling

After implementing your "catch all" global exception handler, two of the provided controller unit tests will start to fail. Those controller unit tests are expecting specific HTTP status codes to be returned for the unhappy paths that they're testing for. If you set a breakpoint in your global exception handler and debug each of those failing tests (one at a time), you can see the exception types that are occurring.

To resolve this issue, you can do one of two things:

1. Rethrow those exception types inside of your global exception handler. Rethrowing allows Spring MVC to handle those exceptions (as it was doing before you added your global error handler).
2. Define an additional exception handler (one for each of the exception types that are being handled incorrectly) so you can return the expected HTTP status code.