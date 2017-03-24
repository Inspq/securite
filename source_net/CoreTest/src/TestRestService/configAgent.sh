#!/bin/bash
# Script d'installation de l'agent web d'OpenAM

set -e
AGENT_PROTO=`echo $AGENT_URL | awk -F: '{print $1}'`
AGENT_HOSTNAME=`echo $AGENT_URL | awk -F '[/:]' '{print $4}'`
AGENT_PORT=`echo $AGENT_URL | awk -F '[/:]' '{print $5}'`
AGENT_URI=`echo $AGENT_URL | awk -F/ '{print $4}'`

if [ ! -e /etc/httpd/conf.d/sx5dotnetrest.conf ]; then
    echo "<VirtualHost *:$AGENT_PORT>" > /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "    ServerName $AGENT_HOSTNAME" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "    ProxyRequests Off" >> /etc/httpd/conf.d/sx5dotnetrest.conf 
    echo "    <Proxy *>" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "            Order deny,allow" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "            Allow from all" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "    </Proxy>" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "    ProxyPass / http://localhost:50633/" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "    ProxyPassReverse / http://localhost:50633/" >> /etc/httpd/conf.d/sx5dotnetrest.conf
    echo "</VirtualHost>" >> /etc/httpd/conf.d/sx5dotnetrest.conf

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
dotnet exec TestRestService.dll