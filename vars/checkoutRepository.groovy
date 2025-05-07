def call(commerceDir, branch, projectRepository) {
    urlPrefix = "https://"
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'VishnuPwd', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {        
        //repoDomainPart = projectRepository.substring(urlPrefix.size())
        //repository = "https://$USERNAME:$PASSWORD@" + repoDomainPart
        echo "##### Checkout repository #####"
        sh """cd ${commerceDir} && git clone ${projectRepository} . && git fetch --all && git checkout origin/${branch} && git pull origin ${branch} && git checkout ${branch}"""
    }
}
