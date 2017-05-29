FROM elfelip/tomcat:8.5.14

ENV CATALINA_HOME /opt/tomcat
ENV CATALINA_OPTS "-server -XX:MaxPermSize=256m -Xmx1024m"
ENV PATH $CATALINA_HOME/bin:$PATH

ARG APP_VERSION=0.0.1-SNAPSHOT
ADD target/sx5-java-UI-${APP_VERSION}.war $CATALINA_HOME/webapps/sx5-java-UI.war

EXPOSE 8080 8009

CMD ["/tmp/configAgent.sh"]
