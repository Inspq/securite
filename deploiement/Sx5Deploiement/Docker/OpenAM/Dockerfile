#FROM inspq-6098.inspq.qc.ca:8444/tomcat-oracle-jdk8
FROM elfelip/tomcat:8.5.14

ENV TZ "America/Toronto"
ENV CATALINA_OPTS "-server -XX:MaxPermSize=256m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"
ENV PATH $JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH

RUN yum install -y unzip which wget

ENV OPENAM_VERSION 5.1.0
#ENV OPENAM_DOWNLOAD_URL https://inspq-6098.inspq.qc.ca:8443/repository/INSPQ-raw/OpenAM-$OPENAM_VERSION.zip
ENV OPENAM_DOWNLOAD_URL https://nexus3.inspq.qc.ca:8443/repository/utilitaires-infrastructure/AM-eval-$OPENAM_VERSION.zip
ENV INSTALL_DIR /tmp/install
RUN mkdir $INSTALL_DIR
WORKDIR $INSTALL_DIR
#ADD openam-distribution/openam-distribution-ssoadmintools/target/SSOAdminTools-$OPENAM_VERSION.zip $INSTALL_DIR
#ADD openam-distribution/openam-distribution-ssoconfiguratortools/target/SSOConfiguratorTools-$OPENAM_VERSION.zip $INSTALL_DIR
#ADD openam-server/target/OpenAM-$OPENAM_VERSION.war $CATALINA_HOME/webapps/openam.war
RUN wget --no-check-certificate -O $INSTALL_DIR/OpenAM.zip "$OPENAM_DOWNLOAD_URL"
RUN unzip $INSTALL_DIR/OpenAM.zip -d $INSTALL_DIR
RUN cp openam/AM-eval-$OPENAM_VERSION.war $CATALINA_HOME/webapps/openam.war
RUN mkdir -p /opt/openam-tools/admin
RUN unzip SSOAdminTools-$OPENAM_VERSION -d /opt/openam-tools/admin
RUN mkdir -p /opt/openam-tools/config
RUN unzip SSOConfiguratorTools-$OPENAM_VERSION.zip -d /opt/openam-tools/config
RUN rm -rf $INSTALL_DIR
RUN sed -i '2i rm -f /root/openam/opends/locks/*' $CATALINA_HOME/bin/catalina.sh
RUN sed -i '3i /root/openam/opends/bin/start-ds' $CATALINA_HOME/bin/catalina.sh

EXPOSE 8080 8009 4444 1689 50389
WORKDIR $CATALINA_HOME
CMD ["catalina.sh", "run"]
