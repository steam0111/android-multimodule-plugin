package ru.hh.android.plugin.feature_module.wizard.step.choose_modules

import ru.hh.android.plugin.feature_module.core.BaseWizardStep
import ru.hh.android.plugin.feature_module.core.ui.custom_view.CheckBoxListView
import ru.hh.android.plugin.feature_module.wizard.PluginWizardModel
import ru.hh.android.plugin.feature_module.wizard.step.choose_modules.model.LibraryModuleDisplayableItem
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextPane


class ChooseModulesWizardStep(
        override val presenter: ChooseModulesPresenter
) : BaseWizardStep<PluginWizardModel, ChooseModulesView>(), ChooseModulesView {

    override lateinit var contentPanel: JPanel

    private lateinit var librariesList: CheckBoxListView<LibraryModuleDisplayableItem>
    private lateinit var libraryDescriptionArea: JTextPane
    private lateinit var enableAllButton: JButton
    private lateinit var disableAllButton: JButton


    override fun showList(displayableItems: List<LibraryModuleDisplayableItem>) {
        librariesList.setItems(displayableItems)
    }


    private fun createUIComponents() {
        librariesList = CheckBoxListView(
                onItemSelectedListener = { presenter.onLibraryItemSelected(it) },
                onItemToggleChangedListener = { presenter.onLibraryItemToggleChanged(it) }
        )
    }

}