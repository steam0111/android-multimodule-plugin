package ru.hh.plugins.geminio.sdk.template.executors

import com.android.tools.idea.wizard.template.RecipeExecutor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.asJava.findFacadeClass
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
    command: RecipeCommand.AddViewModelFactoryToDaggerModule,
    executorData: GeminioRecipeExecutorData
) = with(executorData) {
    if (executorData.isDryRun) {
        HHLogger.d("AddViewModelFactoryToDaggerModule command [$command] ${executorData.isDryRun}")

        val featureName = command.featureName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val componentPath = command.componentPath.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val componentName = command.componentName.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val featurePackage = command.featurePackage.evaluateString(
            targetDirectory,
            moduleTemplateData,
            existingParametersMap
        ) ?: return@with

        val featureComponentPath =
            File(componentPath).toPsiDirectory(executorData.project) ?: return
        val featureComponentPsi = featureComponentPath.findFile(componentName) as KtFile

        val featureComponentImportList = featureComponentPsi.importList ?: return@with
        val importListFactory = KtPsiFactory(featureComponentImportList.project)
        featureComponentImportList.add(importListFactory.createNewLine())
        featureComponentImportList.add(
            importListFactory.createExpression("import $featurePackage.di.${featureName}ViewModelFactory")
        )

        val featureComponentClassPsi =
            featureComponentPsi.children.find { it is KtClass } as? KtClass? ?: return@with
        val featureComponentClassBodyPsi =
            featureComponentClassPsi.children.find { it is KtClassBody } as? KtClassBody
                ?: return@with
        val featureComponentClassBodyFactory = KtPsiFactory(featureComponentClassBodyPsi.project)
        val lastFunction = featureComponentClassBodyPsi.children.findLast { it is KtNamedFunction }

        lastFunction?.let {
            featureComponentClassPsi.addAfter(
                featureComponentClassBodyFactory.createViewModelFactoryMethod(
                    featureName
                ),
                lastFunction
            )
            featureComponentClassPsi.addAfter(
                featureComponentClassBodyFactory.createNewLine(),
                lastFunction
            )
            featureComponentClassPsi.addAfter(
                featureComponentClassBodyFactory.createNewLine(),
                lastFunction
            )
        }

        CodeStyleManager.getInstance(project).reformat(featureComponentPsi)
    }
}

private fun KtPsiFactory.createViewModelFactoryMethod(featureName: String) = createFunction(
    "abstract fun get${featureName}ViewModelFactory(): ${featureName}ViewModelFactory"
)