def call(commerceDir, branch, projectRepository) {
    urlPrefix = "https://"
    withCredentials([[$class: 'SSHUserPrivateKeyBinding', credentialsId: 'VishnuBitBucket',  keyFileVariable: 'PASSWORD']]) {        
        repoDomainPart = projectRepository.substring(urlPrefix.size())
        repository = "https://$USERNAME:$PASSWORD@" + repoDomainPart
        echo "##### Checkout repository #####"
        sh """cd ${commerceDir} && git clone ${repository} . && git fetch --all && git checkout origin/${branch} && git pull origin ${branch} && git checkout ${branch}"""
    }
}
