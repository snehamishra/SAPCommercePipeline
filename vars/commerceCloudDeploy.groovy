def call(buildName, dbUpdateMode, environmentId, strategy) {
    echo "##### Initiate Deployment to SAP Commerce Cloud Environment #####"
    //deploy tag 
    script{
        withCredentials([
            string(credentialsId: 'commerceCloudSubscriptionCode', variable: 'subscriptionCode'),
            string(credentialsId: 'commerceCloudApiClientId', variable: 'clientId'),
            string(credentialsId: 'commerceCloudApiClientSecret', variable: 'clientSecret')
        ]) {
            // Obtain OAuth2 access token
            tokenResponse = sh (script: """curl --location --request POST 'https://ycloud.accounts.ondemand.com/oauth2/token' \
                -H 'Content-Type: application/x-www-form-urlencoded' \
                -H 'Accept: application/json' \
                -d 'client_id=${clientId}' \
                -d 'client_secret=${clientSecret}' \
                --data-urlencode 'grant_type=client_credentials' \
                --data-urlencode 'resource=urn:sap:identity:application:provider:name:cp-dependency'""", returnStdout: true)
            tokenResult = readJSON text: "${tokenResponse}"
            accessToken = tokenResult["access_token"]

            deploy = sh (script: "curl --location --request POST 'https://portalapi.commerce.ondemand.com/v2/subscriptions/${subscriptionCode}/deployments' --header 'Content-Type: application/json' --header 'x-approuter-authorization: Bearer ${accessToken}' --data-raw '{\"buildCode\": \"${buildName}\",\"databaseUpdateMode\": \"${dbUpdateMode}\",\"environmentCode\": \"${environmentId}\",\"strategy\": \"${strategy}\"}'", returnStdout: true)
            echo "$deploy"
            deploy_result = readJSON text: "$deploy"
            deploy_code = deploy_result["code"]
            return deploy_code
        }
    }
}