package ru.hh.plugins.geminio.sdk.template.executors

import com.android.tools.idea.wizard.template.RecipeExecutor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPsiFactory
import ru.hh.plugins.geminio.sdk.recipe.models.commands.RecipeCommand
import ru.hh.plugins.geminio.sdk.template.mapping.expressions.evaluateString
import ru.hh.plugins.geminio.sdk.template.models.GeminioRecipeExecutorData
import ru.hh.plugins.logger.HHLogger
import java.io.File

internal fun RecipeExecutor.execute(
    targetDirectory: VirtualFile,
    command: RecipeCommand.AddNavigation,
    executorData: GeminioRecipeExecutorData
) = with(executorData) {
    if (executorData.isDryRun) {
        HHLogger.d("AddNavigationCommandExecutor command [$command] ${executorData.isDryRun}")

        val srcOut = command.srcOut.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val featureName = command.featureName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val featureModuleName = command.featureModuleName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val featurePackageName = command.featurePackageName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val featureFlowName = command.featureFlowName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val innerRouterName = command.innerRouterName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val containerName = command.containerName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val fragmentName = command.fragmentName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        HHLogger.d("AddNavigationCommandExecutor start")

        val path =
            "$srcOut/presentation/view/${featureFlowName}Flow"
        val pathComponent = File(path).toPsiDirectory(executorData.project) ?: return@with

        HHLogger.d("AddNavigationCommandExecutor pathComponent")
        val innerRouterComponent = pathComponent.findFile(innerRouterName) as KtFile

        HHLogger.d("AddNavigationCommandExecutor innerRouterComponent")
        val containerComponent = pathComponent.findFile(containerName) as KtFile

        HHLogger.d("AddNavigationCommandExecutor containerComponent")
        val fragmentComponent = pathComponent.findFile(fragmentName) as KtFile

        HHLogger.d("AddNavigationCommandExecutor fragmentComponent")

        val innerRouterImportList = innerRouterComponent.importList ?: return@with
        HHLogger.d("AddNavigationCommandExecutor innerRouterImportList")
        val containerImportList = containerComponent.importList ?: return@with
        HHLogger.d("AddNavigationCommandExecutor containerImportList")
        val fragmentImportList = fragmentComponent.importList ?: return@with
        HHLogger.d("AddNavigationCommandExecutor fragmentImportList")

        val argumentImport =
            "import $featurePackageName.presentation.view.${featureName.replaceFirstChar { it.lowercase() }}.${featureName}Argument"
        val fragmentImport = "import $featurePackageName.presentation.view.${featureName.replaceFirstChar { it.lowercase() }}.${featureName}ComponentFragment"

        val innerRouterImportListFactory = KtPsiFactory(innerRouterImportList.project)
        innerRouterImportList.add(innerRouterImportListFactory.createNewLine())
        innerRouterImportList.add(innerRouterImportListFactory.createExpression(argumentImport))
        innerRouterImportList.add(innerRouterImportListFactory.createNewLine())
        innerRouterImportList.add(innerRouterImportListFactory.createExpression(fragmentImport))
        HHLogger.d("AddNavigationCommandExecutor innerRouterImportListFactory")

        val containerImportListFactory = KtPsiFactory(containerImportList.project)
        containerImportList.add(containerImportListFactory.createNewLine())
        containerImportList.add(containerImportListFactory.createExpression(argumentImport))
        HHLogger.d("AddNavigationCommandExecutor containerImportListFactory")

        val fragmentImportListFactory = KtPsiFactory(fragmentImportList.project)
        fragmentImportList.add(fragmentImportListFactory.createNewLine())
        fragmentImportList.add(fragmentImportListFactory.createExpression(argumentImport))
        HHLogger.d("AddNavigationCommandExecutor fragmentImportListFactory")

        val innerRouterClass =
            innerRouterComponent.children.find { it is KtClass } as? KtClass ?: return@with
        HHLogger.d("AddNavigationCommandExecutor innerRouterClass")
        val containerClass =
            containerComponent.children.find { it is KtClass } as? KtClass ?: return@with
        HHLogger.d("AddNavigationCommandExecutor containerClass")
        val fragmentClass =
            fragmentComponent.children.find { it is KtClass } as? KtClass ?: return@with
        HHLogger.d("AddNavigationCommandExecutor fragmentClass")

        val innerRouterClassBody =
            innerRouterClass.children.find { it is KtClassBody } as? KtClassBody ?: return@with
        HHLogger.d("AddNavigationCommandExecutor innerRouterClassBody")
        val containerClassBody =
            containerClass.children.find { it is KtClassBody } as? KtClassBody ?: return@with
        HHLogger.d("AddNavigationCommandExecutor containerClassBody")
        val fragmentClassBody =
            fragmentClass.children.find { it is KtClassBody } as? KtClassBody ?: return@with
        HHLogger.d("AddNavigationCommandExecutor fragmentClassBody")

        val innerRouterClassBodyFactory = KtPsiFactory(innerRouterClassBody.project)
        val containerClassBodyFactory = KtPsiFactory(containerClassBody.project)
        val fragmentClassBodyFactory = KtPsiFactory(fragmentClassBody.project)

        val innerRouterLastFunction =
            innerRouterClassBody.children.findLast { it is KtNamedFunction } as? KtNamedFunction
                ?: innerRouterClassBody.lastChild
        val containerLastFunction =
            containerClassBody.children.findLast { it is KtNamedFunction } as? KtNamedFunction
                ?: containerClassBody.lastChild
        val fragmentLastFunction =
            fragmentClassBody.children.findLast { it is KtNamedFunction } as? KtNamedFunction
                ?: fragmentClassBody.lastChild

        innerRouterClass.addAfter(
            innerRouterClassBodyFactory.createInnerRouterFunction(
                featureName
            ),
            innerRouterLastFunction
        )
        innerRouterClass.addAfter(
            innerRouterClassBodyFactory.createNewLine(),
            innerRouterLastFunction
        )
        innerRouterClass.addAfter(
            innerRouterClassBodyFactory.createNewLine(),
            innerRouterLastFunction
        )

        containerClass.addAfter(
            containerClassBodyFactory.createContainerFunction(
                featureName
            ),
            containerLastFunction
        )
        containerClass.addAfter(
            containerClassBodyFactory.createNewLine(),
            containerLastFunction
        )
        containerClass.addAfter(
            containerClassBodyFactory.createNewLine(),
            containerLastFunction
        )

        fragmentClass.addAfter(
            fragmentClassBodyFactory.createFragmentFunction(
                featureName
            ),
            fragmentLastFunction
        )
        fragmentClass.addAfter(
            fragmentClassBodyFactory.createNewLine(),
            fragmentLastFunction
        )
        fragmentClass.addAfter(
            fragmentClassBodyFactory.createNewLine(),
            fragmentLastFunction
        )

        CodeStyleManager.getInstance(project).reformat(innerRouterComponent)
        CodeStyleManager.getInstance(project).reformat(containerComponent)
        CodeStyleManager.getInstance(project).reformat(fragmentComponent)

        CodeStyleManager.getInstance(project).reformat(innerRouterClassBody)
        CodeStyleManager.getInstance(project).reformat(containerClassBody)
        CodeStyleManager.getInstance(project).reformat(fragmentClassBody)

        CodeStyleManager.getInstance(project).reformat(innerRouterImportList)
        CodeStyleManager.getInstance(project).reformat(containerImportList)
        CodeStyleManager.getInstance(project).reformat(fragmentImportList)
        HHLogger.d("AddNavigationCommandExecutor END")
    }
}

private fun KtPsiFactory.createContainerFunction(featureName: String) = createFunction(
    "fun show${featureName}(argument: ${featureName}Argument)"
)

private fun KtPsiFactory.createInnerRouterFunction(featureName: String) = createFunction(
    "fun show${featureName}(argument: ${featureName}Argument) {" + "\n" +
            "val fragment = ${featureName}ComponentFragment().apply {" + "\n" +
            "arguments = bundleOf(${featureName}ComponentFragment.${featureName.uppercase()}_ARGUMENT to argument)" + "\n" +
            "}" + "\n" +
            "containerFragment.childFragmentManager.replaceContentWithBackStack(" + "\n" +
            "fragment = fragment" + "\n" +
            ")" + "\n" +
            "}"
)

private fun KtPsiFactory.createFragmentFunction(featureName: String) = createFunction(
    "override fun show${featureName}(argument: ${featureName}Argument) {" + "\n" +
            "flowInnerRouter.show${featureName}(argument = argument)" + "\n" +
            "}"
)