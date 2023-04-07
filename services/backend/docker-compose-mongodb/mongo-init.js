print('*************************************************************************')
db.createUser(
    {
        user: "portal_usr",
        pwd: "portal_pwd",
        roles: [
            {
                role: "readWrite",
                db: "ecommerce"
            }
        ]
    }
);

db = new Mongo().getDB("ecommerce");

db.createCollection('administrator', { capped: false });
db.administrator.insert([
    {
        "_id": ObjectId("635105584fd721458645343a"),
        "active": true,
        "login": "ricardo",
        "password": "PR+zRt45rXA24IraD659zQVYw3NgKE7wiIcgp9v8rps=",
        "role": "ADMIN",
        "email": "ricardo@gmail.com"
    },
]);