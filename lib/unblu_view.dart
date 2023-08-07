import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class UnbluView extends StatelessWidget {
  static const StandardMessageCodec _decoder = StandardMessageCodec();

  @override
  Widget build(BuildContext context) {
    final Map<String, String> args = {};
    return SafeArea(
        child: UiKitView(
            viewType: 'UnbluVisitorView',
            creationParams: args,
            creationParamsCodec: _decoder));
  }
}
