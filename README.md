Postman Collection all the endpoint

{
	"info": {
		"_postman_id": "0ead7a33-0656-4fc2-bc17-7cf122309839",
		"name": "small-banking",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "15916013",
		"_collection_link": "https://cloudy-moon-1051.postman.co/workspace/Integrators~6fa26a1f-8284-49fe-8719-dba3952f6055/collection/15916013-0ead7a33-0656-4fc2-bc17-7cf122309839?action=share&source=collection_link&creator=15916013"
	},
	"item": [
		{
			"name": "payment",
			"item": [
				{
					"name": "topup",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMiIsImlhdCI6MTcwNjUxMTM1MCwiZXhwIjoxNzA2NTEzMTUwfQ.R89clpPmEU5bMRz8KU3kHlEDAwxhhwN8IBNggJHMqEssTDCZFr4Dh3cN5j0Ou0tp_ryfAMMiEhl23fuCWZE67w",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							},
							{
								"key": "Accept-Language",
								"value": "en",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"amount\": 10,\n    \"source\": \"213487-312904-312412\",\n    \"paymentType\": \"TOP_UP\"\n}"
						},
						"url": {
							"raw": "http://127.0.0.1:8080/payments/process",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"payments",
								"process"
							]
						}
					},
					"response": []
				},
				{
					"name": "purchase",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2NTIzNjQwLCJleHAiOjE3MDY1MjU0NDB9.G1PYpYAebKjXt6n9rCaP_dtTwHHpG8m1Sg9tjRDB76Im2ggbwfCzswHQn9sUZah0vgCFZ_qz56MUU4vyvBjCLA",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							},
							{
								"key": "Accept-Language",
								"value": "en",
								"type": "text",
								"disabled": true
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"amount\": 90,\n    \"destination\": \"213487-312904-312412\",\n    \"paymentType\": \"PURCHASE\"\n}"
						},
						"url": {
							"raw": "http://127.0.0.1:8080/payments/process",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"payments",
								"process"
							]
						}
					},
					"response": []
				},
				{
					"name": "refund",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMiIsImlhdCI6MTcwNjUxMTM1MCwiZXhwIjoxNzA2NTEzMTUwfQ.R89clpPmEU5bMRz8KU3kHlEDAwxhhwN8IBNggJHMqEssTDCZFr4Dh3cN5j0Ou0tp_ryfAMMiEhl23fuCWZE67w",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"amount\": 20,\n    \"originalPaymentId\" : 9,\n    \"paymentType\": \"REFUND\"\n}"
						},
						"url": {
							"raw": "http://127.0.0.1:8080/payments/process",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"payments",
								"process"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "login-registration",
			"item": [
				{
					"name": "register",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"name\": \"ferid\",\n    \"surname\": \"quluzade\",\n    \"password\": \"ferid1234Q\",\n    \"gsmNumber\" : \"994516060646\"\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://127.0.0.1:8080/auth/register",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"auth",
								"register"
							]
						}
					},
					"response": []
				},
				{
					"name": "login",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"content-type": true
						}
					},
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"password\": \"ferid1234Q\",\n    \"gsmNumber\": \"994516060646\"\n}"
						},
						"url": {
							"raw": "http://127.0.0.1:8080/auth/login",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"auth",
								"login"
							]
						}
					},
					"response": []
				},
				{
					"name": "verify",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"content-type": true
						}
					},
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2NTIzOTY5LCJleHAiOjE3MDY1MjU3Njl9.hcof6CiOVdX-8txPqMAB9gYDAfzkDeal7UWDt0H2Yc2boqDPaResw9A6xEO_0lqpHwG4EgUV3dokIJaVQGm0VQ",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"code\" : \"786946\"\n}"
						},
						"url": {
							"raw": "http://127.0.0.1:8080/customers/verify",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"customers",
								"verify"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "otp",
			"item": [
				{
					"name": "send otp",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"content-type": true
						}
					},
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNzA2NDMxMjQwLCJleHAiOjE3MDY0MzMwNDB9.82zYWsAhFWlngwnG6xVDq2BNJoeSRqTrYQjtU1dmwDerG0QvIZIuIfa6dyKyCIuh0Q6RFB_2tku11cARMOMU0w",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n\n}"
						},
						"url": {
							"raw": "http://127.0.0.1:8080/otp/send",
							"protocol": "http",
							"host": [
								"127",
								"0",
								"0",
								"1"
							],
							"port": "8080",
							"path": [
								"otp",
								"send"
							]
						}
					},
					"response": []
				}
			]
		}
	]
}
