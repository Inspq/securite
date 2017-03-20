#!/bin/bash
# Script utilise pour executer une cible ANT du script deployer.xml pour un environnement specifique.
# Arguments :   Environmement (DEV,INT,PAR,ACC,PP,LABO, PROD etc.)
#               Cible (deployerTout, miseAJour etc.)

# Source profile
. $HOME/@PROFILE@

env=$1
tname=$2
version=$3
BASEDIR=$(dirname $0)
	
propsDir=$@ENVPROPS@

if [[ $# != 3 ]]; then
	echo "\n\tPas assez de parametre."
	echo "\n\tExample:  $0 [Environnement] [Cible] [Repertoire de base] [Version]"
	echo "\t          $0 DEV deployerTout 0.1.006.27042015.0013"
	exit 1
else
	if [[ ! -f $propsDir/$1.properties ]]; then
		echo "Le fichier $propsDir/$1.properties n'existe pas..."
		exit 1
	fi
fi

echo "ANT: Execution de la cible $tname task..."
cd $BASEDIR
$ANT_HOME/bin/ant $tname -buildfile deployer.xml -Denv.deploiement=$env -DRepertoireProprietesDeploiement=$propsDir -Dversion.artefact=$version -l deploiement$env-$tname.log

antReturnCode=$?

echo "ANT: Code de retour: \""$antReturnCode"\""

if [[ $antReturnCode -ne 0 ]];then
	echo "Erreur de deploiement."
	echo "Repertoire du fichier de proprietes = $propsDir"
	echo "Environnement cible: $env"
	echo "Cible ANT lancee: $tname"
	cat deploiement$env-$tname.log
	exit 1;
else
	echo "SUCCES"
	echo "Repertoire du fichier de proprietes = $propsDir"
	echo "Environnement cible: $env"
	echo "Tache ANT lancee: $tname"
	cat deploiement$env-$tname.log
	exit 0;
fi