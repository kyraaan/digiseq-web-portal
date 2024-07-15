## Digiseq Web Portal
A Web Portal Application to allow the modification and management of Client Organisations and Personnel

### Stack
- Java 21
- Spring Boot 3.3
- Maven
- PostgreSQL
- Jooq
- Flyway

### Running Locally

To start the application locally, follow these steps:

- `mvn clean package -DskipTests`

- `docker build -t digiseq-web-portal`

- `docker-compose up` - This will bring up the app, the postgres instance and the pgadmin portal to access the db.

To tear down
`docker-compose down -v`
### To run the app via IntelliJ (useful for debugging)

- `docker-compose up --scale app=0` This will start up all dependencies excluding the java app
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

The docs can be found: `http://localhost:8090/v3/api-docs`