FROM openjdk:13
COPY target/*.jar /lblms.jar
CMD /usr/bin/java -jar /lblms.jar