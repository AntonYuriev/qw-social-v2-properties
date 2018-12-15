import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script

version = "2018.2"

project {

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    val applicationName = "qw-social-v2"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            scriptContent = """
                curl \
                    -H "X-Consul-Token: %consul.acl%" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/$applicationName-dev.yml" \
                    http://%consul.host%/v1/kv/config/applications/qw/$applicationName-dev.yml
            """
        }

        script {
            scriptContent = """
                curl \
                    -H "X-Consul-Token: %consul.acl%" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/$applicationName-testing.yml" \
                    http://%consul.host%/v1/kv/config/applications/qw/$applicationName-testing.yml
            """
        }

        script {
            scriptContent = """
                curl \
                    -H "X-Consul-Token: %consul.acl%" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/$applicationName-stage.yml" \
                    http://%consul.host%/v1/kv/config/applications/qw/$applicationName-stage.yml
            """
        }
    }

    triggers {
        vcs {
        }
    }
})
