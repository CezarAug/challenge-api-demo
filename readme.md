[![Java CI with Gradle](https://github.com/CezarAug/challenge-api-demo/actions/workflows/build.yml/badge.svg?branch=main&event=push)](https://github.com/CezarAug/challenge-api-demo/actions/workflows/build.yml)

## My experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 7 years experience in Java and have been using Spring Boot since the first projects
- In the last 1.5 year I have been working with Spring + Kotlin
- During this period, the majority of projects were integration projects. (Kafka consumers, BFFs)

## How to use this spring-boot project

- Install packages with `gradle build`
- Run `gradle bootRun` for starting the application (or use your IDE)

#### Docker (Recommended)
- Docker compose is also available (default profile: PROD)
  ```
  docker build -t challenge/employee-api:0.0.1-SNAPSHOT ./
  docker-compose up
  ```
  
### API docs and Postman collection
Swagger UI: ``http://localhost:8080/swagger-ui.html``
Postman Collection: [Postman collection](.postman%2Femployee-api-demo.postman_collection.json)

### Authorization
The API has just a Basic authorization, try sending requests with user: ``user`` and password: ``password``.
Or an Authorization header with: ```'Authorization: Basic dXNlcjpwYXNzd29yZA=='```

Or check the Postman collection for request examples.

### Available Spring Profiles

There are two profiles in this project

- ``Local``
  - All SQL queries created by JPA will be printed in the console.
  - JPA is responsible for building the database.
- ``Prod``
  - Profile used in the docker compose. 
  - Queries output and H2 console are disabled
  - Database creation and initial dataset will be handled by Liquibase

### Other tasks

##### Checkstyle

A version of google checks was configured for this project with some minor tweaks.
```
gradle checkstyleMain
gradle checkstyleTest
```

##### Jacoco Report

There are two gradle tasks available.
```
gradle jacocoTestReport
gradle jacocoTestCoverageVerification
```
Test verification will be always run after the report (80% coverage filter).

Test report is available at: ``build/reports/jacoco/test/html/index.html``

##### OWASP Dependency Check
```gradle dependencyCheckAnalyze```
- Please notice that the first execution might take a while to complete.

Currently, the CVSS score is set as 11 (default) just for demo purposes.

They can be set to lower levels and each dependency that has an issue can be configured as an exception with an expiration date. (Then builds will start to fail again and the team has to take action by updating or delaying it).

Report can be found at: ``build/reports/dependency-check-report.html``

### Database

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testDb` for H2 UI.


# Changes and Functionalities added to the project
- Upgraded Spring to the latest possible version for Java 8
  - Due to possible issues with security in previous versions, such as the log4J issue.
- Moved away from Autowired to constructor based injection
  - Unit test friendly :)
- JUnit 5
  - When Spring was upgraded, there is no longer a bundled Junit 4.
- Formatting
  - Added checkstyle (Using google checks)
    - Removed mandatory javadocs in order to add them where they are really necessary.
- Database
  - Prod profile (docker compose) only:
    - Liquibase migrations controlling the schema and not JPA auto generation.
    - Database pre-populated with some test data.
- General changes
  - Added Request object DTO for Employee to avoid exposing the entity directly.
    - Mapstruct for DTO and Entity conversions. (Could have been others)
  - Moved some logic from the controller layer to the service layer on the update request.
  - Added pagination and sorting for findAll employees.
    - The idea is to deny selecting everything at once and limit consumers.
  - Spring Profiles:
    - Enabling and disabling functionalities according to the environment.
  - Cache: Added a simple ConcurrentMapCache.
  - Security: Basic authorization was added.
  - Added validation
  - Fixed some issues when requesting for invalid employees.
- Tests:
  - Added Jacoco report with coverage targets in the build step.
  - Added dependency check
- Build:
  - Added a basic pipeline on GitHub actions to run the tests and check if it builds.
  - Added a step for jacoco report checking for at least 80% coverage (Results are shown in the PR comments)
  - Added an OWASP dependency check step

# Pending tasks and things I'd check with more time
- Should have started with git for a more distributed commit pace. (Sorry)
- Java 17
  - Remove Lombok: During Java 8 era it was the law, but with the evolutions introduced in newer versions it is no longer vital. Now it raises questions and discussions about the trade-off of having extra/unnecessary load. Even not knowing what actually will be generated as bytecode in the end.
  - Spring: Update it to the latest versions. Now Java 17 is required.
- Employee salary: Could have been better (decimals)
- Employee department also could have been separated from the Employee entity
- Employee JPA operations:
  - Update: Would play a little more with writing an update VS using getReference.
  - Would check future entities in order to check possible issues with multiple selects being created. (N + 1 Issue)
  - Post operation could return the id or the full EmployeeResponse object.
- Caching: Try using another solution (Redis most likely)
- Checkstyle: A more fine tune in some configurations (Javadoc for instance)
- SpringDoc: There is an issue with the swagger generated vs the actual objects (_embedded, _links)
- Spring Security: Create an authorization provider and use a proper JWT.
- Tracing: Configure Micrometer Tracing (Previously Known as Spring Cloud Sleuth)
- API Operations
  - Add more operations to search for an employee and not just the ID.
- Liquibase: Could use generate changelog in order to get a database agnostic configuration (Now everything is in SQL)
- Dependency check: Be more strict with the severity of the vulnerabilities and add an exclusion list.

## Original Readme info
### Instructions

- download the zip file of this project
- create a repository in your own GitHub named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues
