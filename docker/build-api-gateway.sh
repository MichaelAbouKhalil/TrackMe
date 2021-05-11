cd ..
cd api-gateway
mvn install -DskipTests
docker build -t mikelak/api-gateway:0.0.1-SNAPSHOT .
docker image prune -f