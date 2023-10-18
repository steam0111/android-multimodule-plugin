package ru.hh.plugins.geminio.sdk.recipe.parsers.commands

import ru.hh.plugins.geminio.sdk.recipe.models.commands.RecipeCommand
import ru.hh.plugins.geminio.sdk.recipe.parsers.ParsersErrorsFactory
import ru.hh.plugins.geminio.sdk.recipe.parsers.expressions.toRecipeExpression

private const val KEY_COMMAND_FEATURE_NAME = "featureName"
private const val KEY_COMMAND_COMPONENT_PATH = "componentPath"
private const val KEY_COMMAND_COMPONENT_NAME = "componentName"
private const val KEY_COMMAND_FEATURE_PACKAGE = "featurePackage"

/**
 * Parser from YAML to [ru.hh.plugins.geminio.sdk.recipe.models.commands.RecipeCommand.Instantiate] command.
 */
internal fun Map<String, Any>.toAddViewModelFactoryToDaggerModule(sectionName: String): RecipeCommand.AddViewModelFactoryToDaggerModule {
    val featureName = requireNotNull(this[KEY_COMMAND_FEATURE_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FEATURE_NAME
        )
    }

    val componentPath = requireNotNull(this[KEY_COMMAND_COMPONENT_PATH] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_COMPONENT_PATH
        )
    }

    val componentName = requireNotNull(this[KEY_COMMAND_COMPONENT_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_COMPONENT_NAME
        )
    }

    val featurePackage = requireNotNull(this[KEY_COMMAND_FEATURE_PACKAGE] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FEATURE_PACKAGE
        )
    }

    return RecipeCommand.AddViewModelFactoryToDaggerModule(
        featureName = featureName.toRecipeExpression(sectionName),
        componentPath = componentPath.toRecipeExpression(sectionName),
        componentName = componentName.toRecipeExpression(sectionName),
        featurePackage = featurePackage.toRecipeExpression(sectionName)
    )
}