import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test_module/main.dart';
import 'package:go_router/go_router.dart';

class MainScreen extends StatefulWidget {
  String userId = '';
  String userName = '';
  Map<String, dynamic> complexData = {};
  MainScreen({
    super.key,
    required this.userId,
    required this.userName,
    required this.complexData,
  });

  @override
  State<MainScreen> createState() {
    return _main_screenState();
  }
}

class _main_screenState extends State<MainScreen> {
  String message = '';

  @override
  void initState() {
    super.initState();
    print("MainScreen initState");
    //platform.setMethodCallHandler(_handleMethodCalls);
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

      await MyApp.platform.invokeMethod('sendDataToAndroid', resultData);
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
          Text('User ID: ${widget.userId}'),
          Text('User Name: ${widget.userName}'),
          Text('Complex Data: ${widget.complexData}'),
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
