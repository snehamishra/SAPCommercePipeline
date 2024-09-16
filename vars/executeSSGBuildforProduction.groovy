def call(commerceDir) {
	echo "##### Building JS application #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr"
	echo "##### Executing [yarn, install] #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr&& npm install -g yarn && yarn install"
	echo "##### Executing [yarn, build:ssr-prod1] #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr && ng update --all && yarn run build:ssr-prod1"
	echo "##### Executing dist folder commit #####"
	sh "cd ${commerceDir}/js-storefront/spartacus-ssr/dist && git add . && git commit -m 'Push from jenkins' && git push"
	//addProperty(commerceDir, "solrserver.instances.default.autostart=false")
	//sh "cd ${commerceDir}/core-customize/hybris/bin/platform && . ./setantenv.sh && ant clean all"
}
   
