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
        builder: (context, state) => const MainScreen(),
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
