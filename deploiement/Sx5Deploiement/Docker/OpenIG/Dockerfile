FROM inspq-6098.inspq.qc.ca:8444/tomcat-oracle-jdk8

ENV JDK_VERSION_MAJ 1
ENV JDK_VERSION_MIN 8
ENV JDK_VERSION_PATCH 0
ENV JDK_VERSION_UPDATE 92
ENV JAVA_BASE /opt
ENV JAVA_HOME $JAVA_BASE/jdk${JDK_VERSION_MAJ}.${JDK_VERSION_MIN}.${JDK_VERSION_PATCH}_${JDK_VERSION_UPDATE}

ENV CATALINA_HOME /opt/tomcat
ENV CATALINA_OPTS "-server -XX:MaxPermSize=256m -Xmx1024m"
ENV PATH $CATALINA_HOME/bin:$PATH

RUN yum install -y unzip which

ENV OPENIG_VERSION 4.0.0
ENV OPENIG_BASE /var/openig

ADD openig-war/target/OpenIG-$OPENIG_VERSION.war /tmp/openig.war
ADD config $OPENIG_BASE

RUN rm -rf $CATALINA_HOME/webapps/*

RUN unzip -q /tmp/openig.war -d $CATALINA_HOME/webapps/ROOT && rm -f /tmp/openig.war

EXPOSE 8080 8443

CMD ["catalina.sh", "run"]
