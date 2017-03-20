FROM centos:latest

RUN yum install -y wget

ENV JDK_VERSION_MAJ 1
ENV JDK_VERSION_MIN 8
ENV JDK_VERSION_PATCH 0
ENV JDK_VERSION_UPDATE 92
ENV JDK_URL http://download.oracle.com/otn-pub/java/jdk/8u92-b14
ENV JAVA_BASE /opt
ENV JAVA_HOME $JAVA_BASE/jdk${JDK_VERSION_MAJ}.${JDK_VERSION_MIN}.${JDK_VERSION_PATCH}_${JDK_VERSION_UPDATE}
RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" ${JDK_URL}/jdk-${JDK_VERSION_MIN}u${JDK_VERSION_UPDATE}-linux-x64.tar.gz \
	&& tar -xzvf jdk-${JDK_VERSION_MIN}u${JDK_VERSION_UPDATE}-linux-x64.tar.gz -C /opt \
	&& rm jdk-${JDK_VERSION_MIN}u${JDK_VERSION_UPDATE}-linux-x64.tar.gz
	
ENV CATALINA_HOME /opt/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN mkdir -p "$CATALINA_HOME"
WORKDIR $CATALINA_HOME

VAR TOMCAT_MAJOR 8
VAR TOMCAT_VERSION 8.0.37
ENV TOMCAT_TGZ_URL https://www.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz

RUN set -x \
	\
	&& wget -O tomcat.tar.gz "$TOMCAT_TGZ_URL" \
	&& tar -xvf tomcat.tar.gz --strip-components=1 \
	&& rm bin/*.bat \
	&& rm tomcat.tar.gz*

EXPOSE 8080 8009
CMD ["catalina.sh", "run"]