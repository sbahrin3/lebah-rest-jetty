# lebah-rest-jetty

This is my implementation of a REST API — a lightweight, straightforward, and efficient (I hope.. hehe) solution for those familiar with Java. It’s perfect for you to get started quickly without diving into more complex frameworks like Spring Boot, Jersey, Apache CXF, or other popular options.

Why did I create this or 'reinvent the wheel'? The answer is simple: I developed this framework for my current project, which involves building a REST API for financial solutions.

I wanted to avoid the complexity of Spring Boot and the need to learn other new frameworks. So, I created my solution, which runs seamlessly out of the box with an embedded Jetty server.

# Examples

Create a REST class by extending it to RestRequest, and annotate it with @Path.
```java
@Path("/users")
public class Users extends RestRequest

```

Create a REST Method by annotating a method with @Post, @Get, @Put, or @Delete according to what it does.

### For example:

### A method that returns a list of users:
```java
@Get("/")
public void listUsers()
```
HTTP GET Request
```
GET http://localhost:8080/users?pageNumber=5&pageSize=10&orderBy=fullName
```


### A method that adds a user:
```java
@Post("/")
public void addUser()
```
HTTP POST Request
```
POST http://localhost:8080/users

Content-Type: application/json
{ 
    "fullName": "Jafar Darwis",
    "email": "jafardarwis@yahoo.com",
    "identificationNumber": "H91223345"
}
```

### A method that returns a user:
```java
@Get("/{userId}")
public void getUser()
```
HTTP GET Request
```
GET http://localhost:8080/users/13
```

### A method that updates user data:
```java
@Put("/{userId}")
public void updateUser(UserData userData)
```
HTTP PUT Request
```
PUT http://localhost:8080/users/13

Content-Type: application/json
{ 
    "fullName": "Muhammad Jafar bin Darwis",
    "identificationNumber": "H91223345"
}
```

### A method that deletes a user:
```java
@Delete("/{userId}")
public void deleteUser()
```
HTTP DELETE Request
```
DELETE http://localhost:8080/users/23
```

### A method that assigns roles to a user:
```java
@Post("/{userId}/roles")
public void assignRolesToUser(RolesData rolesData)
```
HTTP PUT Request
```
PUT http://localhost:8080/users/13/roles

Content-Type: application/json
{ 
    "roles": [
        "1",
        "3",
        "8"
    ]
}
```

### A method that returns the list of roles of a user:
```java
@Get("/{userId}/roles")
public void listUserAssignedRoles()
```
HTTP GET Request
```
GET http://localhost:8080/users/13/roles

```

### A method that deletes a role from a user:
```java
@Delete("{userId}/roles/${roleId}")
public void deleteRoleFromUser()
```
HTTP DELETE Request
```
DELETE http://localhost:8080/users/13/roles/1

```



