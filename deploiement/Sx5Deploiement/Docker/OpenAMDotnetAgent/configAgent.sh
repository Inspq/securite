#!/bin/bash
# Script d'installation de l'agent web d'OpenAM
# Arguments: 
#	virtualhost name: Nom a donner a la configuration de l'hôte virtuel
#	dll: Fichier dll dotnet core a exécuter

set -e
VIRTUAL_HOST_NAME=$1
DOTNET_DLL=$2
KESTREL_PORT=$3
AGENT_PROTO=`echo $AGENT_URL | awk -F: '{print $1}'`
AGENT_HOSTNAME=`echo $AGENT_URL | awk -F '[/:]' '{print $4}'`
AGENT_PORT=`echo $AGENT_URL | awk -F '[/:]' '{print $5}'`
AGENT_URI=`echo $AGENT_URL | awk -F/ '{print $4}'`

if [ ! -e /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf ]; then
    echo "<VirtualHost *:$AGENT_PORT>" > /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ServerName $AGENT_HOSTNAME" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ProxyRequests Off" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf 
    echo "    <Proxy *>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "            Order deny,allow" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "            Allow from all" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    </Proxy>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
echo "    ProxyPass / http://${HOSTNAME}:${KESTREL_PORT}/" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ProxyPassReverse / http://${HOSTNAME}:${KESTREL_PORT}/" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "</VirtualHost>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf

    sed -i s'/Listen 80/Listen '"${AGENT_PORT}"'/g' /etc/httpd/conf/httpd.conf
fi

backupfiles=(/etc/httpd/conf/httpd.conf_amagent*)
if [ ! -e "${backupfiles[0]}" ]; then
	echo $AGENT_PWD > /tmp/passwd.txt
	chmod 400 /tmp/passwd.txt

    # Attendre que le serveur OpenAM soit disponible
    sleep 30
	curl -f -m 60 --retry 5 --retry-delay 30 $OPENAM_URL

    /opt/web_agents/apache24_agent/bin/agentadmin --s "/etc/httpd/conf/httpd.conf" \
                "$OPENAM_URL" "$AGENT_URL" "/" "$AGENT_PROFILE_NAME" \
                "/tmp/passwd.txt" --acceptLicence --changeOwner
fi
apachectl
dotnet exec ${DOTNET_DLL}