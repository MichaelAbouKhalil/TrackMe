cd ..
cd centralized-config
mvn install -DskipTests
docker build -t mikelak/centralized-config:0.0.1-SNAPSHOT .
docker image prune -f