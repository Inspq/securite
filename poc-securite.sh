PROJECT_DIR=$(pwd)
export AGENT_PWD=$1
export FQDN=$(hostname -f)
#export FQDN=$(ifconfig docker0 | grep "inet adr" | awk -F: '{print $2}' | awk '{print $1}')

# Faire le menage des installations precedentes
docker-compose stop
docker-compose rm
#docker image inspect sx5dotnetui && docker rmi sx5dotnetui || echo "Image sx5dotnetui non existante"
#docker image inspect sx5dotnetrest && docker rmi sx5dotnetrest || echo "Image sx5dotnetrest non existante"
#docker image inspect sx5javaui && docker rmi sx5javaui || echo "Image sx5javaui non existante"
#docker image inspect sx5javarest && docker rmi sx5javarest || echo "Image sx5javarest non existante"
docker network inspect poc.inspq.qc.ca && echo "Le reseau docker poc.inspq.qc.ca existe deja" || docker network create poc.inspq.qc.ca

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

docker-compose build
docker-compose create
docker-compose start openam

echo Le serveur OpenAM est en cours de demarrage...
echo Une fois le serveur demarre et configuré pour les agents, faire docker-compose start
echo pour demarrer les autres composants
