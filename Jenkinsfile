import hudson.model.*

node ('master'){
	def resultat = 'non'
    stage('Construire Sx5-dotnet-REST') {
    	build job:'Sx5-construire-dotnet-REST'
    }
    stage('Construire Sx5-dotnet-UI') {
    	build job:'Sx5-construire-dotnet-UI'
    }
    stage('Construire Sx5-java-REST') {
    	build job:'Sx5-construire-java-REST'
    }
    stage('Construire Sx5-java-UI') {
    	build job:'Sx5-construire-java-UI'
    }
   	stage('Deployer Sx5-dotnet-REST en B2'){
		try{
			build job:'Sx5-deployer-dotnet-REST', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-dotnet-REST', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
				]
			}
		}
    }
   	stage('Deployer Sx5-dotnet-UI en B2'){
		try{
			build job:'Sx5-deployer-dotnet-UI', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-dotnet-UI', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
				]
			}
		}
    }
   	stage('Deployer Sx5-java-REST en B2'){
		try{
			build job:'Sx5-deployer-java-REST', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-java-REST', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
				]
			}
		}
    }
   	stage('Deployer Sx5-java-UI en B2'){
		try{
			build job:'Sx5-deployer-java-UI', parameters: [
				[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
			]
		} catch(error) {
			echo "Erreur"
			retry(2) {
				input "Re Essayer?"
				build job:'Sx5-deployer-java-UI', parameters: [
					[$class: 'StringParameterValue', name: 'env.deploiement', value: 'B2']
				]
			}
		}
    }

}