#######################
##       BUILD       ##
#######################

# Use the maven base image with jdk for compile
FROM    docker.uclv.cu/maven:3.6.3-adoptopenjdk-11
ARG     JAR_FILE=target/*.jar
# Copy from builder stage the jar file
COPY    ${JAR_FILE} taller2M-1.0.jar
# Execute the jar with the entry point
ENTRYPOINT [ "java", "-jar", "taller2M-1.0.jar" ]