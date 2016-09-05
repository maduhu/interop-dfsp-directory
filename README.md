# Interop-dfsp-directory

> This project provides an API gateway to the IST Directory Naming Service.  By submitting an URI  with a unique identifier (i.e., "userURI": "http://centraldirectory.com/griffin") to the IST Directory Naming Service, the user will receive back a response with the URI to the recieveing DSFP as well as the account holder's name and default currency the transaction will be conducted in.

## As an interop-dsfp-directory API Consumer (to be refactored):

### Example
#### Resource Request: /user/get
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
#### Resource Response: 
```js
{
  "jsonrpc": "2.0",
  "id": "12345",
  "result": {
    "name": "Chris Griffin",
    "account": "http://receivingdfsp.com/griffin_12345",
    "currency": "USD"
  }
}
```


As a Mule Developer
Installation and Setup
Anypoint Studio
# GENERATED CONTENT  Mule Application Deployment Descriptor #Mon Aug 29 17:33:24 PDT 2016 redeployment.enabled=true encoding=UTF-8 domain=default config.resources=spsp-client-proxy-api.xml,mock-spsp-client-proxy-api.xml
-DMULE_ENV=dev
Standalone Mule ESB
Run Application
Test Application
Anypoint Studio
Run Unit Tests
Test API with Anypoint Studio
Verify Responses in Studio Console output
Standalone Mule ESB 
Run Application
Review Server Logs for Unit Tests
Test API with Browser at http://localhost:8081/console
Test API with Postman at http://localhost:8081/
Verify Responses in Server Logs
