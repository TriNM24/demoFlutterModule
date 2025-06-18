import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'app_router.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  static const platform = MethodChannel('com.app.testembeded/data');
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    print("MyApp initState");
    MyApp.platform.setMethodCallHandler(_handleMethodCalls);
  }

  Future<dynamic> _handleMethodCalls(MethodCall call) async {
    switch (call.method) {
      case 'onActivityResumed':
        // This will be called every time the activity becomes visible
        final Map<String, dynamic> arguments = Map<String, dynamic>.from(
          call.arguments,
        );
        print("onActivityResumed: ${arguments['routeName']}");
        var routName = arguments['routeName'] ?? '/';
        AppRouter.router.go(routName, extra: arguments);
        return true;
      default:
        return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: "Flutter Demo",
      theme: ThemeData(primarySwatch: Colors.blue),
      routerConfig: AppRouter.router,
    );
  }
}
