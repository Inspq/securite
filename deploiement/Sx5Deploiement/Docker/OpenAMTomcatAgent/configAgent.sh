#!/bin/bash
# Script d'installation de l'agent j2ee d'OpenAM

set -e
AGENT_PROTO=`echo $AGENT_URL | awk -F: '{print $1}'`
AGENT_HOSTNAME=`echo $AGENT_URL | awk -F '[/:]' '{print $4}'`
AGENT_PORT=`echo $AGENT_URL | awk -F '[/:]' '{print $5}'`
AGENT_URI=`echo $AGENT_URL | awk -F/ '{print $4}'`
TMP_PORT=60999

if [ ! -e /opt/j2ee_agents/tomcat_v6_agent/Agent_001/config/OpenSSOAgentBootstrap.properties ]; then
	echo $AGENT_PWD > /tmp/passwd.txt
	chmod 400 /tmp/passwd.txt

	echo CONFIG_DIR= /opt/tomcat/conf > /tmp/j2ee-agent.rsp
	echo AM_SERVER_URL= $OPENAM_URL >> /tmp/j2ee-agent.rsp
	echo CATALINA_HOME= /opt/tomcat >> /tmp/j2ee-agent.rsp
	echo INSTALL_GLOBAL_WEB_XML=  >> /tmp/j2ee-agent.rsp
	echo AGENT_URL= $AGENT_PROTO://$AGENT_HOSTNAME:$TMP_PORT/$AGENT_URI >> /tmp/j2ee-agent.rsp
	echo AGENT_PROFILE_NAME= $AGENT_PROFILE_NAME >> /tmp/j2ee-agent.rsp
	echo AGENT_PASSWORD_FILE= /tmp/passwd.txt >> /tmp/j2ee-agent.rsp

	# Attendre que le serveur OpenAM soit disponible
	sleep 30
	curl -f -m 60 --retry 5 --retry-delay 30 $OPENAM_URL
	
	/opt/j2ee_agents/tomcat_v6_agent/bin/agentadmin --install --acceptLicense --useResponse /tmp/j2ee-agent.rsp
sed -i s'/'"${TMP_PORT}"'/'"${AGENT_PORT}"'/g' /opt/j2ee_agents/tomcat_v6_agent/Agent_001/config/OpenSSOAgentBootstrap.properties
	sed -i s'/'"${TMP_PORT}"'/'"${AGENT_PORT}"'/g' /opt/j2ee_agents/tomcat_v6_agent/Agent_001/config/OpenSSOAgentConfiguration.properties
fi

catalina.sh run
