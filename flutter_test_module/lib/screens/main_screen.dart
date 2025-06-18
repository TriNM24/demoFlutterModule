import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:go_router/go_router.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() {
    return _main_screenState();
  }
}

class _main_screenState extends State<MainScreen> {
  static const platform = MethodChannel('com.app.testembeded/data');
  String userId = '';
  String userName = '';
  Map<String, dynamic> complexData = {};

  String message = '';

  @override
  void initState() {
    super.initState();
    print("MainScreen initState");
    platform.setMethodCallHandler(_handleMethodCalls);
  }

  Future<dynamic> _handleMethodCalls(MethodCall call) async {
    switch (call.method) {
      case 'onActivityResumed':
        // This will be called every time the activity becomes visible
        final Map<String, dynamic> arguments = Map<String, dynamic>.from(
          call.arguments,
        );
        setState(() {
          userId = arguments['user_id'] ?? '';
          userName = arguments['user_name'] ?? '';
          // Parse complex data from JSON if needed
          if (arguments['complex_data'] != null) {
            complexData = json.decode(arguments['complex_data']);
          }
        });
        return true;
      default:
        return null;
    }
  }

  // Send data to Android
  Future<void> sendDataToAndroid(String message) async {
    try {
      // Example data to send back
      Map<String, dynamic> resultData = {
        'result_code': 200,
        'message': message,
        'user_data': {'id': 123, 'name': 'Updated Name'},
      };

      await platform.invokeMethod('sendDataToAndroid', resultData);
    } on PlatformException catch (e) {
      print("Failed to send data: ${e.message}");
    }
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Main Screen")),
      body: Column(
        children: [
          Text("Data from Android"),
          Text('User ID: $userId'),
          Text('User Name: $userName'),
          Text('Complex Data: $complexData'),
          ElevatedButton(
            onPressed: () {
              context.push('/second');
            },
            child: Text("Go to Second Screen"),
          ),
          SizedBox(height: 10),
          Padding(
            padding: const EdgeInsets.only(left: 20, right: 20),
            child: TextField(
              onChanged: (value) {
                message = value;
              },
              decoration: InputDecoration(
                labelText: 'Enter data to send to Android',
              ),
            ),
          ),
          ElevatedButton(
            onPressed: () {
              sendDataToAndroid(message);
            },
            child: Text("Send Data to Android"),
          ),
        ],
      ),
    );
  }
}
