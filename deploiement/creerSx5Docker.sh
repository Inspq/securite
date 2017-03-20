#!/bin/bash
FQDN=`hostname -f`

create ()
{
	docker create --name sx5javarest -p 8888:8080 -i elfelip/sx5javarest
	docker create --name sx5javasoap -p 8889:8080 -i elfelip/sx5javasoap
	docker create --name sx5dotnetrest -p 8890:50633 -i elfelip/sx5dotnetrest
	docker create --name sx5javaui --env JAVA_REST_BASE_URL=http://$FQDN:8888 --env JAVA_SOAP_BASE_URL=http://$FQDN:8889 --env DOTNET_REST_BASE_URL=http://$FQDN:8890 --env AGENT_PWD=MotDePasse --env OPENAM_URL=http://$FQDN:8893/openam --env AGENT_URL=http://$FQDN:8891/agentapp --env AGENT_PROFILE_NAME=sx5javaui -p 8891:8080 -i elfelip/sx5javaui
	docker create --name sx5dotnetui --env JAVA_REST_BASE_URL=http://$FQDN:8888 --env JAVA_SOAP_BASE_URL=http://$FQDN:8889 --env DOTNET_REST_BASE_URL=http://$FQDN:8890 --env AGENT_PWD=MotDePasse --env OPENAM_URL=http://$FQDN:8893/openam --env AGENT_URL=http://$FQDN:8892/agentapp --env AGENT_PROFILE_NAME=sx5dotnetui -p 8892:8892 -i elfelip/sx5dotnetui
	docker create --name openam -p 8893:8080 -v /opt/openam/root:/root -i elfelip/openam
	docker create --name openig -p 8894:8080 --env OPENAM_URL=http://$FQDN:8893/openam --env POLICY_ADMIN=amAdmin --env POLICY_ADMIN_PWD=Pan0rama elfelip/openig
}

start ()
{
	docker start sx5dotnetrest
	docker start sx5javasoap
	docker start sx5javarest
	docker start sx5javaui
	docker start sx5dotnetui
}
stop ()
{
	docker stop sx5dotnetui
	docker stop sx5javaui
	docker stop sx5dotnetrest
	docker stop sx5javasoap
	docker stop sx5javarest
}

delete ()
{
	stop
	docker rm sx5javarest
	docker rm sx5javasoap
	docker rm sx5dotnetrest
	docker rm sx5javaui
	docker rm sx5dotnetui
}

if [ -z $1 ]; then
	echo "usage $0 [stop|start|create|delete]"
	exit 0
fi

case "$1" in
	stop) stop ;;
	start) start ;;
	create) create ;;
	delete) delete ;;
esac