package com.example.plugin.data

import com.intellij.ui.IdeBorderFactory
import java.awt.GridLayout
import javax.swing.*

class RuleElementsPanel(val ruleList: MutableList<RuleElement>, private val onCheckBoxClicked: () -> Unit) : JPanel() {

    val listModel = ruleList

    init {
        border = IdeBorderFactory.createTitledBorder("Choose Rules", false)
        layout = GridLayout(5, 1).apply {
            vgap = 2
        }

        addNewElement()
    }

    private fun listModelByDefault(): List<RuleElement> {
        val listModel = mutableListOf<RuleElement>().apply {
            add(RuleElement(0, "Rule 1 -", true))
            add(RuleElement(1, "Rule 2 -", true))
            add(RuleElement(2, "Rule 3 -", true))
            add(RuleElement(3, "Rule 4 -", true))
            add(RuleElement(4, "Rule 5 -", true))
        }
        return listModel
    }

    private fun addNewElement() {
        listModel.forEach { ruleElement ->
            add(JCheckBox(ruleElement.name, ruleElement.isSelected).apply {
                addActionListener {
                    ruleElement.isSelected = !ruleElement.isSelected
                    onCheckBoxClicked.invoke()
                }
            })
        }
    }


//    internal class CheckboxListCellRenderer : JCheckBox(), ListCellRenderer<Any?> {
//        override fun getListCellRendererComponent(
//            list: JList<*>, value: Any?, index: Int,
//            isSelected: Boolean, cellHasFocus: Boolean
//        ): Component {
//            componentOrientation = list.componentOrientation
//            font = list.font
//            background = list.background
//            foreground = list.foreground
//            setSelected(isSelected)
//            isEnabled = list.isEnabled
//            text = value?.toString() ?: ""
//            return this
//        }
//    }
}