FROM elfelip/dotnet:latest

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
RUN yum install -y mod_auth_openidc
ENV SERVICESUI_HOME=/opt/ServicesUI
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish $SERVICESUI_HOME
ENV TZ=America/Toronto
ENV SERVICESUI_PORT=5000
EXPOSE $SERVICESUI_PORT

WORKDIR $SERVICESUI_HOME
#CMD dotnet exec ServicesUI.dll
ADD Keycloak/docker-entrypoint.sh $SERVICESUI_HOME/
RUN chmod a+x $SERVICESUI_HOME/docker-entrypoint.sh

ENTRYPOINT [ "/opt/ServicesUI/docker-entrypoint.sh" ]
CMD [ "ServicesUI.dll" ]