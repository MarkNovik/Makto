@file:Suppress("unused")

package io

object OpenMode {
    const val READING = "r"
    const val READING_BINARY = "rb"
    const val WRITING = "w"
    const val WRITING_BINARY = "wb"
    const val APPEND = "a"
    const val APPEND_BINARY = "ab"
    const val READING_WRITING = "r+" // does not create or override existing file
    const val READING_WRITING_BINARY = "rb+"
    const val READING_WRITING_OVERRIDING = "w+" // creates file if not exists
    const val READING_WRITING_BINARY_OVERRIDING = "wb+"
    const val READING_APPENDING = "a+"
    const val READING_APPENDING_BINARY = "ab+"
}

