Redis:
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest

Google OAuth client needs to be configured
Create a new project and create OAuth client credentails
Chose external user access during creation

1. Configure consent screen: what the user sees while giving consent 
2. Create OAuth client ID: used to identify your application to Google Server
	- set authorized redirect URI to POSTMAN
3. Use google client library or make request to token info url to verify token
4. Docker images for Redis and RabbitMQ 
5. Update RabbitMQ to handle publisher confirms to ensure that the data is actually processed by the server before and is persistent in the even the queue dies
6. Use dependency injection where needed
7. Use appropriate logs	
8. Add support for flag based removal of auth
9. Check all the flows
10. Write test cases
11. Handle bug related to @Around annotation
12. docker run -d --hostname my-rabbit --name some-rabbit rabbitmq:3
13. docker run -p 5672:5672 -d --hostname my-rabbit --name some-rabbit rabbitmq:3

https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html
14. docker network create elastic
15. docker pull docker.elastic.co/elasticsearch/elasticsearch:8.12.2
or try thsi version 7.17.15
16. docker run --name es01 --net elastic -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -t docker.elastic.co/elasticsearch/elasticsearch:8.12.2
17. export ELASTIC_PASSWORD="your_password"
18. docker cp es01:/usr/share/elasticsearch/config/certs/http_ca.crt .
19. curl --cacert http_ca.crt -u elastic:$ELASTIC_PASSWORD https://localhost:9200

docker pull docker.elastic.co/kibana/kibana:8.12.2
docker run --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.12.2
kibana 7.17.15

When copying the enrollment token, make sure there are spaces at the start and end like how the token is generated. If the space is removed, the token is not accepted
docker compose file that is used in the documentation of elastic uses too much RAM hence we setup the minimum amount needed to get this up and running

20. Add support for swagger?
21. Structure of subprojects is taken from gradle at https://docs.gradle.org/current/userguide/multi_project_builds.html
