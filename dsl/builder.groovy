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
                choiceParam('DB_UPDATE_MODE', ['NONE', 'UPDATE'], 'Possible options for databaseUpdateMode are NONE, UPDATE, and INITIALIZE')
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
                choiceParam('DEPLOY_STRATEGY', ['ROLLING_UPDATE', 'RECREATE', 'BLUE/GREEN', 'BLUE_GREEN'], 'Deployment strategy (ROLLING_UPDATE or RECREATE or BLUE/GREEN)')
            }
        }
    }
}

// ****************************
// *** JOB DEFINITION
// ****************************

def bnlSTGBuildEveryDay = pipelineJob('BNLSTGBuildEveryDay') {
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
JobParameters.setLogs(bnlSTGBuildEveryDay)
JobParameters.setLibraryBranchParam(bnlSTGBuildEveryDay)
JobParameters.setProjectRepository(bnlSTGBuildEveryDay, projectRepo)
JobParameters.setProjectTag(bnlSTGBuildEveryDay, projectTag)
JobParameters.setProjectName(bnlSTGBuildEveryDay, projectRepoName)
JobParameters.setSonarUrl(bnlSTGBuildEveryDay, sonarUrl)
JobParameters.setPackageToTest(bnlSTGBuildEveryDay, packageToTest)

def bnlSTGPackageAndDeploy = pipelineJob('BNLSTGPackageAndDeploy') {
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


JobParameters.setLogs(bnlSTGPackageAndDeploy)
JobParameters.setLibraryBranchParam(bnlSTGPackageAndDeploy)
JobParameters.setBuildName(bnlSTGPackageAndDeploy, buildName)
JobParameters.setProjectTag(bnlSTGPackageAndDeploy, projectTag)
JobParameters.setDatabaseUpdateMode(bnlSTGPackageAndDeploy)
JobParameters.setEnvironment(bnlSTGPackageAndDeploy, environment)
JobParameters.setStrategy(bnlSTGPackageAndDeploy)

def bnlPRODBuildEveryDay = pipelineJob('BNLPRODBuildEveryDay') {
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
JobParameters.setLogs(bnlPRODBuildEveryDay)
JobParameters.setLibraryBranchParam(bnlPRODBuildEveryDay)
JobParameters.setProjectRepository(bnlPRODBuildEveryDay, projectRepo)
JobParameters.setProjectTag(bnlPRODBuildEveryDay, projectTag)
JobParameters.setProjectName(bnlPRODBuildEveryDay, projectRepoName)
JobParameters.setSonarUrl(bnlPRODBuildEveryDay, sonarUrl)
JobParameters.setPackageToTest(bnlPRODBuildEveryDay, packageToTest)

def bnlPRODPackageAndDeploy = pipelineJob('BNLPRODPackageAndDeploy') {
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


JobParameters.setLogs(bnlPRODPackageAndDeploy)
JobParameters.setLibraryBranchParam(bnlPRODPackageAndDeploy)
JobParameters.setBuildName(bnlPRODPackageAndDeploy, buildName)
JobParameters.setProjectTag(bnlPRODPackageAndDeploy, projectTag)
JobParameters.setDatabaseUpdateMode(bnlPRODPackageAndDeploy)
JobParameters.setEnvironment(bnlPRODPackageAndDeploy, environment)
JobParameters.setStrategy(bnlPRODPackageAndDeploy)

// ****************************
// *** LIST VIEW DEFINITION
// ****************************

listView('BNL Pipelines') {
    jobs {
        names(
            'BNLSTGBuildEveryDay',
            'PackageAndDeploy',
            'BNLPRODBuildEveryDay',
            'BNLPRODPackageAndDeploy'
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
