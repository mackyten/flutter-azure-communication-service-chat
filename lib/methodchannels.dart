import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:v3/users.dart';

class MethodChannels {
  static var platform = const MethodChannel("acs-android");

  static Future<void> createChatThread(String u2id, String u2Name) async {
    try {
      final arguments = {
        "u1id": Users.currentUser["id"],
        "u2id": u2id,
        "u1Name": Users.currentUser["name"],
        "u2Name": u2Name,
        "accessToken": Users.currentUser["token"]
      };
      await platform.invokeMethod('create-chat-thread', arguments);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  static Future<List<Map<String, dynamic>>?> getChatMessages(
      String chatThreadId) async {
    try {
      final arguments = {
        "chatThreadId": chatThreadId,
        "accessToken": Users.currentUser["token"]
      };
      final List<Object?> result =
          await platform.invokeMethod('get-chat-messages', arguments);

      final mapped = List<Map<Object?, Object?>>.from(result);

      List<Map<String, dynamic>> listOfMaps = mapped.map((item) {
        Map<String, dynamic> map = {};
        map["content"] = item["content"];
        map["id"] = item["id"];
        map["senderDisplayName"] = item["senderDisplayName"];
        map["createdOn"] = item["createdOn"];
        map["senderId"] = item["senderId"];
        return map;
      }).toList();

      return listOfMaps;
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
      return null;
    }
  }

  static Future<void> sendMessage(
      String chatThreadId, TextEditingController controller) async {
    try {
      final arguments = {
        "chatThreadId": chatThreadId,
        "accessToken": Users.currentUser["token"],
        "content": controller.text,
        "displayName": Users.currentUser["name"]
      };

      await platform.invokeMethod('send-chat-message', arguments);
      controller.clear();
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  static Future<List<Map<String, dynamic>>?> requestAllChatThreads() async {
    try {
      final arguments = {
        "accessToken": Users.currentUser["token"],
      };

      final List<Object?> result =
          await platform.invokeMethod('request-all-chat-threads', arguments);
      final mapped = List<Map<Object?, Object?>>.from(result);

      List<Map<String, dynamic>> listOfMaps = mapped.map((item) {
        Map<String, dynamic> map = {};
        map["participants"] = item["participants"];
        map["id"] = item["id"];
        map["topic"] = item["topic"];

        return map;
      }).toList();
      return listOfMaps;
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
        return null;
      }
    }
    return null;
  }

  static Future<void> realtimeNotification() async {
    try {
      final arguments = {"accessToken": Users.currentUser["token"]};
      await platform.invokeMethod('realtime-notification', arguments);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }
}
