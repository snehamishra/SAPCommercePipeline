def call(buildName, dbUpdateMode, environmentId, strategy) {
    echo "##### Initiate Deployment to SAP Commerce Cloud Environment #####"
    //deploy tag 
    script{
        def accessToken = getCommerceCloudToken()
        withCredentials([string(credentialsId: 'commerceCloudSubscriptionCode', variable: 'subscriptionCode')]) {
            deploy = sh (script: "curl --location --request POST 'https://portalapi.commerce.ondemand.com/v2/subscriptions/${subscriptionCode}/deployments' --header 'Content-Type: application/json' --header 'x-approuter-authorization: Bearer ${accessToken}' --header 'Content-Type: text/plain' --data-raw '{\\\"buildCode\\\": \\\"${buildName}\\\",\\\"databaseUpdateMode\\\": \\\"${dbUpdateMode}\\\",\\\"environmentCode\\\": \\\"${environmentId}\\\",\\\"strategy\\\": \\\"${strategy}\\\"}'\",returnStdout:true)
            echo "$deploy"
            deploy_result = readJSON text: "$deploy"
            deploy_code = deploy_result["code"]
            return deploy_code
        }
    }
}