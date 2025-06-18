import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

// Import your screen widgets
import 'screens/main_screen.dart';
import 'screens/second_screen.dart';

class AppRouter {
  static final GoRouter router = GoRouter(
    routes: [
      GoRoute(
        path: '/',
        name: 'main',
        builder: (context, state) {
          Map<String, dynamic> arguments = state.extra as Map<String, dynamic>;
          return MainScreen(
            userId: arguments['userId'],
            userName: arguments['userName'],
            complexData: json.decode(arguments['complexData']),
          );
        },
      ),
      GoRoute(
        path: '/second',
        name: 'second',
        builder: (context, state) => const SecondScreen(),
      ),
    ],
    errorBuilder:
        (context, state) => Scaffold(
          body: Center(child: Text('Error: Page ${state.path} not found')),
        ),
  );
}
