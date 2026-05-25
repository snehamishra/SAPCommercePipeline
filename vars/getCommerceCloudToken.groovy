def call() {
    echo "##### Obtaining OAuth2 Access Token for SAP Commerce Cloud #####"
    script {
        withCredentials([
            string(credentialsId: 'commerceCloudTokenEndpoint', variable: 'tokenEndpoint'),
            string(credentialsId: 'commerceCloudClientId', variable: 'clientId'),
            string(credentialsId: 'commerceCloudClientSecret', variable: 'clientSecret'),
            string(credentialsId: 'commerceCloudResource', variable: 'resource')
        ]) {
            def tokenResponse = sh(
                script: """curl --silent --location --request POST '${tokenEndpoint}' \
                    --header 'Content-Type: application/x-www-form-urlencoded' \
                    --header 'Accept: application/json' \
                    -d 'client_id=${clientId}' \
                    -d 'client_secret=${clientSecret}' \
                    --data-urlencode 'grant_type=client_credentials' \
                    --data-urlencode 'resource=${resource}'""",
                returnStdout: true
            ).trim()
            def tokenJson = readJSON text: tokenResponse
            if (!tokenJson["access_token"]) {
                error("Failed to obtain OAuth2 access token from SAP Commerce Cloud")
            }
            return tokenJson["access_token"]
        }
    }
}
