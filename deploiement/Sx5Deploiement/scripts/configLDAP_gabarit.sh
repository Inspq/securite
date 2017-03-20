# Installer OpenLDAP
yum -y install openldap openldap-servers openldap-clients

# Copier les fichiers de config examples dans le repertoire du serveur LDAP
if [ -e /var/lib/ldap/DB_CONFIG ];then
	cp /usr/share/openldap-servers/DB_CONFIG.example /var/lib/ldap/DB_CONFIG
	chown ldap. /var/lib/ldap/DB_CONFIG
fi

# Demarrer et activer les services
systemctl start slapd
systemctl enable slapd

# Definir le mot de passe pour le LDAP
 PWD_ENC=`slappasswd -s @LDAPPWD@`

# Creer un fichier ldif pour la creation de l'administrateur
cat << EOF > /tmp/chrootpw.ldif
dn: olcDatabase={0}config,cn=config
changetype: modify
add: olcRootPW
olcRootPW: ${PWD_ENC}
EOF
# Importer le fichier ldif
ldapadd -Y EXTERNAL -H ldapi:/// -f /tmp/chrootpw.ldif
# Importer les schemas de bases
ldapadd -Y EXTERNAL -H ldapi:/// -f /etc/openldap/schema/cosine.ldif
ldapadd -Y EXTERNAL -H ldapi:/// -f /etc/openldap/schema/nis.ldif
ldapadd -Y EXTERNAL -H ldapi:/// -f /etc/openldap/schema/inetorgperson.ldif


# Creer le domaine
cat << EOF > /tmp/chdomain.ldif
# replace to your own domain name for "dc=***,dc=***" section

# specify the password generated above for "olcRootPW" section

dn: olcDatabase={1}monitor,cn=config
changetype: modify
replace: olcAccess
olcAccess: {0}to * by dn.base="gidNumber=0+uidNumber=0,cn=peercred,cn=external,cn=auth"
  read by dn.base="cn=Manager,@DOMAINE@" read by * none

dn: olcDatabase={2}hdb,cn=config
changetype: modify
replace: olcSuffix
olcSuffix: @DOMAINE@

dn: olcDatabase={2}hdb,cn=config
changetype: modify
replace: olcRootDN
olcRootDN: cn=Manager,@DOMAINE@

dn: olcDatabase={2}hdb,cn=config
changetype: modify
add: olcRootPW
olcRootPW: ${PWD_ENC}

dn: olcDatabase={2}hdb,cn=config
changetype: modify
add: olcAccess
olcAccess: {0}to attrs=userPassword,shadowLastChange by
  dn="cn=Manager,@DOMAINE@" write by anonymous auth by self write by * none
olcAccess: {1}to dn.base="" by * read
olcAccess: {2}to * by dn="cn=Manager,@DOMAINE@" write by * read
EOF

ldapmodify -Y EXTERNAL -H ldapi:/// -f /tmp/chdomain.ldif

cat << EOF > /tmp/basedomain.ldif
# replace to your own domain name for "dc=***,dc=***" section

dn: @DOMAINE@
objectClass: dcObject
objectclass: organization
o: bicycle2.santepublique.rtss.qc.ca
dc: bicycle2

dn: cn=Manager,@DOMAINE@
objectClass: organizationalRole
cn: Manager
description: Directory Manager

dn: ou=SEC,@DOMAINE@
objectClass: organizationalUnit
ou: People

dn: ou=users,ou=SEC,@DOMAINE@
objectClass: organizationalUnit
ou: users

dn: ou=groups,ou=SEC,@DOMAINE@
objectClass: organizationalUnit
ou: groups

dn: ou=roles,ou=SEC,@DOMAINE@
objectClass: organizationalUnit
ou: roles

EOF

ldapadd -x -D cn=Manager,@DOMAINE@ -w @LDAPPWD@ -f /tmp/basedomain.ldif 

# Configurer le firewall
firewall-cmd --add-service=ldap --permanent
firewall-cmd --reload