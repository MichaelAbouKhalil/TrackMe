cd ..
cd discovery-service
mvn install -DskipTests
docker build -t mikelak/discovery-service:0.0.1-SNAPSHOT .
docker image prune -f