from nexus3.inspq.qc.ca:5000/inspq/dotnet:1.0.4

RUN yum install -y wget java7 httpd unzip links

ARG WEB_AGENT_URL=https://nexus3.inspq.qc.ca:8443/repository/utilitaires-web/
ARG WEB_AGENT_FILE=Apache_v24_Linux_64bit_4.0.0.zip
RUN wget --no-check-certificate $WEB_AGENT_URL/$WEB_AGENT_FILE -O /tmp/web-agent.zip
RUN unzip /tmp/web-agent.zip -d /opt

ADD configAgent.sh /tmp/configAgent.sh
RUN chmod 755 /tmp/configAgent.sh
ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish /opt/ServicesUI

EXPOSE 5000

WORKDIR /opt/ServicesUI
#CMD dotnet exec ServicesUI.dll
CMD /tmp/configAgent.sh