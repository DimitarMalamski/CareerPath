# run.ps1 - Run Spring Boot app with local Postgres
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/careerpath"
$env:SPRING_DATASOURCE_USERNAME="cp_user"
$env:SPRING_DATASOURCE_PASSWORD="cp_pass"

./gradlew bootRun