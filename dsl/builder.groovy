def pipelineRepo = '${PIPELINE_REPO}'
def projectRepo = '${PROJECT_REPO}'
def projectTag = '${PROJECT_TAG}'
def sonarUrl = '${SONAR_URL}'
def projectRepoName = '${SONAR_REPO_NAME}'
def packageToTest = '${PACKAGE_TO_TEST}'

def subscriptionId = '${SUBSCRIPTION_ID}'
def token = '${CLOUD_API_TOKEN}'
def buildName = '${BUILD_NAME}'
def environment = '${ENVIRONMENT_ID}'

// ****************************
// *** JOB PARAMETERS
// ****************************
class JobParameters {

    static void setLogs(job) {
        job.with {
            logRotator(-1, 15, -1, -1)
        }
    }

    static void setLibraryBranchParam(job) {
        job.with {
            parameters {
                stringParam('LIBRARY_BRANCH', 'master', 'Library branch name')
            }
        }
    }

    static void setProjectRepository(job, projectRepo) {
        job.with {
            parameters {
                stringParam('PROJECT_REPO', projectRepo, 'URL for the code repository containing your code')
            }
        }
    }

    static void setProjectTag(job, projectTag) {
        job.with {
            parameters {
                stringParam('PROJECT_TAG', projectTag, 'Tag or branch to use from your code project repository')
            }
        }
    }

    static void setProjectName(job, projectRepoName) {
        job.with {
            parameters {
                stringParam('PROJECT_REPO_NAME', projectRepoName, 'Identifier for your project')
            }
        }
    }

    static void setSonarUrl(job, sonarUrl) {
        job.with {
            parameters {
                stringParam('SONAR_URL', sonarUrl, 'Sonar Url')
            }
        }
    }

    static void setPackageToTest(job, packageToTest) {
        job.with {
            parameters {
                stringParam('PACKAGE_TO_TEST', packageToTest, 'Package(s) to test')
            }
        }
    }

    static void setBuildName(job, buildName) {
        job.with {
            parameters {
                stringParam('BUILD_NAME', buildName, 'Build Name to be used as an identifier in Cloud Portal')
            }
        }
    }

    static void setDatabaseUpdateMode(job) {
        job.with {
            parameters {
                choiceParam('DB_UPDATE_MODE', ['NONE', 'UPDATE', 'INITIALIZE'], 'Possible options for databaseUpdateMode are NONE, UPDATE, and INITIALIZE')
            }
        }
    }

    static void setEnvironment(job, environment) {
        job.with {
            parameters {
                stringParam('ENVIRONMENT_ID', environment, 'The environment ID to deploy to')
            }
        }
    }

    static void setStrategy(job) {
        job.with {
            parameters {
                choiceParam('DEPLOY_STRATEGY', ['ROLLING_UPDATE', 'RECREATE'], 'Deployment strategy (ROLLING_UPDATE or RECREATE)')
            }
        }
    }
}

// ****************************
// *** JOB DEFINITION
// ****************************

def hsSTGBuildEveryDay = pipelineJob('HSSTGBuildEveryDay') {
    definition {
        triggers {
            cron('H 18 * * *')
        }
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("githubToolsCredentials")
                    }
                    branch('${LIBRARY_BRANCH}')
                }
                scriptPath('pipelines/pipelineBuildEveryDay.groovy')
                lightweight(false)
            }
        }
    }
}
JobParameters.setLogs(hsSTGBuildEveryDay)
JobParameters.setLibraryBranchParam(hsSTGBuildEveryDay)
JobParameters.setProjectRepository(hsSTGBuildEveryDay, projectRepo)
JobParameters.setProjectTag(hsSTGBuildEveryDay, projectTag)
JobParameters.setProjectName(hsSTGBuildEveryDay, projectRepoName)
// JobParameters.setSonarUrl(hsSTGBuildEveryDay, sonarUrl)
// JobParameters.setPackageToTest(hsSTGBuildEveryDay, packageToTest)

def hsSTGPackageAndDeploy = pipelineJob('HSSTGPackageAndDeploy') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("githubToolsCredentials")
                        credentials("commerceCloudCredentials")
                    }
                    branch('${LIBRARY_BRANCH}')
                }
                scriptPath('pipelines/pipelinePackageAndDeploy.groovy')
                lightweight(false)
            }
        }
    }
}


JobParameters.setLogs(hsSTGPackageAndDeploy)
JobParameters.setLibraryBranchParam(hsSTGPackageAndDeploy)
JobParameters.setBuildName(hsSTGPackageAndDeploy, buildName)
JobParameters.setProjectTag(hsSTGPackageAndDeploy, projectTag)
JobParameters.setDatabaseUpdateMode(hsSTGPackageAndDeploy)
JobParameters.setEnvironment(hsSTGPackageAndDeploy, environment)
JobParameters.setStrategy(hsSTGPackageAndDeploy)

def hsPRODBuildEveryDay = pipelineJob('HSPRODBuildEveryDay') {
    definition {
        triggers {
            cron('H 18 * * *')
        }
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("githubToolsCredentials")
                    }
                    branch('${LIBRARY_BRANCH}')
                }
                scriptPath('pipelines/pipelineBuildEveryDayProduction.groovy')
                lightweight(false)
            }
        }
    }
}
JobParameters.setLogs(hsPRODBuildEveryDay)
JobParameters.setLibraryBranchParam(hsPRODBuildEveryDay)
JobParameters.setProjectRepository(hsPRODBuildEveryDay, projectRepo)
JobParameters.setProjectTag(hsPRODBuildEveryDay, projectTag)
JobParameters.setProjectName(hsPRODBuildEveryDay, projectRepoName)

def hsProdPackageAndDeploy = pipelineJob('HSPRODPackageAndDeploy') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("githubToolsCredentials")
                        credentials("commerceCloudCredentials")
                    }
                    branch('${LIBRARY_BRANCH}')
                }
                scriptPath('pipelines/pipelinePackageAndDeploy.groovy')
                lightweight(false)
            }
        }
    }
}


JobParameters.setLogs(hsProdPackageAndDeploy)
JobParameters.setLibraryBranchParam(hsProdPackageAndDeploy)
JobParameters.setBuildName(hsProdPackageAndDeploy, buildName)
JobParameters.setProjectTag(hsProdPackageAndDeploy, projectTag)
JobParameters.setDatabaseUpdateMode(hsProdPackageAndDeploy)
JobParameters.setEnvironment(hsProdPackageAndDeploy, environment)
JobParameters.setStrategy(hsProdPackageAndDeploy)

// ****************************
// *** LIST VIEW DEFINITION
// ****************************

listView('HS Pipelines') {
    jobs {
        names(
            'HSSTGBuildEveryDay',
            'HSSTGPackageAndDeploy',
            'HSPRODBuildEveryDay',
            'HSPRODPackageAndDeploy'
        )
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
