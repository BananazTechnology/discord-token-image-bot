[![SOFT](https://github.com/BananazTechnology/discord-token-image-bot/actions/workflows/SOFT.yml/badge.svg?branch=develop)](https://github.com/BananazTechnology/discord-token-image-bot/actions/workflows/SOFT.yml)[![RELEASE](https://github.com/BananazTechnology/discord-token-image-bot/actions/workflows/RELEASE.yml/badge.svg?branch=main)](https://github.com/BananazTechnology/discord-token-image-bot/actions/workflows/RELEASE.yml)

# Discord Token Image Bot
* Description: A Discord bot made with @Javacord!
* Version: (Check main for release or develop for dev)
* Creator: Aaron Renner
* THIS BRANCH IS ONLY FOR TOKEN IMAGES ONLY!

### Table of Contents
* [Introduction](#introduction)
* Setup *"How to"*
  * [Run Spring-Boot](#running-the-project)
* Help
  * [Setup Libraries and Examples](#libraries)
  
## Introduction

This Java application is built on the Spring-Boot framework! This project interacts with Discord commands or startup objects in the application.yml to play games in Discord channels, see *How-TO* below for more details.

* THIS BRANCH IS ONLY FOR TOKEN IMAGES ONLY!

## Setup
### Properties
The following document formatting MUST REMAIN THE SAME, replace or add only where noted to!
Tips:
* * THIS BRANCH IS ONLY FOR RANDOM IMAGES ONLY!
* The position of the `discord` below `nft-bot` is essential!

``` yaml
nft-bot:
  custom:
    - baseUrl: <The base URL>
      max: <A max bounds number>
      fileFormat: <Blanket image file type>
      command: <Command to match against>
  discord:
    prefix: <Command prefix for all (Ex. "!")>
    token: <DISCORD-TOKEN>
    channelId: <CHANNEL ID>

(*) = is optional. DOES NOT NEED TO BE INCLUDED
```

### Running PROD
Setup the `SPRING_APPLICATION_JSON` value in the Docker-Compose. See example docker-compose.yaml in this project.

### Running the Project

Executing the project can be done in two ways, the first is by initializing using Maven which the second produces a traditional Jar file. Before attempting to run the program some setup must be done inside of the [src/main/resources/application.properties](src/main/resources/application.yml), you can follow the guides.

### Build with Maven

If you have Maven installed on your machine you can navigate to the root project directory with this README file and execute the following. Remember to follow the above Database setup procedures first.
```sh
mvn -B -DskipTests clean package
```
You can also use the built in Maven wrapper and execute the project by following this command.
```sh
./mvnw -B -DskipTests clean package
```
### Setting up in IDE

Download Lombok to your IDE or VS Code Extension!

Use the IDE "Run Configuration" to setup the `-Dspring.application.json` (eclipse example) in the Environment Properties

### Creating a Docker Image

To build a container that can execute the application from a safe location you can use my supplied [Dockerfile](Dockerfile) to do so. You should follow the guides first to better understand some of these arguments.

```Dockerfile
CMD [ "java", \
        "-jar", \
        "discord-nft-bot.jar"]
```