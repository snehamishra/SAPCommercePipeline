def call(buildCode, dbUpdateMode, environmentId, strategy) {

    echo "##### Initiate Deployment to SAP Commerce Cloud Environment #####"
    //deploy tag 
    script{
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'commerceCloudCredentials', usernameVariable: 'subscriptionId', passwordVariable: 'token']]) {
            deploy = sh (script: "curl --location --request POST 'https://portalapi.commerce.ondemand.com/v2/subscriptions/${subscriptionId}/deployments' --header 'Content-Type: application/json' --header 'Authorization: Bearer ${token}' --header 'Content-Type: text/plain' --data-raw '{\"buildCode\": \"${buildName}\",\"databaseUpdateMode\": \"${dbUpdateMode}\",\"environmentCode\": \"${environmentId}\",\"strategy\": \"${strategy}\"}'",returnStdout:true)
            echo "$deploy"
            deploy_result = readJSON text: "$deploy"
            deploy_code = deploy_result["code"]
            return deploy_code
        }

        if (!body.startsWith("{")) {
            error("Deployment API did not return valid JSON:\n${body}")
        }

        def json = readJSON text: body

        echo "=============================="
        echo "DEPLOYMENT TRIGGERED SUCCESSFULLY"
        echo "DEPLOYMENT CODE = ${json.code}"
        echo "=============================="

        return json.code
    }
}