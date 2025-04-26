enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        maven("https://repo.opencollab.dev/maven-releases/") {
            name = "opencollab-releases"
        }
        maven("https://repo.opencollab.dev/maven-snapshots/") {
            name = "opencollab-snapshots"
        }
        maven("https://jitpack.io") {
            name = "jitpack"
        }
        maven("https://repo.viaversion.com" )
        maven("https://maven.lenni0451.net/everything")
        mavenCentral()
    }
}

rootProject.name = "mcprotocollib"

include(
    "protocol",
    "example"
)
