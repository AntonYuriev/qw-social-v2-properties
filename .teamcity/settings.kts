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
                    -H "X-Consul-Token: d449e42a-f011-a4ff-b7b0-efa0d54022e7" \
                    --request PUT \
                    --data "@%teamcity.build.checkoutDir%/resources/$applicationName-dev.yml" \
                    http://127.0.0.1:8500/v1/kv/config/applications/qw/qw-social-v2-dev.yml
            """
        }
    }

    triggers {
        vcs {
        }
    }
})
