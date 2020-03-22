# web-dev-2020 - LB Library Management Software

Library Management Software developed by three third year UCD Comp Sci Students

## To Run:

#### With H2 database
Ensure H2 properties are active in src/main/resources/application.properties

**mvn clean install spring-boot:repackage**

**mvn spring-boot:run**

#### With MySQL/Docker
Ensure MySQL properties are active in src/main/resources/application.properties

**mvn clean install spring-boot:repackage**

**docker-compose build**

**docker-compose up**
