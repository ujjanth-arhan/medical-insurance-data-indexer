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


