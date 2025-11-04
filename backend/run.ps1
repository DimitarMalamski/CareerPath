$env:SPRING_PROFILES_ACTIVE="local"
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5433/careerpath_local"
$env:SPRING_DATASOURCE_USERNAME="cp_user"
$env:SPRING_DATASOURCE_PASSWORD="cp_pass"

./gradlew bootRun