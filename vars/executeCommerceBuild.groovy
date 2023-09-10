def call(commerceDir) {
	echo "##### Goto spartacus folder #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr"
	echo "##### Install yarn #####"
	sh "yarn install"
	echo "##### Execute build #####"
	sh "yarn run build:ssr"
	echo "##### Commit dist folder #####"
	sh "git commit dist && git push"
	
	
	//addProperty(commerceDir, "solrserver.instances.default.autostart=false")
	//sh "cd ${commerceDir}/core-customize/hybris/bin/platform && . ./setantenv.sh && ant clean all"
} 
   
