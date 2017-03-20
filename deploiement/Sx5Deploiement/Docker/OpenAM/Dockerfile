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

ENV OPENAM_VERSION 13.0.0
#ENV OPENAM_DOWNLOAD_URL https://inspq-6098.inspq.qc.ca:8443/repository/INSPQ-raw/OpenAM-$OPENAM_VERSION.zip
ENV INSTALL_DIR /tmp/install
RUN mkdir $INSTALL_DIR
WORKDIR $INSTALL_DIR
ADD openam-distribution/openam-distribution-ssoadmintools/target/SSOAdminTools-$OPENAM_VERSION.zip $INSTALL_DIR
ADD openam-distribution/openam-distribution-ssoconfiguratortools/target/SSOConfiguratorTools-$OPENAM_VERSION.zip $INSTALL_DIR
#RUN wget --no-check-certificate -O OpenAM.zip "$OPENAM_DOWNLOAD_URL"
#RUN unzip OpenAM.zip
#RUN cp openam/OpenAM-$OPENAM_VERSION.war $CATALINA_HOME/webapps/openam.war
ADD openam-server/target/OpenAM-$OPENAM_VERSION.war $CATALINA_HOME/webapps/openam.war
RUN mkdir -p /opt/openam-tools/admin
RUN unzip SSOAdminTools-$OPENAM_VERSION -d /opt/openam-tools/admin
RUN mkdir -p /opt/openam-tools/config
RUN unzip SSOConfiguratorTools-$OPENAM_VERSION.zip -d /opt/openam-tools/config
RUN rm -rf $INSTALL_DIR
RUN sed -i '2i rm -f /root/openam/opends/locks/*' $CATALINA_HOME/bin/catalina.sh
RUN sed -i '3i /root/openam/opends/bin/start-ds' $CATALINA_HOME/bin/catalina.sh

EXPOSE 8080 8009 4444 1689 50389

CMD ["catalina.sh", "run"]
