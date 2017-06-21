FROM elfelip/openamdotnetwebagent:4.0.0

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish /opt/TestRestService
ENV TZ=America/Toronto

EXPOSE 50633

WORKDIR /opt/TestRestService
#CMD dotnet exec TestRestService.dll
CMD /tmp/configAgent.sh sx5dotnetrest TestRestService.dll 50633