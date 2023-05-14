package com.example.plugin.data

import com.intellij.ui.CollectionListModel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBList
import java.awt.Component
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.*

class RuleElementsPanel : JPanel() {

    private val listModel = CollectionListModel<String>()

    val jList = JBList<String>()

    init {
        jList.cellRenderer = CheckboxListCellRenderer()

        border = IdeBorderFactory.createTitledBorder("Choose Rules", false)
        layout = GridLayout(5, 1).apply {
            vgap = 2
        }

        addNewElement()
    }

    private fun addNewElement() {
//        jList.add(JCheckBox("number1"))
//        jList.add(JCheckBox("number1"))
//        jList.add(JCheckBox("number1"))
//        jList.add(JCheckBox("number1"))
//
//        add(jList)

        add(JCheckBox("number1"))
        add(JCheckBox("number1"))
        add(JCheckBox("number1"))
        add(JCheckBox("number1"))
        add(JCheckBox("number1"))
    }


    internal class CheckboxListCellRenderer : JCheckBox(), ListCellRenderer<Any?> {
        override fun getListCellRendererComponent(
            list: JList<*>, value: Any?, index: Int,
            isSelected: Boolean, cellHasFocus: Boolean
        ): Component {
            componentOrientation = list.componentOrientation
            font = list.font
            background = list.background
            foreground = list.foreground
            setSelected(isSelected)
            isEnabled = list.isEnabled
            text = value?.toString() ?: ""
            return this
        }
    }
}