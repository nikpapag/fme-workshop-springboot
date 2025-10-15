# Use a small Java 17 runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy your built jar from the local target folder into the image
# If your jar name is fixed, replace *.jar with the exact file name for faster builds
COPY target/*.jar /app/app.jar

# Optional envs (override at runtime)
ENV JAVA_OPTS=""
ENV SERVER_PORT=8080
ENV SPLIT_SDK_KEY=""

EXPOSE 8080

# Start the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --server.port=${SERVER_PORT}"]

