from nexus3.inspq.qc.ca:5000/inspq/dotnet:1.0.4

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish /opt/TestRestService

EXPOSE 5000

WORKDIR /opt/TestRestService
CMD dotnet exec TestRestService.dll