# Allows you to run this app easily as a docker container.
# See README.md for more details.
#
# 1. Build the image with: docker build --no-cache -t test/my-hilla-app:latest .
# 2. Run the image with: docker run --rm -ti -p8080:8080 test/my-hilla-app
#
# Uses Docker Multi-stage builds: https://docs.docker.com/build/building/multi-stage/

# The "Build" stage. Copies the entire project into the container, into the /app/ folder, and builds it.
FROM eclipse-temurin:17 AS BUILD
COPY . /app/
WORKDIR /app/
RUN ./mvnw clean test package -Pproduction
# At this point, we have the app (executable jar file):  /app/target/my-hilla-app-1.0-SNAPSHOT.jar

# The "Run" stage. Start with a clean image, and copy over just the app itself, omitting gradle, npm and any intermediate build files.
FROM eclipse-temurin:17
COPY --from=BUILD /app/sortable-layout-demo/target/sortable-layout-demo-2.0.2.jar /app/sortable-layout-demo.jar
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT java -jar sortable-layout-demo.jar 8080