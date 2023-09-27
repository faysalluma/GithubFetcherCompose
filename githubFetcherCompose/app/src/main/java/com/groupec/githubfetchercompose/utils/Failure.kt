package com.groupec.githubfetchercompose.utils

sealed class Failure {
    class NetworkFailure(val msg: String) : Failure()
    abstract class FeatureFailure : Failure()
    class SampleFeatureFailure: FeatureFailure()
}