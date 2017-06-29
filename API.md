# Central Directory Gateway API
***

This API is a simple pass through for [Central Directory API](https://github.com/LevelOneProject/central-directory/blob/master/API.md) implemented by Dwolla.
Most of the endpoints listed below make a proxy call to the underlying central-directory api.
The only exception is to lookup a resource by identifier. This is a composite endpoint that calls other apis
and return an aggregated response.
* `POST` [**Register a DFSP**](#register-a-dfsp)
* `GET` [**Get identifier types**](#get-identifier-types) 
* `POST` [**Register an identifier**](#register-an-identifier)
* `GET` [**Lookup resource by identifier**](#lookup-resource-by-identifier)
* `GET` [**Directory health check**](#directory-health)


***

## Endpoints

### **Register a DFSP**
Refer to Dwolla's documentation page for details. [Central Directory API](https://github.com/LevelOneProject/central-directory/blob/master/API.md)

### **Get identifier types**
Refer to Dwolla's documentation page for details. [Central Directory API](https://github.com/LevelOneProject/central-directory/blob/master/API.md)


### **Register an identifier**
Refer to Dwolla's documentation page for details. [Central Directory API](https://github.com/LevelOneProject/central-directory/blob/master/API.md)


### **Lookup resource by identifier**
This endpoint allows retrieval of a URI that will return customer information by supplying an identifier and identifier type. The implementation of this endpoint makes calls to Central Directory resource lookup, Scheme Adapter Get Receiver, Central Fraud Sharing User Fraud Score endpoint, aggregates the responses from all the calls and responds

#### HTTP Request
```GET http://central-directory-gateway/resources?identifier={identifierType:identifier}```

#### Authentication
| Type | Description |
| ---- | ----------- |
| HTTP Basic | The username and password are the key and secret of a registered DFSP, ex dfsp1:dfsp1 |

#### Query Params
| Field | Type | Description |
| ----- | ---- | ----------- |
| identifier | String | Valid identifier type and identifier separated with a colon |

#### Response 200 OK
| Field | Type | Description |
| ----- | ---- | ----------- |
| **directory_details** | **Array** |  |
| directory_details.name | String | The name of the created DFSP |
| directory_details.providerUrl | URI | A URI that can be called to get more information about the customer |
| directory_details.shortName | String | The shortName of the created DFSP|
| directory_details.preferred | String | Details if the DFSP is set as preferred, can either be true or false |
| directory_details.registered | String | Returns true if DFSP is registered for the identifier, false if defaulted |
| **dfsp_details** | **Object** | Object that holds Payee details |
| dfsp_details.type | String | Defaulted to "payee" |
| dfsp_details.name | String | Name of the payee |
| dfsp_details.account | String | Payee ledger account |
| dfsp_details.currencyCode | String | Defaulted to "USD" |
| dfsp_details.currencySymbol | String | Defaulted to "$" |
| dfsp_details.imageUrl | String | Url for the image |
| dfsp_details.paymentsUrl | String | Payments Url |
| **fraud_details** | **Object** | Object that holds Fraud score related details |
| fraud_details.score | Integer | The likelihood of fraud |
| fraud_details.created | String | (Optional)Time when score was created |
| fraud_details.id | String | (Optional)Resource identifier |


#### Request
```http
GET http://central-directory-gateway/resources?identifier=eur:1234 HTTP/1.1
```

#### Response
``` http
HTTP/1.1 200 OK
"directory_details": {
[
  {
    "name": "The First DFSP",
    "providerUrl": "http://dfsp/users/1",
    "shortName": "dsfp1",
    "preferred": "true",
    "registered": "true"
  },
  {
    "name": "The Second DFSP",
    "providerUrl": "http://dfsp/users/2",
    "shortName": "dsfp2",
    "preferred": "false",
    "registered": "false"
  }
]
},
"dfsp_details": {
    "type": "payee",
    "account": "ilpdemo.red.bob",
    "currencyCode": "USD",
    "currencySymbol": "$",
    "name": "Bob Dylan",
    "imageUrl": "https: //red.ilpdemo.org/api/receivers/bob/profile_pic.jpg"
},
"fraud_detaiils": {
    "score": 34
}
```

#### Errors (4xx)
| Field | Description |
| ----- | ----------- |
| NotFoundError | The requested resource could not be found. |
``` http
{
  "id": "NotFoundError",
  "message": "The requested resource could not be found."
}
```

