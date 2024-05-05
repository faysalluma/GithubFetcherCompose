package com.groupec.githubfetchercompose

import androidx.multidex.MultiDexApplication
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp

/**
 * Used for Hilt as root for dependency
 */
@HiltAndroidApp
class SampleApp: MultiDexApplication() {
    // nothing to do
    override fun onCreate() {
        super.onCreate()
        initFlipper()
    }

    private fun initFlipper () {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            // Setup Flipper
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))

            // Add Network plugin
            val networkPlugin = NetworkFlipperPlugin()
            client.addPlugin(networkPlugin)

            // Add Navigation plugin
            client.addPlugin(NavigationFlipperPlugin.getInstance())

            // Start client
            client.start()
        }
    }
}