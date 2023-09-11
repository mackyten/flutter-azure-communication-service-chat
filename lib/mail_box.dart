import 'package:flutter/material.dart';
import 'package:v3/methodchannels.dart';

class MailBox extends StatefulWidget {
  const MailBox({super.key});

  @override
  State<MailBox> createState() => _MailBoxState();
}

class _MailBoxState extends State<MailBox> {
  @override
  void initState() {
    getThreads();
    super.initState();
  }

  List<Map<String, dynamic>> threads = [];
  bool isLoading = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: isLoading
          ? const Center(
              child: CircularProgressIndicator(),
            )
          : threads.isEmpty
              ? const Center(
                  child: Text("You have no existing thread"),
                )
              : ListView.builder(
                  itemCount: threads.length,
                  itemBuilder: (context, index) {
                    final thread = threads[index];
                    final id = thread["id"];
                    final List<String> participants =
                        List<String>.from(thread["participants"]);
                    final topic = thread["topic"];

                    return ListTile(
                      title: Text(topic.toString()),
                      subtitle: Column(
                        children: [
                          Text(id.toString()),
                          ListView.builder(
                            shrinkWrap: true,
                            itemCount: participants.length,
                            itemBuilder: (context, participantIndex) {
                              return Text(participants[participantIndex]);
                            },
                          ),
                        ],
                      ),
                    );
                  },
                ),
    );
  }

  void getThreads() async {
    final result = await MethodChannels.requestAllChatThreads();

    if (result != null) {
      setState(() {
        threads = result;
      });
    }

    setState(() {
      isLoading = false;
    });
  }
}
