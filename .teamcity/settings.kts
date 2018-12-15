import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

version = "2018.2"

project {

    buildType(Build)

    params {
        add {
            param("application.name", "qw-social-v2")
        }
        add {
            param("consul.host", "127.0.0.1:8500")
        }
        add {
            password("consul.acl", "credentialsJSON:2cd3a2c5-3271-4a00-bddf-58e5cffeb4a5", display = ParameterDisplay.HIDDEN)
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            scriptContent = """
                curl \
                    -H "X-Consul-Token: %consul.acl%" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/%application.name%-dev.yml" \
                    http://%consul.host%/v1/kv/config/applications/qw/%application.name%-dev.yml
            """
        }

        script {
            scriptContent = """
                curl \
                    -H "X-Consul-Token: %consul.acl%" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/%application.name%-testing.yml" \
                    http://%consul.host%/v1/kv/config/applications/qw/%application.name%-testing.yml
            """
        }

        script {
            scriptContent = """
                curl \
                    -H "X-Consul-Token: %consul.acl%" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/%application.name%-stage.yml" \
                    http://%consul.host%/v1/kv/config/applications/qw/%application.name%-stage.yml
            """
        }
    }

    triggers {
        vcs {
        }
    }
})
