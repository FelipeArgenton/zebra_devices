import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:zebra/zebra.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  String barcode = "Sem informações";
  final zebra = Zebra();

  @override
  void initState() {
    zebra.scanBarcode().listen((newBarcode) {
      setState(() {
        barcode = newBarcode;
      });
      print('Barcode: $barcode');
    });
    zebra.createProfile(profileName: 'Example Profile');
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Zebra Package Example'),
        ),
        body: Center(
          child: Text('Barcode: $barcode'),
        ),
      ),
    );
  }
}
