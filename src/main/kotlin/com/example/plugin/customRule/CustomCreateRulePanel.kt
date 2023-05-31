package com.example.plugin.customRule

import com.example.plugin.data.TypeFile
import com.example.plugin.util.constraintsLeft
import com.example.plugin.util.constraintsRight
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import java.awt.GridBagLayout
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class CustomCreateRulePanel : JPanel() {

    val nameTextField = JTextField()
    val templateFindErrorField = JTextField()
    val templateCorrectErrorField = JTextField()
    val messageRecommendationField = JTextField()

    val categoryComboBox = ComboBox<TypeFile>()

    var onCategoryIndexChanged: ((Int) -> Unit)? = null

    private val categoryList = listOf(TypeFile(0, "kotlin"), TypeFile(1, "xml"))

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(
            JPanel().apply {
                layout = GridBagLayout()
                add(JLabel("Name Rule:"), constraintsLeft(0, 0))
                add(nameTextField, constraintsRight(1, 0))
                add(JLabel("Type File:"), constraintsLeft(0, 1))
                if (categoryComboBox.itemCount == 0) {
                    categoryList.forEach { categoryComboBox.addItem(it) }
                }
                add(categoryComboBox, constraintsRight(1, 1))
                add(JLabel("Template String Find Error:"), constraintsLeft(0, 2))
                add(templateFindErrorField, constraintsRight(1, 2))
                add(JLabel("Template String Correct Error (may be empty):"), constraintsLeft(0, 3))
                add(templateCorrectErrorField, constraintsRight(1, 3))
                add(JLabel("Message Recommendation:"), constraintsLeft(0, 4))
                add(messageRecommendationField, constraintsRight(1, 4))
                categoryComboBox.addActionListener { onCategoryIndexChanged?.invoke(categoryComboBox.selectedIndex) }
            }
        )
    }

    override fun getPreferredSize() = Dimension(550, 130)
}
