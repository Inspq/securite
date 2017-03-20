#!/usr/bin/bash
# Ce script permet de configurer un serveur de deploiement pour l'application fonctions allegees
# Il doit etre lance sur chacun des serveurs de deploiement en tant que root
# Parametres : Aucun

# Detecter l'OS
OS=`uname -a |awk '{print $1}'`
echo "Systeme exploitation : $OS"

# Creer le groupe proprietaire @GRP@ s'il n'existe pas
if [ -z `grep @GRP@ /etc/group` ]
then
	echo "creation du groupe proprietaire @GRP@"
	if [ "$OS" == AIX ]
	then
		# Pour AIX
		mkgroup -'A' @GRP@
	else
		groupadd @GRP@
	fi
fi
# Creer l'utilisateur administrateur pour Fonctions allegees s'il n'existe pas
if [ -z `id -u @USER@ 2> /dev/null` ]
then
	echo "Creation de l'utilisateur administrateur pour Fonctions Allegees"
	useradd -c "Administrateur deploiement de Fonctions Allegees" -s /usr/bin/bash -d /home/@USER@ -m -g @GRP@ @USER@
fi

# Configurer le PROFILE de l'utilisateur
echo "Configuration du PROFILE de l'utilisateur @USER@ : /home/@USER@/@PROFILE@"
if [ -z "`grep JAVA_HOME /home/@USER@/@PROFILE@`" ]
then
	echo "export JAVA_HOME=@JAVAHOME@" >> /home/@USER@/@PROFILE@
	echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> /home/@USER@/@PROFILE@
fi
if [ -z "`grep ANT_HOME /home/@USER@/@PROFILE@`" ]
then
	echo "export ANT_HOME=@ANTHOME@" >> /home/@USER@/@PROFILE@
	echo "export PATH=\$ANT_HOME/bin:\$PATH" >> /home/@USER@/@PROFILE@
fi
chown @USER@:@GRP@ /home/@USER@/@PROFILE@

# Creer la cle rsa pour SSH si elle n'existe pas
if [ ! -f /home/@USER@/.ssh/id_rsa ]
then
	echo "Creation de la cle rsa pour SSH"
	su - @USER@ -c "ssh-keygen -f /home/@USER@/.ssh/id_rsa -t rsa -N ''"
fi
# Preparation de Sudo
echo "Preparation de sudo pour Fonctions Allegees"

if [ "$OS" == AIX ]
then
	if [ -z `grep @SUDORUNASALIAS@ /etc/sudoers` ]
	then 
		# Modification du fichier sudoers
		cat << EOF >> /etc/sudoers
## Modification des valeurs par defaut pour le gourpe @GRP@
Defaults:%@GRP@ !requiretty

Runas_Alias @SUDORUNASALIAS@ = @USER@
## Pour la gestion fonctions allegees

%@GRP@   ALL=(@SUDORUNASALIAS@) NOPASSWD: ALL
EOF
	fi		
else
	if [ -n /etc/sudoers.d ]
	then
		mkdir -p /etc/sudoers.d
		chmod 750 /etc/sudoers.d
	fi
	if [ ! -f /etc/sudoers.d/sudo@USER@ ]
	then
		# Creation du fichier /etc/sudoers.d/sudo@USER@
		cat << EOF > /etc/sudoers.d/sudo@USER@ 
## Modification des valeurs par defaut pour le gourpe @GRP@
Defaults:%@GRP@ !requiretty

Runas_Alias @SUDORUNASALIAS@ = @USER@
## Pour la gestion fonctions allegees

%@GRP@   ALL=(@SUDORUNASALIAS@) NOPASSWD: ALL
EOF
		chmod 440 /etc/sudoers.d/sudo@USER@
	fi
	# Modifier le fichier sudoers pour ajouter l'include s'il n'y est pas.
	if [ `grep "\#includedir /etc/sudoers.d" /etc/sudoers | wc -l` = 0 ]
	then
		cat << EOF >> /etc/sudoers
## Read drop-in files from /etc/sudoers.d (the # here does not mean a comment)
#includedir /etc/sudoers.d
EOF
	fi
fi

# Creation du repertoire de base
echo "Creation du repertoire de base @BASEDIR@"
mkdir -p @BASEDIR@
chown -R @USER@:@GRP@ @BASEDIR@
chmod 775 @BASEDIR@
chmod g+s @BASEDIR@

# Ajout de l'usager jenkins dans le groupe @GRP@ s'il existe
if grep jenkins /etc/passwd; then
	if ! grep --silent '@GRP@.*jenkins' /etc/group; then 
		echo "Ajout de l'usager UNIX jenkins dans le groupe @GRP@"
		usermod -G `id -nG jenkins | tr " " ","`,@GRP@ jenkins
	else
		echo L\'usager jenkins est deja dans le groupe @GRP@
	fi
fi

# Ajout de l'usager @USER@ dans le groupe wasgroup s'il existe
if grep wasgroup /etc/group; then 
	if ! grep --silent 'wasgroup.*@USER@' /etc/group; then 
		echo "Ajout de l'usager UNIX @USER@ dans le groupe wasgroup"
		usermod -G `id -nG @USER@ | tr " " ","`,wasgroup @USER@
	else
		echo "L'usager UNIX @USER@ est deja dans le groupe wasgroup"
	fi
fi