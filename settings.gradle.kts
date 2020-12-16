rootProject.name = "hh-android-plugins"

// region Shared modules

// region Shared core modules
include(":shared-core-utils")
include(":shared-core-freemarker")

project(":shared-core-utils").projectDir = File("$settingsDir/shared/core/utils")
project(":shared-core-freemarker").projectDir = File("$settingsDir/shared/core/freemarker")
// endregion

// region Shared features
include(":shared-feature-geminio-sdk")

project(":shared-feature-geminio-sdk").projectDir = File("$settingsDir/shared/features/geminio-sdk")
// endregion

// endregion

// Plugins
include(":hh-carnival")
include(":hh-garcon")
include(":hh-geminio")

project(":hh-carnival").projectDir = File("$settingsDir/plugins/hh-carnival")
project(":hh-garcon").projectDir = File("$settingsDir/plugins/hh-garcon")
project(":hh-geminio").projectDir = File("$settingsDir/plugins/hh-geminio")
