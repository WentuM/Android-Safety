package com.example.plugin.data

import com.example.plugin.util.constraintsLeft
import com.example.plugin.util.constraintsRight
import com.intellij.ui.IdeBorderFactory
import java.awt.Dimension
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.*

class RuleElementsPanel(val ruleList: MutableList<RuleElement>, private val onCheckBoxClicked: () -> Unit) : JPanel() {

    val listModel = ruleList

    init {
        border = IdeBorderFactory.createTitledBorder("Choose Rules", false)
        layout = GridBagLayout()

        addNewElement()
    }

    private fun listModelByDefault(): List<RuleElement> {
        val listModel = mutableListOf<RuleElement>().apply {
            add(RuleElement(0, "Rule 1 - ", true))
            add(RuleElement(1, "Rule 2 -", true))
            add(RuleElement(2, "Rule 3 -", true))
            add(RuleElement(3, "Rule 4 -", true))
            add(RuleElement(4, "Rule 5 -", true))
        }
        return listModel
    }

    private fun addNewElement() {
        var y = 0
        listModel.forEach { ruleElement ->
            val currentJButton = JButton("Delete")
            val currentJCheckBox = JCheckBox(ruleElement.name, ruleElement.isSelected)

            currentJButton.addActionListener {
                remove(currentJButton)
                remove(currentJCheckBox)
                listModel.remove(ruleElement)
            }

            add(currentJButton, constraintsLeft(0, y))
            add(currentJCheckBox.apply {
                addActionListener {
                    ruleElement.isSelected = !ruleElement.isSelected
                    onCheckBoxClicked.invoke()
                }
            }, constraintsRight(1, y))
            y++
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