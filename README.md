# StoryVault Service README

## **Overview**
StoryVault is a RESTful API service designed to manage Story entries. It allows users to create, update, retrieve, and delete Story entries while providing secure authentication using JWT (JSON Web Token). The service is documented using OpenAPI 3.1.0 and includes endpoints for both Story management and authentication.

## **Base URL**
```
http://localhost:8080
```

---

## **Authentication**
The API uses **JWT Bearer Authentication** for securing endpoints. To access protected routes, you must first authenticate using the `/authenticate` endpoint to obtain a token. Include the token in the `Authorization` header of your requests as follows:
```
Authorization: Bearer <your-token>
```

---

## **Endpoints**

### **Story Management**

#### 1. **Create a New Entry**
- **Endpoint**: `POST /new-entry`
- **Description**: Creates a new Story entry.
- **Request Body**:
  ```json
  {
      "description":"A productive day!",
      "entryDate": "2025-01-29"
  }
  ```
- **Response**:
  ```json
  {
      "id": 1,
      "day": "Wednesday",
      "description": "A productive day!",
      "entryDate": "2025-01-29"
  }
  ```

#### 2. **Update an Entry**
- **Endpoint**: `PUT /update-entry`
- **Description**: Updates an existing Story entry.
- **Request Body**:
  ```json
  {
      "id": 1,
      "day": "Monday",
      "description": "An updated productive day!",
      "entryDate": "2025-01-30"
  }
  ```
- **Response**:
  ```json
  {
      "id": 1,
      "day": "Monday",
      "description": "An updated productive day!",
      "entryDate": "2025-01-30"
  }
  ```

#### 3. **Get an Entry by ID**
- **Endpoint**: `GET /get-entry/{id}`
- **Description**: Retrieves a specific Story entry by its ID.
- **Path Parameter**:
    - `id` (integer): The ID of the Story entry.
- **Response**:
  ```json
  {
      "id": 1,
      "day": "Monday",
      "description": "A productive day!",
      "entryDate": "2025-01-30"
  }
  ```

#### 4. **Get All Entries**
- **Endpoint**: `GET /get-all`
- **Description**: Retrieves all Story entries.
- **Response**:
  ```json
  [
      {
          "id": 1,
          "day": "Monday",
          "description": "A productive day!",
          "entryDate": "2025-01-30"
      },
      {
          "id": 2,
          "day": "Tuesday",
          "description": "Another great day!",
          "entryDate": "2025-01-31"
      }
  ]
  ```

#### 5. **Delete an Entry by ID**
- **Endpoint**: `DELETE /delete-entry/{id}`
- **Description**: Deletes a specific Story entry by its ID.
- **Path Parameter**:
    - `id` (integer): The ID of the Story entry.
- **Response**:
  ```json
  "Success"
  ```

---

### **Authentication**

#### Authenticate User
- **Endpoint**: `POST /authenticate`
- **Description**: Authenticates a user and provides a JWT token for authorization.
- **Request Body**:
  ```json
  {
      "username": "admin",
      "password": "password"
  }
  ```
- **Response**:
  ```json
  {
      "token": "<jwt_token>"
  }
  ```

---

## **Schemas**

### Story Object
```json
{
    "id": {
        "type": "integer",
        "format": "int32"
    },
    "day": {
        "type": "string"
    },
    "description": {
        "type": "string"
    },
    "entryDate": {
        "type": "string"
    }
}
```

### AuthRequest Object
```json
{
    "username": {
        "type": "string"
    },
    "password": {
        "type": "string"
    }
}
```

### JwtResponse Object
```json
{
    "token": {
        "type": "string"
    }
}
```

---

## **Security**
This API uses JWT Bearer Authentication. Secure endpoints require the following header:
```
Authorization: Bearer <jwt_token>
```

To obtain a token, use the `/authenticate` endpoint with valid credentials.

---

## **Setup Instructions**

### Prerequisites
1. Java Development Kit (JDK) installed.
2. Maven or Gradle for dependency management.

### Steps to Run the Service
1. Clone the repository and navigate to the project directory.
2. Build the project using Maven or Gradle:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   java -jar target/StoryVault-service.jar
   ```
4. Access Swagger UI for API documentation at:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

---

## **Swagger Documentation**
The API is documented using Swagger (OpenAPI). You can explore and test all endpoints via Swagger UI at:
```
http://localhost:8080/swagger-ui/index.html [JWT protected]
http://localhost:8080/v3/api-docs
```

---

## **Contact**
For any issues or inquiries, please contact [sundarmachani@gmail.com].