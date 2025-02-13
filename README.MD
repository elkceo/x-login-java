# X (Twitter) Login Java App

This is a simple Java application that allows users to authenticate with X using OAuth.

Features

## Features
- **X (Twitter) authentication using OAuth**
- **Java Spring Boot backend**
- **Thymeleaf-based login page**

## Prerequisites
Before running the application, ensure you have the following:
-  Java **17 or higher** installed
-  **Maven** installed
-  **Twitter Developer account** with API keys

## Setup

### 1. Clone the repository

### 2. Configure API Keys
```properties
x.api.key=your_consumer_key
x.api.secret=your_consumer_secret
x.callback.url=https://yourdomain.com/callback
```

### 3. Build and Run
```sh
mvn clean install
mvn spring-boot:run
```
## License

This project is licensed under the MIT License. Feel free to use and modify it.

## Contributions

Contributions are welcome! Feel free to submit a pull request or report issues.
