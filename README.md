# MyRetail RESTful API

## Overview

This is a simple RESTful application hosted on **Heroku**.

- **Hosted URL**: [MyRetail RESTful API](https://myretail-restful-66be21578f77.herokuapp.com/products/{id})
- **Note**: The only product currently loaded into the database is **ID=13860428**
- **Swagger Documentation**: [Swagger UI](https://myretail-restful-66be21578f77.herokuapp.com/swagger-ui.html)

---

## Running Locally

To run this application locally, you need to set the following environment variables:

- `MONGOUSER`: MongoDB username
- `MONGOPASSWORD`: MongoDB password
- `REDSKYKEY`: Key for accessing the external RedSky API

---

## Database

This application uses **MongoDB** hosted on [MongoDB Atlas](https://www.mongodb.com/), with the database whitelisted to **all IPs** (since Heroku doesn't provide static IPs).

---

## Future Enhancements

1. **User Authentication**  
   Add authentication and authorization for securing the API.
2. **Swagger Integration**  
   Include Swagger documentation for API endpoints.
3. **Groovy/Spock Integration Tests**  
   Write integration tests for the API using **Groovy/Spock** (I was unable to complete this due to issues installing Docker on my personal Windows machine).
4. **Metrics and Alerting**

---