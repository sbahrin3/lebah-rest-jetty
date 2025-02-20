# lebah-rest-jetty

This is my implementation of a REST API — a lightweight, straightforward, and efficient (I hope.. hehe) solution for those familiar with Java. It’s perfect for you to get started quickly without diving into more complex frameworks like Spring Boot, Jersey, Apache CXF, or other popular options.

Why did I create this or 'reinvent the wheel'? The answer is simple: I developed this framework for my current project, which involves building a REST API for financial solutions.

I wanted to avoid the complexity of Spring Boot and the need to learn other new frameworks. So, I created my solution, which runs seamlessly out of the box with an embedded Jetty server.

# Examples

Create a REST class by extending it to RestRequest, and annotate it with Path.
```java
@Path("/users")
public class Users extends RestRequest

```

Create a REST Method by annotating a method with @Post, @Get, @Put, or @Delete according to what it does.

For example:

A method that returns a list of users:
```java
@Get("/")
public void listUsers()
```

A method that adds a user:
```java
@Post("/")
public void addUser()
```

A method that returns a user:
```java
@Get("/{userId}")
public void getUser()
```

A method that updates user data:
```java
@Put("/{userId}")
public void updateUser(UserData userData)
```

A method that deletes a user:
```java
@Delete("/{userId}")
public void deleteUser()
```

A method that assigns roles to a user:
```java
@Post("/{userId}/roles")
public void assignRolesToUser(RolesData rolesData)
```

A method that returns the list of roles of a user:
```java
@Get("/{userId}/roles")
public void listUserAssignedRoles()
```

A method that deletes a role from a user:
```java
@Delete("{userId}/roles/${roleId}"
public void deleteRoleFromUser()
```

```


