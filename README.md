## Tiny Weather Bulletin

# Description
Tiny weather bulletin could be used to check the weather conditions in the interested city for 2 next days from request moment.
There are 3 types of forecasts:
1. Forecast during working hours (managed by application)
2. Forecast outside working hours (depending on during hours)
3. Entire forecast   

## Initial Configuration
1.	Apache Maven (http://maven.apache.org)  All of the code examples in this book have been compiled with Java version 11.
2.	Git Client (http://git-scm.com)
3.  Docker(https://www.docker.com/products/docker-desktop)

**Important:** In this chapter we will be introducing a new service called Organization Service. You can donwload the organization and licensing service from the initial folder. That will contain all the source code required to start chapter 6.

## How To Use

To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). From your command line:

```bash
# Clone this repository
$ git clone https://github.com/iraievskyi/forecast-demo

# To build the code as a docker image, open a command-line 
# window and execute the following command:
$ mvn clean package dockerfile:build

# Now we are going to use docker-compose to start the actual image.  To start the docker image, stay in the directory containing  your chapter 6 source code and  Run the following command: 
$ docker-compose -f twb/docker-compose.yml up
```

# The build command

Will execute the [Spotify dockerfile plugin](https://github.com/spotify/dockerfile-maven) defined in the pom.xml file.  

Running the above command at the root of the project directory will build all of the projects.  If everything builds successfully you should see a message indicating that the build was successful.

# The Run command

This command will run our services using the docker-compose.yml file located in the /twb directory. 

If everything starts correctly you should see a bunch of Spring Boot information fly by on standard out.  At this point all of the services will be running.

## Api documentation
To check Api documentation use [Tiny Weather Bulletin](http://localhost:8080/swagger-ui.html)