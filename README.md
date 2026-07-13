````markdown
# devxbackend

devxbackend is a Spring Boot application providing a secure internal portal for enterprise banking users. It acts as a presentation and orchestration layer for approved business services, enabling users to access operational information and complete authorised workflows through a consistent web interface.

The application is designed to operate within a controlled banking environment and follows enterprise requirements for security, resilience, auditability, observability, and controlled software delivery.

## Application Metadata

```properties
assignment_group = xxxxx
application = devxbackend
release = RLSE123456
impact = 3
````

| Attribute        | Value                       |
| ---------------- | --------------------------- |
| Application      | devxbackend                |
| Assignment Group | Central Team                       |
| Impact           | 3                           |
| Application Type | Spring Boot Web Application |
| Environment      | Enterprise Banking          |
| Support Model    | Assignment-group owned      |
| Harness Pipeline | devx_pipeline |


## Key Capabilities

devxbackend provides:

* Secure access to approved banking services and operational workflows
* Integration with internal APIs and enterprise systems
* Role-based access control
* Centralised validation and error handling
* End-to-end request tracing and audit logging
* Health and readiness endpoints for platform monitoring
* Externalised environment-specific configuration
* Automated build, test, security scanning, and deployment support

## Technology Stack

* Java
* Spring Boot
* Spring Web
* Spring Security
* Spring Boot Actuator
* Maven
* JUnit
* REST APIs
* Container-based deployment

Additional dependencies and their versions are defined in `pom.xml`.

## Repository Structure

```text
.
├── src
│   ├── main
│   │   ├── java
│   │   └── resources
│   │       ├── application.yml
│   │       └── static
│   └── test
│       └── java
├── pom.xml
├── mvnw
├── mvnw.cmd
├── Dockerfile
└── README.md
```

## Prerequisites

The following software is required for local development:

* A supported Java Development Kit
* Maven, or the Maven wrapper included in the repository
* Access to required internal services
* Valid development credentials supplied through the approved secrets-management process

Verify the installed Java version:

```bash
java -version
```

## Building the Application

Build the application and execute all tests:

```bash
./mvnw clean verify
```

On Windows:

```powershell
mvnw.cmd clean verify
```

Build without executing tests:

```bash
./mvnw clean package -DskipTests
```

The packaged application will be created under:

```text
target/
```

## Running Locally

Start the application using Maven:

```bash
./mvnw spring-boot:run
```

Alternatively, run the packaged JAR:

```bash
java -jar target/HARNESS-portal.jar
```

To activate a specific Spring profile:

```bash
java -jar target/HARNESS-portal.jar \
  --spring.profiles.active=local
