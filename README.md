# Requests Runner

A lightweight desktop HTTP client and batch request runner built with JavaFX. Designed for batch execution with template interpolation.

## Usage
1. Set the HTTP method and destination URL, or click 'Import cURL' to paste an existing command.
2. Setup headers and body in the respective tabs. Use placeholders with double curly brackets such as {{number}} inside the body if you plan to run batch requests with variables.
3. Paste values into the text field on the right panel.
4. Click 'Send'. The batch runner will execute requests sequentially and stream results into the UI.

## Installation
#### 1. Clone the repository:
```bash
   git clone https://github.com/hyperf0rm/requests-runner.git
   cd requests-runner
```
#### 2. Run the application
```powershell
.\mvnw clean package
```

## Building from source
#### 1. Building Fat JAR
```powershell
.\mvnw clean package
```

#### 2. Packaging application
```powershell
jpackage `
  --input target\ `
  --name Runner `
  --main-jar runner-1.0.jar `
  --main-class io.github.hyperf0rm.runner.Launcher `
  --type app-image `
  --dest build\ `
  --add-modules java.base,java.desktop,java.net.http,java.sql,jdk.jfr
```