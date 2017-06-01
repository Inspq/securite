import hudson.model.*

node ('master'){
	// Construire Sx5-dotnet-REST
    stage('Construire Sx5-dotnet-REST') {
    	build job:'Sx5-construire-dotnet-REST'
    }
    stage('Construire Sx5-dotnet-REST-OIDC') {
    	build job:'Sx5-construire-dotnet-REST-OIDC'
    }
    stage('Construire Sx5-dotnet-UI') {
    	build job:'Sx5-construire-dotnet-UI'
    }
    stage('Construire Sx5-dotnet-UI-OIDC') {
    	build job:'Sx5-construire-dotnet-UI-OIDC'
    }
    stage('Construire Sx5-java-REST') {
    	build job:'Sx5-construire-java-REST'
    }
    stage('Construire Sx5-java-UI') {
    	build job:'Sx5-construire-java-UI'
    }
    stage('Construire Sx5-java-REST-OIDC') {
    	build job:'Sx5-construire-java-REST-OIDC'
    }
    stage('Construire Sx5-java-UI-OIDC') {
    	build job:'Sx5-construire-java-UI-OIDC'
    }
   	stage('Deployer Sx5-dotnet-REST en ' + params.env){
		try{
			build job:'Sx5-deployer-dotnet-REST', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-dotnet-REST', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-dotnet-REST-OIDC en ' + params.env){
		try{
			build job:'Sx5-deployer-dotnet-REST-OIDC', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-dotnet-REST-OIDC', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-dotnet-UI en ' + params.env){
		try{
			build job:'Sx5-deployer-dotnet-UI', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-dotnet-UI', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-dotnet-UI-OIDC en ' + params.env){
		try{
			build job:'Sx5-deployer-dotnet-UI-OIDC', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-dotnet-UI-OIDC', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-java-REST en ' + params.env){
		try{
			build job:'Sx5-deployer-java-REST', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-java-REST', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-java-UI en ' + params.env){
		try{
			build job:'Sx5-deployer-java-UI', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-java-UI', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-java-REST-OIDC en ' + params.env){
		try{
			build job:'Sx5-deployer-java-REST-OIDC', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-java-REST-OIDC', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
   	stage('Deployer Sx5-java-UI-OIDC en ' + params.env){
		try{
			build job:'Sx5-deployer-java-UI-OIDC', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-java-UI-OIDC', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: params.env]
				]
			}
		}
    }
}