FROM elfelip/dotnet:latest

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
RUN yum install -y mod_auth_openidc
ENV TESTRESTSERVICE_HOME=/opt/TestRestService
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish $TESTRESTSERVICE_HOME
ENV TZ=America/Toronto
ENV TESTRESTSERVICE_PORT=50633
EXPOSE $TESTRESTSERVICE_PORT

WORKDIR $TESTRESTSERVICE_HOME
#CMD dotnet exec ServicesUI.dll
ADD Keycloak/docker-entrypoint.sh $TESTRESTSERVICE_HOME/
RUN chmod a+x $TESTRESTSERVICE_HOME/docker-entrypoint.sh

ENTRYPOINT [ "/opt/TestRestService/docker-entrypoint.sh" ]
CMD [ "TestRestService.dll" ]