FROM elfelip/dotnet:1.0.4

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish /opt/sx5dotnetuioidc
ENV TZ=America/Toronto

EXPOSE 5000

WORKDIR /opt/sx5dotnetuioidc
CMD dotnet exec scratch.dll