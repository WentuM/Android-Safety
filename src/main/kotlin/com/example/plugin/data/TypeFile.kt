package com.example.plugin.data

private const val DEFAULT_NAME = "UnnamedCategory"

data class TypeFile(
    var id: Int = 0,
    var name: String = ""
) {

    override fun toString() = name

    companion object {
        fun getDefault(id: Int) = TypeFile(id, DEFAULT_NAME)
    }
}