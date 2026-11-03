FROM amazoncorretto:21-alpine-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

FROM build AS runner

COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/configuration/ configuration/

EXPOSE 7000
EXPOSE 5005

CMD ["java", "-jar", "app.jar"]