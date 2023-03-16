package com.example.plugin.models

data class Module(
    val name: String,
    val nameWithoutPrefix: String
) {
    override fun toString() = nameWithoutPrefix
}