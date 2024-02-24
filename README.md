Redis:
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest

Google OAuth client needs to be configured
Create a new project and create OAuth client credentails
Chose external user access during creation

1. Configure consent screen: what the user sees while giving consent 
2. Create OAuth client ID: used to identify your application to Google Server
	- set authorized redirect URI to POSTMAN
3. Use google client library or make request to token info url to verify token

	
