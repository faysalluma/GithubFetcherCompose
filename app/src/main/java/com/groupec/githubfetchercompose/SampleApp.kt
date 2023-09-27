package com.groupec.githubfetchercompose

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * Used for Hilt as root for dependency
 */
@HiltAndroidApp
class SampleApp: MultiDexApplication() {
    // nothing to do
}