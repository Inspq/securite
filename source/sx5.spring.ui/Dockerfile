FROM elfelip/tomcat:8.5.15

ENV CATALINA_HOME /opt/tomcat
ENV CATALINA_OPTS "-server -XX:MaxPermSize=256m -Xmx1024m"
ENV PATH $CATALINA_HOME/bin:$PATH

ARG APP_VERSION=0.0.1-SNAPSHOT
ADD target/securite.ui-${APP_VERSION}.war $CATALINA_HOME/webapps/ROOT.war

EXPOSE 8080 8009
WORKDIR $CATALINA_HOME

CMD ["catalina.sh", "run"]
