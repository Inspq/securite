FROM jboss/keycloak:latest

USER root
# Ajouter certificats
RUN mkdir /opt/jboss/certificats

ADD certificats /opt/jboss/certificats
RUN keytool -noprompt -importcert -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file /opt/jboss/certificats/ADFSPocRoot.crt -alias rootpocramq && keytool -noprompt -importcert -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file /opt/jboss/certificats/ADFSSigningCert.crt -alias signingpocramq

#USER jboss

ADD docker-entrypoint.sh /opt/jboss/
RUN chmod a+x /opt/jboss/docker-entrypoint.sh
