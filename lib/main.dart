
import 'package:flutter/material.dart';
import 'package:flutter_unblu_demo/pigeon.dart';
import 'package:flutter_unblu_demo/unblu_view.dart';

var unbluFlutterApi = UnbluFlutterToVisitorHostApi();

void main() async {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<bool>(
      future: unbluFlutterApi.createVisitor(),
      builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          if (snapshot.hasError) {
            // Handle the error appropriately.
            return Text('Error: ${snapshot.error}');
          }

          final Map<String, String> args = {};

          return const MaterialApp(
            home: Scaffold(
              body: Stack(
                children: <Widget>[UnbluView()],
              ),
            ),
          );
        } else {
          // While waiting for the future to complete, you can return a loader.
          return const CircularProgressIndicator();
        }
      },
    );
  }
}
/*}
;*/