package ru.hh.plugins.geminio.sdk.recipe.parsers.commands

import ru.hh.plugins.geminio.sdk.recipe.models.commands.RecipeCommand
import ru.hh.plugins.geminio.sdk.recipe.parsers.ParsersErrorsFactory
import ru.hh.plugins.geminio.sdk.recipe.parsers.expressions.toRecipeExpression

private const val KEY_COMMAND_SRC_OUT = "srcOut"
private const val KEY_COMMAND_FEATURE_NAME = "featureName"
private const val KEY_COMMAND_FEATURE_MODULE_NAME = "featureModuleName"
private const val KEY_COMMAND_FEATURE_PACKAGE_NAME = "featurePackageName"
private const val KEY_COMMAND_FEATURE_FLOW_NAME = "featureFlowName"
private const val KEY_COMMAND_INNER_ROUTER_NAME = "innerRouterName"
private const val KEY_COMMAND_CONTAINER_NAME = "containerName"
private const val KEY_COMMAND_FRAGMENT_NAME = "fragmentName"

/**
 * Parser from YAML to [ru.hh.plugins.geminio.sdk.recipe.models.commands.RecipeCommand.Instantiate] command.
 */
internal fun Map<String, Any>.toAddNavigationCommand(sectionName: String): RecipeCommand.AddNavigation {
    val srcOut = requireNotNull(this[KEY_COMMAND_SRC_OUT] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_SRC_OUT
        )
    }

    val featureName = requireNotNull(this[KEY_COMMAND_FEATURE_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FEATURE_NAME
        )
    }

    val featureModuleName = requireNotNull(this[KEY_COMMAND_FEATURE_MODULE_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FEATURE_MODULE_NAME
        )
    }

    val featurePackageName = requireNotNull(this[KEY_COMMAND_FEATURE_PACKAGE_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FEATURE_PACKAGE_NAME
        )
    }

    val featureFlowName = requireNotNull(this[KEY_COMMAND_FEATURE_FLOW_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FEATURE_FLOW_NAME
        )
    }

    val innerRouterName = requireNotNull(this[KEY_COMMAND_INNER_ROUTER_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_INNER_ROUTER_NAME
        )
    }

    val containerName = requireNotNull(this[KEY_COMMAND_CONTAINER_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_CONTAINER_NAME
        )
    }

    val fragmentName = requireNotNull(this[KEY_COMMAND_FRAGMENT_NAME] as? String) {
        ParsersErrorsFactory.sectionRequiredParameterErrorMessage(
            sectionName = sectionName,
            key = KEY_COMMAND_FRAGMENT_NAME
        )
    }

    return RecipeCommand.AddNavigation(
        srcOut = srcOut.toRecipeExpression(sectionName),
        featureName = featureName.toRecipeExpression(sectionName),
        featureModuleName = featureModuleName.toRecipeExpression(sectionName),
        featurePackageName = featurePackageName.toRecipeExpression(sectionName),
        featureFlowName = featureFlowName.toRecipeExpression(sectionName),
        innerRouterName = innerRouterName.toRecipeExpression(sectionName),
        containerName = containerName.toRecipeExpression(sectionName),
        fragmentName = fragmentName.toRecipeExpression(sectionName),
    )
}