# file-search-service
## Server file search service

## Deployment
1. change **root** Directory available for searching files in **application.properties**
2. change user login/password in SecurityConfig (by default it's user/password)
2. mvn clean install
3. deploy war to tomcat
4. start tomcat
      
## Documentation
When application is deployed to server you can see documented REST API via Swagger UI from link
http://[your-domain]/swagger-ui.html

## Test
 Write a rest web service with the following APIs:
- given a name of a directory the service returns a list of all of the subdirectories with paths and files counts;
- given a path to a particular directory the service returns a list of files in that directory;
- given a path to a particular file name the service returns all of the standard file attributes for that file;
Write the unit tests where applicable.
Write the API documentation
Write the deployment instructions

## Author
Vasyl Pryimak
