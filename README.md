# Interop-dfsp-directory

This project provides an API gateway to the IST Directory Naming Service.  By submitting an URI  with a unique identifier (i.e., "userURI": "http://centraldirectory.com/griffin") to the IST Directory Naming Service, the user will receive back a response with the URI to the recieveing DSFP as well as the account holder's name and default currency the transaction will be conducted in.

## As an interop-dsfp-directory API Consumer (to be refactored):
### Example
#### Resource Request: http://127.0.0.1:8083/directory/v1/user/get
Body:
```js
{
  "jsonrpc": "2.0",
  "id": "45567",
  "method": "directory.user.get",
  "params": {
    "userURI": "http://centraldirectory.com/griffin"
}
```
#### Resource 200 Response: 
Body:
```js
{
  "result": {
    "name": "Chris Griffin",
    "account": "http://receivingdfsp.com/griffin_12345",
    "currency": "USD"
  },
  "id": "",
  "jsonrpc": "2.0"
}
```
#### Resource 500 Response
Body:
```js
{
  "jsonrpc": "2.0",
  "id": "sampleid",
  "error": {
    "type": "parsingerror",
    "code": 400,
    "errorPrint": "The request could not be read by our software.",
    "message": "Parsing error"
  },
  "debug": {
    "cause": {
      "error": {
        "code": 400,
        "message": "An application generated message related to the error",
        "errorPrint": "This is the exception message from the top level exception",
        "type": "parsingerror"
      }
    },
    "stackInfo": [
      "Callnumber1",
      "Callnumber2"
    ]
  }
}
```
The add action is not part of the project's functional requirements.  It is a facility to allow sample data to be dynamically entered in order to test the /user/get action.

#### Resource Request: http://127.0.0.1:8083/directory/v1/user/add
Body:
```js
{
  "users": [
    {
      "uri": "http://centraldirectory.com/griffin",
      "name": "Chris Griffin",
      "account": "http://receivingdfsp.com/griffin_12345",
      "currency": "USD"
    },
    {
      "uri": "http://centraldirectory.com/magoo",
      "name": "Mr. Magoo",
      "account": "http://receivingdfsp.com/magoo_12345",
      "currency": "USD"
    },
    {
      "uri": "http://centraldirectory.com/yosemite",
      "name": "Yosemite Sam",
      "account": "http://receivingdfsp.com/yosemite_12345",
      "currency": "INR"
    },
    {
      "uri": "http://centraldirectory.com/mitty",
      "name": "Walter Mitty",
      "account": "http://receivingdfsp.com/mitty_12345",
      "currency": "ARS"
    }
  ]
}
```
#### Resource 200 Response: 
Body:
```js
{
  "result": {
    "message": "Updated 4 entities based on request"
  },
  "id": "3514adb5-fe14-4e40-9532-a1363b56cdc5",
  "jsonrpc": "2.0"
}
```
#### Resource 500 Response
Body:
```js
{
  "jsonrpc": "2.0",
  "id": "sampleid",
  "error": {
    "type": "parsingerror",
    "code": 400,
    "errorPrint": "The request could not be read by our software.",
    "message": "Parsing error"
  },
  "debug": {
    "cause": {
      "error": {
        "code": 400,
        "message": "An application generated message related to the error",
        "errorPrint": "This is the exception message from the top level exception",
        "type": "parsingerror"
      }
    },
    "stackInfo": [
      "Callnumber1",
      "Callnumber2"
    ]
  }
}
```

## As a Mule Developer

### Installation and Setup

#### Anypoint Studio
* [https://www.mulesoft.com/platform/studio](https://www.mulesoft.com/platform/studio)
* Clone https://github.com/LevelOneProject/interop-dfsp-directory.git to local Git repository
* Import into Studio as a Maven-based Mule Project with pom.xml
* Go to Run -> Run As Configurations.  Make sure interop-dfsp-directory project is highlighted.  Go to (x)=Arguments tab and make sure that -DMULE_ENV=dev is set in the VM Arguments.
#### Standalone Mule ESB
* [https://developer.mulesoft.com/download-mule-esb-runtime](https://developer.mulesoft.com/download-mule-esb-runtime)
* Add the environment variable you are testing in (dev, prod, qa, etc).  Open <Mule Installation Directory>/conf/wrapper.conf and find the GC Settings section.  Here there will be a series of wrapper.java.additional.(n) properties.  create a new one after the last one where n=x (typically 14) and assign it the next number (i.e., wrapper.java.additional.15) and assign -DMULE_ENV=dev as its value (wrapper.java.additional.15=-DMULE_ENV=dev)
* Download the zipped project from Git
* Copy zipped file (Mule Archived Project) to <Mule Installation Directory>/apps
### Run Application
#### Anypoint Studio
* Run As Mule Application with Maven
#### Standalone Mule ESB
* CD to <Mule Installation Directory>/bin -> in terminal type ./mule
### Test Application
#### Anypoint Studio
* Run Unit Tests
* Test API with Anypoint Studio in APIKit Console
* Verify Responses in Studio Console output

#### Standalone Mule ESB
* Review Server Logs for Unit Test results
* Test API with Browser at [http://localhost:8081/console](http://localhost:8083/directory/v1/console/)
* Test API with Postman at [http://localhost:8081/](http://localhost:8083/directory/v1/user/get)
  * **Note**: make sure you have set the content type to application/json 
* Verify Responses in Server Logs <Mule Installation Directory>/logs/*.log
