#!/bin/bash
# Script d'installation de l'agent web d'OpenAM
# Arguments: 
#	dll: Fichier dll dotnet core a exécuter

set -e
DOTNET_DLL=$1

if [ ! -e /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf ]; then
    echo "<VirtualHost *:${VIRTUAL_HOST_PORT}>" > /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCProviderMetadataURL $OIDC_AUTHORITY_URL" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCSSLValidateServer Off" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCRemoteUserClaim email" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCScope \"openid email\"" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCProviderIssuer $OIDC_ISSUER" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCProviderTokenEndpointAuth client_secret_basic" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo " " >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCClientID $OIDC_CLIENT_ID" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCClientSecret $OIDC_CLIENT_SECRET" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCCryptoPassphrase $OIDC_CLIENT_SECRET" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    OIDCRedirectURI $OIDC_REDIRECT_URI" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ServerName ${VIRTUAL_HOST_NAME}" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    <LocationMatch "/">" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "        AuthType openid-connect" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "        Require valid-user" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    </LocationMatch>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ProxyPass / http://localhost:$SERVICESUI_PORT/" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "    ProxyPassReverse / http://localhost:$SERVICESUI_PORT/" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf
    echo "</VirtualHost>" >> /etc/httpd/conf.d/${VIRTUAL_HOST_NAME}.conf

    sed -i s'/Listen 80/Listen '"${VIRTUAL_HOST_PORT}"'/g' /etc/httpd/conf/httpd.conf
fi

apachectl

exec dotnet exec $@
exit $?