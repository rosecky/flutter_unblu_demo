import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class UnbluView extends StatelessWidget {
  static const StandardMessageCodec _decoder = StandardMessageCodec();

  const UnbluView({super.key});

  @override
  Widget build(BuildContext context) {
    final Map<String, String> args = {};

    // Conditional platform check
    final uView = Platform.isAndroid
        ? AndroidView(
            viewType: 'UnbluVisitorView',
            creationParams: args,
            creationParamsCodec: _decoder)
        : UiKitView(
            viewType: 'UnbluVisitorView',
            creationParams: args,
            creationParamsCodec: _decoder);

    return SafeArea(child: uView);
  }
}
