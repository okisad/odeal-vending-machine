# REST API vending machine application

http://localhost:8080/swagger-ui.html

The REST API examples of app

For database you can visit this path

http://localhost:8080/h2-console

username : oktay
pass : 1234

## Get Inventory Info

### Request

`GET /inventories/{inventoryId}`

    curl -i -H 'Accept: application/json' http://localhost:8080/inventories/1

### Response

    HTTP/1.1 200 OK
    Date: Thu, 24 Feb 2011 12:36:30 GMT
    Status: 200 OK
    Connection: close
    Content-Type: application/json
    {
    "id": 1,
    "totalProductAmount": 90,
    "products": [
        {
            "id": 29,
            "name": "zKuNd",
            "amount": 3,
            "price": 1.5,
            "productType": "FOOD"
        },
        {
            "id": 5,
            "name": "wpMNw",
            "amount": 3,
            "price": 1.8,
            "productType": "BEVERAGE_COLD"
        },
        {
            "id": 3,
            "name": "iwubp",
            "amount": 3,
            "price": 1.3,
            "productType": "BEVERAGE_COLD"
        },
        {
            "id": 23,
            "name": "sjJsx",
            "amount": 3,
            "price": 2.2,
            "productType": "FOOD"
        },
        {
            "id": 17,
            "name": "HTmzQ",
            "amount": 3,
            "price": 1.05,
            "productType": "FOOD"
        },
        {
            "id": 2,
            "name": "JsfKx",
            "amount": 3,
            "price": 2.35,
            "productType": "BEVERAGE_COLD"
        },
        {
            "id": 18,
            "name": "BZkxe",
            "amount": 3,
            "price": 1.3,
            "productType": "FOOD"
        },
        {
            "id": 14,
            "name": "tcxVi",
            "amount": 3,
            "price": 1.25,
            "productType": "FOOD"
        },
        {
            "id": 12,
            "name": "xqcCL",
            "amount": 3,
            "price": 3.7,
            "productType": "FOOD"
        },
        {
            "id": 16,
            "name": "QKlDZ",
            "amount": 3,
            "price": 4.05,
            "productType": "FOOD"
        },
        {
            "id": 8,
            "name": "FAnZx",
            "amount": 3,
            "price": 4.3,
            "productType": "BEVERAGE_HOT"
        },
        {
            "id": 21,
            "name": "PqHfB",
            "amount": 3,
            "price": 4.7,
            "productType": "FOOD"
        },
        {
            "id": 9,
            "name": "EesGv",
            "amount": 3,
            "price": 1.35,
            "productType": "BEVERAGE_HOT"
        },
        {
            "id": 25,
            "name": "JyzCA",
            "amount": 3,
            "price": 1.35,
            "productType": "FOOD"
        },
        {
            "id": 28,
            "name": "vPnyR",
            "amount": 3,
            "price": 1.45,
            "productType": "FOOD"
        },
        {
            "id": 7,
            "name": "HwOSb",
            "amount": 3,
            "price": 4.7,
            "productType": "BEVERAGE_HOT"
        },
        {
            "id": 15,
            "name": "ZQuok",
            "amount": 3,
            "price": 4.55,
            "productType": "FOOD"
        },
        {
            "id": 30,
            "name": "TWMYn",
            "amount": 3,
            "price": 5.0,
            "productType": "FOOD"
        },
        {
            "id": 20,
            "name": "oIPfr",
            "amount": 3,
            "price": 4.45,
            "productType": "FOOD"
        },
        {
            "id": 27,
            "name": "WPEbw",
            "amount": 3,
            "price": 2.4,
            "productType": "FOOD"
        },
        {
            "id": 10,
            "name": "HbFGi",
            "amount": 3,
            "price": 2.05,
            "productType": "BEVERAGE_HOT"
        },
        {
            "id": 26,
            "name": "ujAuc",
            "amount": 3,
            "price": 1.0,
            "productType": "FOOD"
        },
        {
            "id": 24,
            "name": "vSgMb",
            "amount": 3,
            "price": 3.4,
            "productType": "FOOD"
        },
        {
            "id": 22,
            "name": "etcag",
            "amount": 3,
            "price": 4.85,
            "productType": "FOOD"
        },
        {
            "id": 19,
            "name": "lNxOm",
            "amount": 3,
            "price": 4.2,
            "productType": "FOOD"
        },
        {
            "id": 13,
            "name": "PnCvf",
            "amount": 3,
            "price": 3.5,
            "productType": "FOOD"
        },
        {
            "id": 4,
            "name": "Nqkyh",
            "amount": 3,
            "price": 4.35,
            "productType": "BEVERAGE_COLD"
        },
        {
            "id": 1,
            "name": "odfwE",
            "amount": 3,
            "price": 1.45,
            "productType": "BEVERAGE_COLD"
        },
        {
            "id": 6,
            "name": "hsoov",
            "amount": 3,
            "price": 1.2,
            "productType": "BEVERAGE_HOT"
        },
        {
            "id": 11,
            "name": "qTYsD",
            "amount": 3,
            "price": 3.45,
            "productType": "FOOD"
        }
    ],
    "sugarCount": 500
    }

## Order Product

### Request

`POST /orders/inventories/1`

    curl -i -H 'Accept: application/json' -d '{"productAmount":1,"productId":12,"productSugarAmount":10,"paymentType":"CASH","loadedMoneyList":[{"value":1.0,"moneyType":"COIN","amount":4}]}' http://localhost:8080/orders/inventories/1
    
### Request Body Example With All Values
`
  {
	"productAmount":1,
	"productId":12,
	"productSugarAmount":10,
	"loadedMoneyList":[
		{
			"value":1.0,
			"moneyType":"COIN",
			"amount":4
		}
	],
	"paymentType":"CASH"
}
`

### Response

    HTTP/1.1 201 Created
    Date: Thu, 24 Feb 2011 12:36:30 GMT
    Status: 200 OK
    Connection: close
    Content-Type: application/json

    {
    "productName": "IDRcV",
    "productAmount": 1,
    "paymentType": "CASH",
    "totalPrice": 3.0,
    "change": 1.0,
    "changes": [
          {
            "value": 1.0,
            "moneyType": "COIN",
            "amount": 1
          }
        ]
    }
