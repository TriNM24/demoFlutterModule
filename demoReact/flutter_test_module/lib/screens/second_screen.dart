import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:permission_handler/permission_handler.dart';

class SecondScreen extends StatefulWidget {
  const SecondScreen({super.key});

  @override
  State<SecondScreen> createState() {
    return _SecondScreenState();
  }
}

class _SecondScreenState extends State<SecondScreen> {
  String cameraPermission = "No permission";
  bool isPermissionGranted = false;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      checkCameraPermission();
    });
  }

  Future<void> checkCameraPermission() async {
    final status = await Permission.camera.status;
    setState(() {
      isPermissionGranted = status.isGranted;
      if (status.isGranted) {
        cameraPermission = "Permission granted";
      }
    });
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Second Screen")),
      body: SizedBox(
        width: double.infinity,
        child: Column(
          mainAxisSize: MainAxisSize.max,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text('Second Screen'),
            ElevatedButton(
              onPressed: () {
                //Navigator.pushNamed(context, '/');
                context.pop();
              },
              child: Text("Go to Main Screen"),
            ),
            ElevatedButton(
              onPressed:
                  isPermissionGranted
                      ? null
                      : () {
                        requestCameraPermission();
                      },
              child: Text("Request camera permission"),
            ),
            Text(cameraPermission),
          ],
        ),
      ),
    );
  }

  Future<void> requestCameraPermission() async {
    final status = await Permission.camera.request();
    if (status.isGranted) {
      // Permission granted, proceed with camera usage
      setState(() {
        cameraPermission = "Permission granted";
      });
    } else if (status.isDenied) {
      // Permission denied, handle accordingly (e.g., show an explanation)
      setState(() {
        cameraPermission = "Permission denied";
      });
    } else if (status.isPermanentlyDenied) {
      // Permission permanently denied, open app settings
      openAppSettings();
    }
  }
}
