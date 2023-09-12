def call(commerceDir) {
	echo "##### Building JS application #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr"
	echo "##### Executing [yarn, install] #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr&& npm install -g yarn && yarn install"
	echo "##### Executing [yarn, build:ssr] #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr && ng update --all && yarn run build:ssr"
	echo "##### Executing dist folder commit #####"
	//withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'githubCodeRepoCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {        
        sh "cd ${commerceDir}/js-storefront/spartacus-ssr/dist && git add . && git commit --author="sneha mishra <sneha.mishra@bestandless.com.au>" && git push"
	
	//sh "cd ${commerceDir}/js-storefront/spartacus-ssr && git add dist/* && git commit dist && git push"
	
	
	//addProperty(commerceDir, "solrserver.instances.default.autostart=false")
	//sh "cd ${commerceDir}/core-customize/hybris/bin/platform && . ./setantenv.sh && ant clean all"
} 
   
