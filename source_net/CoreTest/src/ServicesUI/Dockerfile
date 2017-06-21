FROM elfelip/openamdotnetwebagent:4.0.0

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish /opt/ServicesUI
ENV TZ=America/Toronto

EXPOSE 5000

WORKDIR /opt/ServicesUI
#CMD dotnet exec ServicesUI.dll
CMD /tmp/configAgent.sh sx5dotnetui ServicesUI.dll 5000