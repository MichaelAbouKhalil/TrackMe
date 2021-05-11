cd ../models
mvn install -DskipTests
cd ../common-utils
mvn install -DskipTests
cd ../auth-service
mvn install -DskipTests
docker build -t mikelak/auth-service:0.0.1-SNAPSHOT .
docker image prune -f