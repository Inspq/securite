#!/bin/bash
# Script d'installation de l'agent web d'OpenAM
# Arguments: 
#	dll: Fichier dll dotnet core a exécuter

set -e
DOTNET_DLL=$1

if [ ! -e /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf ]; then
    echo "<VirtualHost *:${VIRTUAL_HOST_PORT}>" > /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ServerName ${VIRTUAL_HOST_NAME}" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthIntrospectionEndpoint    $OIDC_TOKEN_INTRO_URL" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthIntrospectionEndpointMethod    GET" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthSSLValidateServer    Off" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthRemoteUserClaim    audience" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthIntrospectionTokenParamName    access_token" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo " " >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthClientID    $OIDC_CLIENT_ID" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCOAuthClientSecret    $OIDC_CLIENT_SECRET" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    <LocationMatch "/">" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "        Authtype oauth20" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "        Require valid-user" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    </LocationMatch>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ProxyPass / http://localhost:$TESTRESTSERVICE_PORT/" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ProxyPassReverse / http://localhost:$TESTRESTSERVICE_PORT/" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "</VirtualHost>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf

    sed -i s'/Listen 80/Listen '"${VIRTUAL_HOST_PORT}"'/g' /etc/httpd/conf/httpd.conf
fi

apachectl

exec dotnet exec $@
exit $?