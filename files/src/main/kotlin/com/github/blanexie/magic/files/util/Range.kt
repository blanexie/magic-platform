package com.github.blanexie.magic.files.util
data class Range(
        val status: Int,
        val headers: Map<String, *>,
        val bytes: ByteArray?,
)
