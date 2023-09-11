import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:v3/mail_box.dart';
import 'package:v3/methodchannels.dart';

import 'users.dart';
import 'chat_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    MethodChannels.realtimeNotification();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
              onPressed: () {
                Navigator.of(context)
                    .push(MaterialPageRoute(builder: (_) => const MailBox()));
              },
              icon: const Icon(CupertinoIcons.mail_solid))
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: Users.contacts.length,
              itemBuilder: (context, index) {

                final contact = Users.contacts[index];
                final name = Users.currentUser["id"] == contact["id"]
                    ? "${contact["name"].toString()} (You)"
                    : contact["name"].toString();
                final id = contact["id"].toString();

                return ListTile(
                  title: Text(name),
                  subtitle: Text(
                    id,
                    style: const TextStyle(color: Colors.grey, fontSize: 9),
                  ),
                  onTap: () {
                    if (Users.currentUser["id"] != id) {
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (context) => ChatScreen(contact: contact),
                        ),
                      );
                    }
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
