package com.groupec.githubfetchercompose.utils

fun String.getError(): Error? {
    return when(this) {
        Error.BACK_END_UNKNOWN.title -> Error.BACK_END_UNKNOWN
        else -> null
    }
}

fun String.getPackageValue(): String? {
    return when(this) {
        Packages.DEMO.toString() -> Packages.DEMO.value
        Packages.RECETTE.toString() -> Packages.RECETTE.value
        Packages.PROD.toString() -> Packages.PROD.value
        else -> null
    }
}