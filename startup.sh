#!/bin/bash

# Start up all dependencies excluding the java app
docker-compose up -d --scale app=0

# Run Flyway migrations
mvn flyway:migrate

# Create the JAR
mvn clean package -DskipTests

# Build the image
docker build -t digiseq-web-portal .

# And finally, spin up the app
docker-compose up app
