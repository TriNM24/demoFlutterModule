package com.demoreact

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MyFlutterActivity : FlutterActivity() {
    private val CHANNEL = "com.app.testembeded/data"

    companion object {
        // Static method to create a builder for MyFlutterActivity with cached engine
        fun withCachedEngine(engineId: String): CachedEngineIntentBuilder {
            return CachedEngineIntentBuilder(MyFlutterActivity::class.java, engineId)
        }
        // Add this method for new engine
        fun withNewEngine(): NewEngineIntentBuilder {
            return NewEngineIntentBuilder(MyFlutterActivity::class.java)
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Override the method channel handler to use the intent extras from this activity
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "getInitialData" -> {
                    val data = HashMap<String, Any>()
                    intent?.extras?.let { bundle ->
                        bundle.getString("user_id")?.let { userId ->
                            data["user_id"] = userId
                        }
                        bundle.getString("user_name")?.let { userName ->
                            data["user_name"] = userName
                        }
                        bundle.getString("complex_data")?.let { complexData ->
                            data["complex_data"] = complexData
                        }
                    }
                    // Send the data back to Flutter
                    result.success(data)
                }

                "sendDataToAndroid" -> {
                    // Get data from Flutter
                    val resultData = call.arguments as? Map<*, *>

                    // Process the received data
                    handleDataFromFlutter(resultData)

                    // Send a success response back to Flutter
                    result.success(true)
                }

                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun handleDataFromFlutter(data: Map<*, *>?) {
        data?.let {
            val resultCode = (it["result_code"] as? Number)?.toInt() ?: 0
            val message = it["message"] as? String ?: ""
            val userData = it["user_data"] as? Map<*, *>

            // Do something with the data
            Toast.makeText(this, "Received from Flutter: $message", Toast.LENGTH_SHORT).show()

            // If you want to pass this data back to MainActivity
            val resultIntent = Intent()
            resultIntent.putExtra("RESULT_CODE", resultCode)
            resultIntent.putExtra("MESSAGE", message)

            // Set result and finish activity
            setResult(RESULT_OK, resultIntent)

            // Optionally finish the activity if needed
            // finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Notify Flutter that the activity is visible
        flutterEngine?.dartExecutor?.binaryMessenger?.let {
            MethodChannel(it, CHANNEL)
                .invokeMethod("onActivityResumed", intent.extras?.let { bundleToMap(it) })
        }
    }

    // Add this helper method
    private fun bundleToMap(bundle: Bundle): Map<String, Any?> {
        /*val map = HashMap<String, Any?>()
        for (key in bundle.keySet()) {
            map[key] = bundle.get(key)
        }
        return map*/
        val map = HashMap<String, Any?>()
        for (key in bundle.keySet()) {
            when {
                bundle.containsKey(key) -> {
                    when (bundle.get(key)?.javaClass) {
                        Int::class.java -> map[key] = bundle.getInt(key)
                        Long::class.java -> map[key] = bundle.getLong(key)
                        Float::class.java -> map[key] = bundle.getFloat(key)
                        Double::class.java -> map[key] = bundle.getDouble(key)
                        Boolean::class.java -> map[key] = bundle.getBoolean(key)
                        String::class.java -> map[key] = bundle.getString(key)
                        Bundle::class.java -> map[key] = bundleToMap(bundle.getBundle(key) ?: Bundle())
                        Array<String>::class.java -> map[key] = bundle.getStringArray(key)
                        IntArray::class.java -> map[key] = bundle.getIntArray(key)
                        else -> {
                            try {
                                // Try to get the value as a parcelable or serializable
                                map[key] = bundle.get(key)
                            } catch (e: Exception) {
                                // If all else fails, just put null
                                map[key] = null
                            }
                        }
                    }
                }
            }
        }
        return map
    }
}
