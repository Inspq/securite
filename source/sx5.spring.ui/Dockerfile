FROM elfelip/tomcat:8.5.15

ENV TZ=America/Toronto
ENV CATALINA_HOME /opt/tomcat
ENV CATALINA_OPTS "-DSERVER_PORT=8080 -server -XX:MaxPermSize=256m -Xmx1024m"
ENV PATH $JAVA_HOME/bin:$PATH

ARG APP_VERSION=0.0.1-SNAPSHOT
ADD target/securite.ui-${APP_VERSION}.war $CATALINA_HOME/webapps/ROOT.war
ADD server.xml $CATALINA_HOME/conf

RUN rm -rf $CATALINA_HOME/webapps/ROOT
RUN rm -rf $CATALINA_HOME/webapps/docs
RUN rm -rf $CATALINA_HOME/webapps/examples
RUN rm -rf $CATALINA_HOME/webapps/host-manager
RUN rm -rf $CATALINA_HOME/webapps/manager
RUN mkdir $CATALINA_HOME/webapps/ROOT 
RUN cd $CATALINA_HOME/webapps/ROOT && jar -xvf ../ROOT.war


EXPOSE 8080 8009
WORKDIR $CATALINA_HOME

CMD ["catalina.sh", "run"]