```

The application is typically available locally at:

```text
http://localhost:8080
```

The actual port can be changed through the application configuration.

## Configuration

Configuration is externalised using Spring Boot properties and environment variables.

Example environment variables:

```bash
export SPRING_PROFILES_ACTIVE=local
export SERVER_PORT=8080
export LOGGING_LEVEL_ROOT=INFO
```

Typical configuration files include:

```text
application.yml
application-local.yml
application-test.yml
```

Production credentials, API keys, certificates, passwords, and tokens must not be committed to this repository.

Secrets must be retrieved at runtime from the bank-approved secrets-management platform.

## Security

devxbackend must comply with the organisation's security standards.

Security controls include:

* Authentication through the approved enterprise identity provider
* Role-based authorisation
* TLS for all network communication
* Secure handling of sessions and authentication tokens
* Protection against common web application vulnerabilities
* Dependency and container vulnerability scanning
* Audit logging for security-relevant actions
* Removal or masking of sensitive information from logs
* Runtime retrieval of secrets from an approved secrets manager

Never include the following information in source control:

* Passwords
* API keys
* Access tokens
* Private certificates
* Customer or account information
* Personally identifiable information

## API Integration

Connections to downstream systems should use approved enterprise integration patterns.

All outbound requests should include, where supported:

* Correlation ID
* Request or trace ID
* Application identity
* Appropriate authentication credentials
* Defined connection and response timeouts

Integrations should implement suitable handling for:

* Connection failures
* Timeouts
* Invalid responses
* Authentication failures
* Rate limiting
* Temporary downstream-service unavailability

Retries must only be enabled where the operation is safe and idempotent.

## Health Checks

Spring Boot Actuator provides operational endpoints for monitoring and platform health checks.

Common endpoints include:

```text
/actuator/health
/actuator/health/liveness
/actuator/health/readiness
/actuator/info
/actuator/metrics
```

The availability and exposure of individual endpoints are controlled through application configuration.

Health endpoints must not expose sensitive configuration, credentials, infrastructure information, or internal implementation details.

## Logging and Observability

Application logging should use structured, machine-readable output in deployed environments.

Logs should include:

* Timestamp
* Log level
* Application name
* Environment
* Correlation ID
* Trace ID
* Request path
* Response status
* Processing duration

Sensitive values must be masked or excluded.

The following information must not be logged:

* Passwords
* Authentication tokens
* Session identifiers
* Payment information
* Customer account information
* Personally identifiable information
* Secret or certificate contents

Application metrics and traces should be exported to the organisation's approved observability platform.

## Testing

Run all automated tests:

```bash
./mvnw test
```

Run the complete verification lifecycle:

```bash
./mvnw clean verify
```

The test suite should include:

* Unit tests
* Controller tests
* Service-layer tests
* Integration tests
* Security tests
* Contract tests for critical integrations
* Negative and error-handling scenarios

Changes must not be merged when mandatory tests or quality controls are failing.

## Code Quality

All contributions should comply with the repository's configured quality controls.

These may include:

* Static code analysis
* Dependency vulnerability scanning
* Secret detection
* Software composition analysis
* Code-coverage thresholds
* Container image scanning
* Deployment-policy validation

Quality and security findings must be reviewed before release approval.

## Container Build

Build the application:

```bash
./mvnw clean package
```

Build the container image:

```bash
docker build -t HARNESS-portal:local .
```

Run the container locally:

```bash
docker run --rm \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  HARNESS-portal:local
```

Production images must be built and published only through the approved CI/CD pipeline.

## Deployment

devxbackend is deployed through the organisation's controlled delivery pipeline.

A standard release process includes:

1. Source-code validation
2. Unit and integration testing
3. Static code analysis
4. Dependency and secret scanning
5. Application packaging
6. Container-image creation
7. Container vulnerability scanning
8. Deployment-policy validation
9. Deployment to a lower environment
10. Automated acceptance and regression testing
11. Change approval
12. Controlled production deployment
13. Post-deployment validation

Manual production deployment from a developer workstation is not supported.

## Release Information

Current release:

```text
RLSE123456
```

All production changes must be associated with an approved release and change record.

The release record should include:

* Scope of change
* Business and technical impact
* Test evidence
* Security evidence
* Deployment plan
* Validation plan
* Rollback plan
* Required approvals

## Rollback

Application rollback normally involves redeploying the previously approved artifact or container image.

Before deployment, confirm that:

* The previous production version remains available
* Database changes are backward compatible
* Feature flags can be disabled where applicable
* Configuration changes can be reverted
* The rollback procedure has been validated
* Operational teams understand the recovery steps

Database changes requiring destructive or irreversible operations must include a separately reviewed recovery strategy.

## Operational Support

Operational incidents and support requests should be routed using the following metadata:

```properties
assignment_group = xxxxx
application = devxbackend
release = RLSE123456
impact = 3
```

When raising an incident, provide:

* Environment
* Application version
* Release identifier
* Time of failure
* Correlation or trace ID
* Affected functionality
* Number of affected users
* Relevant error messages
* Recent deployment or configuration changes
* Steps already taken
* Supporting logs and monitoring evidence

Do not attach credentials, authentication tokens, customer data, or other sensitive information to support records.

## Contribution Process

Before submitting a change:

1. Create a branch from the approved base branch.
2. Implement the change following project coding standards.
3. Add or update automated tests.
4. Run the local verification suite.
5. Confirm that no secrets or sensitive information are included.
6. Submit a pull request.
7. Obtain the required peer and code-owner approvals.
8. Resolve all mandatory quality and security findings.
9. Merge through the approved repository workflow.

Example:

```bash
git checkout -b feature/short-description
./mvnw clean verify
git add .
git commit -m "Add short description of change"
git push origin feature/short-description
```

## Ownership

This application is owned and supported by:

```text
Assignment Group: xxxxx
Application:      devxbackend
Release:          RLSE123456
Impact:           3
```

For production incidents, follow the organisation's standard incident-management and escalation procedures.

```
```
