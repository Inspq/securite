#!/bin/bash
# Script permettant de modifier le fichier .profile d'un usager unix en fonction d'un deploiement automatisee.
# Arguments : 	Nom du fichier .profile
#				Nom de la variable d'environnement pour le deploiement (XXXXPropertiesDir)
#				Valeur a assigner a la vairable d'environnement (/SIPMI/XXXX/properties)

PROFILE_FILE=$1
ENV_VAR=$2
ENV_VALUE=$3

if grep $ENV_VAR $PROFILE_FILE
then
	echo "La propriete existe deja"
else
	cp $PROFILE_FILE $PROFILE_FILE.bak
	echo "export $ENV_VAR=$ENV_VALUE" >> $PROFILE_FILE
fi