def call() {
    def tokenEndpoint = "https://ycloud.accounts.ondemand.com/oauth2/token"
    def resource = "urn:sap:identity:application:provider:name:cp-dependency"

    def tokenResponse = sh(
        script: """curl --silent --location --request POST '${tokenEndpoint}' \
            -H 'Content-Type: application/x-www-form-urlencoded' \
            -H 'Accept: application/json' \
            -d 'client_id=${COMMERCE_CLOUD_CLIENT_ID}' \
            -d 'client_secret=${COMMERCE_CLOUD_CLIENT_SECRET}' \
            --data-urlencode 'grant_type=client_credentials' \
            --data-urlencode 'resource=${resource}'""",
        returnStdout: true
    )

    def tokenJson = readJSON text: tokenResponse
    if (!tokenJson["access_token"]) {
        error("Failed to obtain access token from SAP Commerce Cloud OAuth2 endpoint")
    }
    return tokenJson["access_token"]
}