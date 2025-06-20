package com.myreactnativemodule

import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.demoreact.BuildConfig
import com.demoreact.MainActivity
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class NativeBridgeModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "NativeBridge"
    }

    // Hiển thị Toast từ React Native
    @ReactMethod
    fun showToast(message: String, duration: String = "SHORT") {
        val toastDuration = if (duration == "LONG") Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        
        reactContext.currentActivity?.runOnUiThread {
            Toast.makeText(reactContext, message, toastDuration).show()
        }
    }

    // Lấy thông tin thiết bị
    @ReactMethod
    fun getDeviceInfo(promise: Promise) {
        try {
            val deviceInfo = Arguments.createMap().apply {
                putString("brand", Build.BRAND)
                putString("model", Build.MODEL)
                putString("version", Build.VERSION.RELEASE)
                putInt("sdkVersion", Build.VERSION.SDK_INT)
                putString("manufacturer", Build.MANUFACTURER)
            }
            promise.resolve(deviceInfo)
        } catch (e: Exception) {
            promise.reject("DEVICE_INFO_ERROR", "Không thể lấy thông tin thiết bị: ${e.message}")
        }
    }

    // Mở MainActivity (Native Activity)
    @ReactMethod
    fun openNativeActivity(userName: String?, message: String?) {
        try {
            val intent = Intent(reactContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                userName?.let { putExtra("USER_NAME_FROM_RN", it) }
                message?.let { putExtra("MESSAGE_FROM_RN", it) }
                putExtra("TIMESTAMP_FROM_RN", System.currentTimeMillis())
            }
            reactContext.startActivity(intent)
        } catch (e: Exception) {
            showToast("Lỗi khi mở Native Activity: ${e.message}")
        }
    }

    // Callback cho React Native
    @ReactMethod
    fun showNativeDialog(title: String, message: String, callback: Callback) {
        try {
            reactContext.currentActivity?.runOnUiThread {
                android.app.AlertDialog.Builder(reactContext.currentActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK") { _, _ ->
                        callback.invoke("OK_PRESSED")
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        callback.invoke("CANCEL_PRESSED")
                    }
                    .show()
            }
        } catch (e: Exception) {
            callback.invoke("ERROR: ${e.message}")
        }
    }

    // Promise cho async operations
    @ReactMethod
    fun performAsyncTask(taskName: String, promise: Promise) {
        Thread {
            try {
                // Simulate some work
                Thread.sleep(2000)
                
                val result = Arguments.createMap().apply {
                    putString("taskName", taskName)
                    putString("status", "completed")
                    putDouble("timestamp", System.currentTimeMillis().toDouble())
                    putString("result", "Task '$taskName' đã hoàn thành thành công!")
                }
                promise.resolve(result)
            } catch (e: Exception) {
                promise.reject("ASYNC_TASK_ERROR", "Lỗi trong task: ${e.message}")
            }
        }.start()
    }

    // Gửi event đến React Native
    @ReactMethod
    fun startEventEmitter() {
        Thread {
            for (i in 1..5) {
                try {
                    Thread.sleep(1000)
                    val eventData = Arguments.createMap().apply {
                        putInt("count", i)
                        putString("message", "Event số $i từ Native")
                        putDouble("timestamp", System.currentTimeMillis().toDouble())
                    }
                    
                    reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit("NativeEvent", eventData)
                } catch (e: Exception) {
                    break
                }
            }
        }.start()
    }

    // Constants export
    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf(
            "PLATFORM" to "ANDROID",
            "MODULE_VERSION" to "1.0.0",
            "BUILD_TYPE" to if (BuildConfig.DEBUG) "DEBUG" else "RELEASE"
        )
    }
} 