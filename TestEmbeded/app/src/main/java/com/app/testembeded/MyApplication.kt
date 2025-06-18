package com.app.testembeded

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Instantiate a FlutterEngine.
        initFlutterEngine("flutter_engine_main", "/")
        initFlutterEngine("flutter_engine_second", "/second")

    }

    private fun initFlutterEngine(name: String, initRoute: String) {
        var flutterEngine = FlutterEngine(this)
        flutterEngine.navigationChannel.setInitialRoute(initRoute)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(name, flutterEngine)
    }
}