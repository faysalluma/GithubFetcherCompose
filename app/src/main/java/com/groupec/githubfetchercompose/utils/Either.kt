package com.groupec.githubfetchercompose.utils

sealed class Either<out L, out R> {
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    fun apply(f: (L) -> Any, g: (R) -> Any) {
        when (this) {
            is Left -> f(a)
            is Right -> g(b)
        }
    }
}

fun <L, R, T, U> Either<L, R>.map(f: (L) -> T, g: (R) -> U): Either<T, U> {
    return when (this) {
        is Either.Left -> Either.Left(f(a))
        is Either.Right -> Either.Right(g(b))
    }
}

fun <L, R> Either<L, R>.right(): R? {
    return when (this) {
        is Either.Right -> b
        is Either.Left -> null
    }
}

fun <L, R> Either<L, R>.left(): L? {
    return when (this) {
        is Either.Right -> null
        is Either.Left -> a
    }
}