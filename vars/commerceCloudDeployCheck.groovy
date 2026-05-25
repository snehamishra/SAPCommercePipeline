def call(deployCode) {
    script {
        while (true) {
          def accessToken = getCommerceCloudToken()
          withCredentials([string(credentialsId: 'commerceCloudSubscriptionCode', variable: 'subscriptionCode')]) {
              wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${accessToken}", var: 'PASSWD']]]) {
                  result = sh (script: "curl --location --request GET 'https://portalapi.commerce.ondemand.com/v2/subscriptions/${subscriptionCode}/deployments/$deployCode' --header 'x-approuter-authorization: Bearer ${accessToken}'",returnStdout:true)
              }
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