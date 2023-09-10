def call(commerceDir) {
	echo "##### Building JS application #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr"
	echo "##### Executing [yarn, install] #####"
	sh "npm install -g yarn && yarn install"
	echo "##### Executing [yarn, build:ssr] #####"
	sh "yarn run build:ssr"
	echo "##### Executing dist folder commit #####"
	sh "git commit dist && git push"
	
	
	//addProperty(commerceDir, "solrserver.instances.default.autostart=false")
	//sh "cd ${commerceDir}/core-customize/hybris/bin/platform && . ./setantenv.sh && ant clean all"
} 
   
