export const TEST_USER_LIST = [
  {
    id: "635105584fd721458645343a",
    login: "ricardo",
    password: null,
    superAdmin: true,
    email: "ricarod@gmail.com",
    lastLogin: "2023-02-07T15:07:57.708Z",
  },
];

export const MOCK_COUPONS_LIST = [
    {
        "id": "63ff1a3d8804464530399f6e",
        "name": "complete",
        "code": "code_complete",
        "active": true,
        "activeFrom": "2023-03-01 09:16:00",
        "activeTo": "2023-03-01 09:16:00",
        "discount": 12,
        "discountType": "AMOUNT",
        "dayOfWeek": {
            "monday": true,
            "tuesday": true,
            "wednesday": true,
            "thursday": true,
            "friday": true,
            "saturday": false,
            "sunday": false
        },
        "shortDescription": "test",
        "locationProducts": [
            {
                "idLocation": "63d3b2c94f1aa6072f8da001",
                "idProducts": [
                    "63ece1730ae06559175ce00f",
                    "63e0d0a672dd4046ddbfd2fb"
                ],
                "allProducts": null
            },
            {
                "idLocation": "63fe2824a327c3570001f93b",
                "idProducts": [],
                "allProducts": true
            }
        ]
    },
    {
        "id": "63ff212f4b835903257e969f",
        "name": "complete2",
        "code": "complete2",
        "active": true,
        "activeFrom": "2023-03-01 09:16:00",
        "activeTo": "2023-03-01 09:16:00",
        "discount": 12,
        "discountType": "AMOUNT",
        "dayOfWeek": {
            "monday": true,
            "tuesday": true,
            "wednesday": true,
            "thursday": true,
            "friday": true,
            "saturday": false,
            "sunday": false
        },
        "shortDescription": "12",
        "locationProducts": [
            {
                "idLocation": "63fe26aba327c3570001f93a",
                "idProducts": [],
                "allProducts": null
            },
            {
                "idLocation": "63d3b2c94f1aa6072f8da001",
                "idProducts": [
                    "63e0d0a672dd4046ddbfd2fb",
                    "63e11fa1f7a98e364c3cd9e9"
                ],
                "allProducts": null
            }
        ]
    }
];