package com.app.testembeded

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.testembeded.ui.theme.TestEmbededTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TestEmbededTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(bottom = 8.dp)
        )
        Button(onClick = {
            Toast.makeText(context, "Android start Flutter Main Screen", Toast.LENGTH_SHORT).show()
            val flutterIntent = MyFlutterActivity
                .withCachedEngine("flutter_engine_main")
                .build(context)

            /*var flutterIntent = MyFlutterActivity
                .withNewEngine()
                .initialRoute("/")
                .build(context)*/

            // Add data via intent extras
            flutterIntent.putExtra("userId", "12345")
            flutterIntent.putExtra("userName", "John Doe ${Date()}")
            flutterIntent.putExtra("complexData", "{\"key\":\"value\"}")
            flutterIntent.putExtra("routeName", "/")

            context.startActivity(flutterIntent)

        }) {
            Text(text = "Click Me to start Flutter Main Screen")
        }
        Button(
            modifier = Modifier.padding(top = 4.dp), onClick = {
                Toast.makeText(context, "Android start Flutter Second Screen", Toast.LENGTH_SHORT)
                    .show()
                val flutterIntent = MyFlutterActivity
                    .withCachedEngine("flutter_engine_main")
                    .build(context)

                flutterIntent.putExtra("routeName", "/second")
                context.startActivity(flutterIntent)
            }) {
            Text(text = "Click Me to start Flutter Second Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestEmbededTheme {
        Greeting("Android")
    }
}