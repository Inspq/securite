FROM elfelip/dotnet:1.0.4

ARG RUNTIME=centos.7-x64
ARG CONFIG=Release
ADD bin/$CONFIG/netcoreapp1.1/$RUNTIME/publish /opt/sx5dotnetrestoidc
ENV TZ=America/Toronto

EXPOSE 5001

WORKDIR /opt/sx5dotnetrestoidc
CMD dotnet exec services.dll