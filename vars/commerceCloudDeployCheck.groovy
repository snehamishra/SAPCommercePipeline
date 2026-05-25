def call(deployCode) {
    script {
        while (true) {
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

              result = sh (script: "curl --location --request GET 'https://portalapi.commerce.ondemand.com/v2/subscriptions/${subscriptionCode}/deployments/$deployCode' --header 'x-approuter-authorization: Bearer ${accessToken}'", returnStdout: true)
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