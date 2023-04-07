### Ecommerce

# Swagger

http://localhost:8080/swagger-ui/index.html

App to use to manage Ecommerce

# database

docker run -d -p 27017:27017 --name ecommerce-mongo mongo:latest

Run with docker:
cd docker-entrypoint-initdb.d
docker compose up

with docker up the application with active profile "docker" and has mongo with dates saved

Run local

to run in local run Application with active profile "standalone" or vm parameter:

-Dspring.profiles.active=standalone

Main functionalities are:

- Information of car park location, services and available products
- Car park final Client Management
- eCommerce for short and long stay products
- Base platform for White-label client platforms

* Execute smtp server with docker:

docker run -t -i -p 3030:3025 --name greenmail greenmail/standalone:1.6.11

docker run -t -i -p 8000:80 --name roundcube roundcube/roundcubemail:latest

* Or

cd docker-greenmail

docker-compose up

you can see the mailbox login in: http://localhost:8000

user: email you want check

password: test

* execute whole app with docker without cache:

goto docker compose folder and execute:

docker-compose up --build --force-recreate --no-deps

Use prometheus (inside biller project):

`docker run -p 9090:9090 -v ./prometheus/:/etc/prometheus prom/prometheus`

Use grafana (credentials admin/admin):

`docker run --name grafana -d -p 3001:3000 grafana/grafana`

Use prometheus and grafana with docker compose:

`cd docker-compose-monitoring`

`docker-compose up`
