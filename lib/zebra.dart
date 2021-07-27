
import 'dart:async';

import 'package:flutter/services.dart';

class Zebra {
  static const _methodChannelName = 'br.com.srssistemas/zebra';
  static const  _channel = const MethodChannel(_methodChannelName);

  static const _scanChannelName = "br.com.srssistemas/scan";
  static const _scanChannel = const EventChannel(_scanChannelName);

  const Zebra();

  Future<void> createProfile({required String profileName}) async {
    await _channel.invokeMethod('createProfile', profileName);
  }

  Stream scanBarcode(){
    return _scanChannel.receiveBroadcastStream();
  }

}
