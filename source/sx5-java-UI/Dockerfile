FROM nexus3.inspq.qc.ca:5000/tomcat:8.0.41

ENV JDK_VERSION_MAJ 1
ENV JDK_VERSION_MIN 8
ENV JDK_VERSION_PATCH 0
ENV JDK_VERSION_UPDATE 121
ENV JAVA_BASE /opt
ENV JAVA_HOME $JAVA_BASE/jdk${JDK_VERSION_MAJ}.${JDK_VERSION_MIN}.${JDK_VERSION_PATCH}_${JDK_VERSION_UPDATE}

ENV CATALINA_HOME /opt/tomcat
ENV CATALINA_OPTS "-server -XX:MaxPermSize=256m -Xmx1024m"
ENV PATH $CATALINA_HOME/bin:$PATH

ARG TOMCAT_AGENT_URL=https://nexus3.inspq.qc.ca:8443/repository/utilitaires-web/
ARG TOMCAT_AGENT_FILE=Tomcat-v6-Agent_3.5.0.zip

RUN yum install -y unzip
RUN wget --no-check-certificate $TOMCAT_AGENT_URL/$TOMCAT_AGENT_FILE -O /tmp/tomcat-agent.zip
RUN unzip /tmp/tomcat-agent.zip -d /opt

ADD configAgent.sh /tmp/configAgent.sh
RUN chmod 755 /tmp/configAgent.sh

ARG APP_VERSION=0.1.0-SNAPSHOT
ADD target/sx5-java-UI-${APP_VERSION}.war $CATALINA_HOME/webapps/sx5-java-UI.war
ADD tomcat-conf-web.xml $CATALINA_HOME/conf/web.xml

EXPOSE 8080 8009

CMD ["/tmp/configAgent.sh"]
