# lebah-rest-jetty

This is my approach of RESTful framework.  This makes sense because I want something lightweight and straightforward without the heavy abstractions of Spring Boot. I want full control over the framework without dealing with excessive configurations or unnecessary features of Spring Boot.

## Why Not Spring Boot?

### Too Much Magic
Spring Boot relies heavily on conventions, annotations, and auto-configuration. While this makes development faster, it can be confusing when something doesn’t work as expected. Debugging can be difficult because a lot happens behind the scenes.

### Steep Learning Curve
To fully understand Spring Boot, developers often need to grasp Spring Core concepts like dependency injection (DI), inversion of control (IoC), and AOP (aspect-oriented programming). This can be overwhelming for beginners.

### Heavy and Opinionated
Spring Boot comes with many pre-configured dependencies and features, which can lead to unnecessary overhead for small or simple applications. If a developer doesn’t want all the built-in features, they must learn how to disable or override them.

### Complex Dependency Management
Spring Boot heavily depends on Spring’s ecosystem and the BOM (Bill of Materials) approach. While this simplifies dependency management, it can sometimes introduce version conflicts or unexpected dependency resolutions.

### Startup Time and Resource Usage
Compared to lightweight frameworks, Spring Boot applications tend to have longer startup times and consume more memory due to extensive class scanning and dependency injection.


