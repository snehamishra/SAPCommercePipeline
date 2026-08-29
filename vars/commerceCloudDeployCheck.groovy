def call(deployCode) {
    script {
        while (true) {
          withCredentials([
              string(credentialsId: 'commerceCloudSubscriptionCode', variable: 'subscriptionCode'),
              string(credentialsId: 'commerceCloudClientId', variable: 'COMMERCE_CLOUD_CLIENT_ID'),
              string(credentialsId: 'commerceCloudClientSecret', variable: 'COMMERCE_CLOUD_CLIENT_SECRET')
          ]) {
              def token = getCommerceCloudToken()
              result = sh (script: "curl --location --request GET 'https://portalapi.commerce.ondemand.com/v2/subscriptions/${subscriptionCode}/deployments/$deployCode' --header 'x-approuter-authorization: Bearer ${token}'",returnStdout:true)
          }
          echo "$result"
          statusResult = readJSON text: "$result"

          if("DEPLOYED".equals(statusResult["status"])) {
            break;
          }

          if("FAIL".equals(statusResult["status"])) {
            error("Deployment was not completed successfully on SAP Commerce Cloud")
          }

          sh('sleep 120s')

        }

        echo "Commerce Cloud Deploy Complete"
    }
}  
