//package com.example.plugin.annotator
//
//// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
//
//import com.intellij.lexer.Lexer
//import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
//import com.intellij.openapi.editor.HighlighterColors
//import com.intellij.openapi.editor.colors.TextAttributesKey
//import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
//import com.intellij.psi.TokenType
//import com.intellij.psi.tree.IElementType
//import org.intellij.sdk.language.psi.SimpleTypes
//
//
//class SimpleSyntaxHighlighter : SyntaxHighlighterBase() {
//    override fun getHighlightingLexer(): Lexer {
//        return SimpleLexerAdapter()
//    }
//
//    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
//        if (tokenType == SimpleTypes.SEPARATOR) {
//            return SEPARATOR_KEYS
//        }
//        if (tokenType == SimpleTypes.KEY) {
//            return KEY_KEYS
//        }
//        if (tokenType == SimpleTypes.VALUE) {
//            return VALUE_KEYS
//        }
//        if (tokenType == SimpleTypes.COMMENT) {
//            return COMMENT_KEYS
//        }
//        return if (tokenType == TokenType.BAD_CHARACTER) {
//            BAD_CHAR_KEYS
//        } else EMPTY_KEYS
//    }
//
//    companion object {
//        val SEPARATOR = TextAttributesKey.createTextAttributesKey(
//            "SIMPLE_SEPARATOR",
//            DefaultLanguageHighlighterColors.OPERATION_SIGN
//        )
//        val KEY = TextAttributesKey.createTextAttributesKey("SIMPLE_KEY", DefaultLanguageHighlighterColors.KEYWORD)
//        val VALUE = TextAttributesKey.createTextAttributesKey("SIMPLE_VALUE", DefaultLanguageHighlighterColors.STRING)
//        val COMMENT =
//            TextAttributesKey.createTextAttributesKey("SIMPLE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
//        val BAD_CHARACTER =
//            TextAttributesKey.createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
//        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
//        private val SEPARATOR_KEYS = arrayOf(SEPARATOR)
//        private val KEY_KEYS = arrayOf(KEY)
//        private val VALUE_KEYS = arrayOf(VALUE)
//        private val COMMENT_KEYS = arrayOf(COMMENT)
//        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
//    }
//}
