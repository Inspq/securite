PROJECT_DIR=$(pwd)
export AGENT_PWD=$1
#export FQDN=$(hostname -f)
export FQDN=$(ifconfig docker0 | grep "inet adr" | awk -F: '{print $2}' | awk '{print $1}')

docker-compose stop
docker-compose rm

cd $PROJECT_DIR/source/sx5-java-REST
mvn clean install

cd $PROJECT_DIR/source/sx5-java-UI
mvn clean install

cd $PROJECT_DIR/source_net/CoreTest/src/TestRestService
dotnet restore -r centos.7-x64
dotnet publish -r centos.7-x64 -c Release

cd $PROJECT_DIR/source_net/CoreTest/src/ServicesUI
dotnet restore -r centos.7-x64
dotnet publish -r centos.7-x64 -c Release


docker-compose create
docker-compose start
