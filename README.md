## Digiseq Web Portal
A Web Portal Application to allow the modification and management of Client Organisations and Personnel

### Stack
- Java 21
- Spring Boot 3.3
- Maven
- Docker
- PostgreSQL
- jOOQ
- Flyway

### Running Locally

To start the application locally, follow these steps:

Unfortunately, jOOQ requires an active database connection, in order to generate the records during compilation.
So we must first spin up our postgres instance before we can run any maven builds

Run the following script:
```sh
./startup.sh
```

Then you should be able to see the health of the service http://localhost:8090/actuator/health

To tear down
`docker-compose down -v`
### To run the app via IntelliJ (useful for debugging)
- `docker-compose up --scale app=0` 
- Start the app via IntelliJ on `local` profile

### Database access
Access the local PGAdmin portal for database management:

- Portal URL: http://localhost:8001/browser/
- Credentials:
  - Email: admin@admin.com
  - Password: password
#### Connection Details
Use these details to connect to the Postgres server:

- **Hostname/Address:** postgres 
- **Port:** 5432
- **Username:** postgres
- **Password:** postgres

### Documentation
The OpenAPI swagger-ui can be found here: http://localhost:8090/swagger-ui/index.html

The API docs can be found here: http://localhost:8090/v3/api-doc

Some sample requests can be found here: - `src/test/resources/requests`