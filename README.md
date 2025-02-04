# lebah-rest-jetty

This is my implementation of a REST API—a lightweight, straightforward, and efficient (I hope.. hehe) solution for those familiar with Java. It’s perfect if you want to get started quickly without diving into more complex frameworks like Spring Boot, Jersey, Apache CXF, or other popular options.

Why did I create this or 'reinvent the wheel'? The answer is simple: I developed this framework specifically for my current project, which involves building a REST API for banking solutions.

I wanted to avoid the complexity of Spring Boot and the need to learn other new frameworks. So, I created my own solution, which runs seamlessly out of the box with an embedded Jetty server.

# Examples

To get list of Users:

``` markdown
GET http://localhost:8080/users
```

Result:

```markdown

HTTP STATUS 200

{
    "count": 168,
    "list": [
        {
            "username": "rodger.grady@hotmail.com",
            "firstName": "Clark",
            "lastName": "Cummerata"
        },
        {
            "username": "roger.purdy@gmail.com",
            "firstName": "Dewitt",
            "lastName": "Mraz"
        },
        {
            "username": "ardell.powlowski@hotmail.com",
            "firstName": "Diego",
            "lastName": "Bechtelar"
        },
```

To add a new User:

```markdown
POST http://localhost:8080/users

JSON BODY:
{
    "username": "jafar@gmail.com",
    "password": "1234",
    "firstName": "Jafar",
    "lastName": "Darwis"
}
```

Result:
```markdown
HTTP STATUS 200

{
    "username": "jafar@gmail.com",
    "firstName": "Jafar",
    "lastName": "Darwis"
}
```

To retrieve existing User:

```markdown
GET HTTP://localhost:8080/users/jafar@gmail.com
```

Result:

```markdown
HTTP STATUS 200

{
    "username": "jafar@gmail.com",
    "firstName": "Jafar",
    "lastName": "Darwis"
}
```

If when GET a User but data do not exist:

```markdown
HTTP STATUS 404

{
    "message": "Resource Not Found."
}
```


more will come...
        
