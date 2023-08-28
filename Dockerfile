#######################
##       BUILD       ##
#######################

# Use the maven base image with jdk for compile
FROM    docker.uclv.cu/adoptopenjdk:11-jre-hotspot
#ARG JAR_FILE=target/*.jar
# Copy from builder stage the jar file
#COPY${JAR_FILE} taller-2m
COPY "./target/tallerM2-0.0.1-SNAPSHOT.jar" "taller-2m.jar"
# Execute the jar with the entry point
ENTRYPOINT [ "java", "-jar", "taller-2m.jar" ]

#FROM docker.uclv.cu/openjdk:11.0-oracle
#COPY "./target/tallerM2-0.0.1-SNAPSHOT.jar" "taller-2m.jar"
#EXPOSE 8080
#ENTRYPOINT [ "java", "-jar", "taller-2m.jar" ]
