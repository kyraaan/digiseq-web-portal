package com.digiseq.digiseqwebportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
public abstract class BaseRepositoryIntegrationTest {

  protected static DSLContext dslContext;

  @Container
  static PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:13")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("password");

  @BeforeAll
  static void setUp() throws SQLException {
    postgresContainer.start();

    Connection connection =
        DriverManager.getConnection(
            postgresContainer.getJdbcUrl(),
            postgresContainer.getUsername(),
            postgresContainer.getPassword());

    dslContext = DSL.using(connection);

    dslContext.execute(
        "CREATE TABLE CLIENTORG ("
            + "CLIENT_ID SERIAL PRIMARY KEY,"
            + "NAME VARCHAR(255),"
            + "REGISTERED_DATE DATE,"
            + "EXPIRY_DATE DATE,"
            + "ENABLED BOOLEAN"
            + ")");

    dslContext.execute(
        "CREATE TABLE PERSONNEL ("
            + "PERSONNEL_ID SERIAL PRIMARY KEY,"
            + "USERNAME VARCHAR(255),"
            + "PASSWORD VARCHAR(255),"
            + "FIRST_NAME VARCHAR(255),"
            + "LAST_NAME VARCHAR(255),"
            + "EMAIL VARCHAR(255),"
            + "PHONE_NUMBER VARCHAR(255),"
            + "CLIENT_ID INT,"
            + "FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTORG(CLIENT_ID)"
            + ")");
  }

  @AfterEach
  void cleanUp() {
    dslContext.execute("DELETE FROM PERSONNEL");
    dslContext.execute("DELETE FROM CLIENTORG");
  }

  @AfterAll
  static void tearDown() {
    postgresContainer.stop();
  }
}
