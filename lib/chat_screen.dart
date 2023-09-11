import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:v3/string_helpers.dart';
import 'users.dart';
import 'methodchannels.dart';

class ChatScreen extends StatefulWidget {
  const ChatScreen({super.key, required this.contact});
  final Map<String, dynamic> contact;

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  final GlobalKey<AnimatedListState> _listKey = GlobalKey<AnimatedListState>();
  TextEditingController messageController = TextEditingController();
  String? threadId;
  bool isLoading = true;
  List<Map<String, dynamic>> messages = [];

  @override
  void initState() {
    getChatMessages();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    MethodChannels.platform.setMethodCallHandler((MethodCall call) {
      if (call.method == 'onDataReceived') {
        Map<String, dynamic> newMessage = Map<String, dynamic>.from(call
            .arguments);
        _listKey.currentState
            ?.insertItem(0, duration: const Duration(milliseconds: 500));

        setState(() {
          messages.insert(0, newMessage);
        });
      }

      return Future.error('Method not implemented');
    });

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.contact["name"]),
      ),
      body: Column(
        children: [
          if (isLoading)
            const Expanded(
              child: Center(
                child: CupertinoActivityIndicator(),
              ),
            )
          else
            Expanded(
              child: AnimatedList(
                key: _listKey,
                initialItemCount: messages.length,
                reverse: true,
                itemBuilder: (context, i, animation) {
                  final message = messages[i];
                  final content = message["content"].toString();
                  final senderId = message["senderId"].toString();
                  bool isMe = senderId == Users.currentUser["id"];
                  final timestamp =
                      twelveHourFormat(message["createdOn"].toString());
                  if (content.isNotEmpty) {
                    return FadeTransition(
                      opacity: animation,
                      child: Container(
                        margin: const EdgeInsets.all(8.0),
                        child: Align(
                          alignment: isMe
                              ? Alignment.centerRight
                              : Alignment.centerLeft,
                          child: Column(
                            crossAxisAlignment: isMe
                                ? CrossAxisAlignment.end
                                : CrossAxisAlignment.start,
                            children: [
                              Row(
                                mainAxisAlignment: isMe
                                    ? MainAxisAlignment.end
                                    : MainAxisAlignment.start,
                                crossAxisAlignment: CrossAxisAlignment.end,
                                children: [
                                  if (!isMe)
                                    const Icon(
                                      Icons.account_circle,
                                      size: 30,
                                      color: Colors.grey,
                                    ),
                                  Container(
                                    constraints: BoxConstraints(
                                      minWidth: 50.0,
                                      maxWidth:
                                          MediaQuery.of(context).size.width *
                                              0.7,
                                    ),
                                    padding: const EdgeInsets.all(8.0),
                                    decoration: BoxDecoration(
                                      color: isMe
                                          ? Colors.grey[300]
                                          : Colors.deepPurple[300],
                                      borderRadius: const BorderRadius.only(
                                        topLeft: Radius.circular(20.0),
                                        topRight: Radius.circular(20.0),
                                        bottomLeft: Radius.circular(20.0),
                                        bottomRight: Radius.circular(20.0),
                                      ),
                                    ),
                                    child: Text(
                                      content,
                                      style: TextStyle(
                                          color: isMe
                                              ? Colors.black
                                              : Colors.white),
                                    ),
                                  ),
                                ],
                              ),
                              Text(
                                timestamp,
                                style: const TextStyle(
                                    fontSize: 8, color: Colors.grey),
                              ),
                            ],
                          ),
                        ),
                      ),
                    );
                  } else {
                    return Container();
                  }
                },
              ),
            ),
          SafeArea(
            child: TextField(
              controller: messageController,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(50.0),
                  borderSide: const BorderSide(color: Colors.grey),
                ),
                hintText: 'Type your message here...',
                suffixIcon: IconButton(
                  onPressed: () {
                    if (messageController.text.isNotEmpty) {
                      MethodChannels.sendMessage(
                          widget.contact["chatThreadId"], messageController);
                    }
                  },
                  icon: const Icon(
                    CupertinoIcons.arrow_up_circle_fill,
                    size: 35,
                    color: Colors.purple,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Future<void> getChatMessages() async {
    final result =
        await MethodChannels.getChatMessages(widget.contact["chatThreadId"]);

    if (result != null) {
      setState(() {
        messages = result;
        isLoading = false;
      });
    }
    setState(() {
      isLoading = false;
    });
  }

  String dateFormatter(String date) {
    DateTime parsedDate = DateTime.parse(date).add(const Duration(hours: 8));
    String formattedDate =
        DateFormat('MMMM d, yyyy hh:mm:ss a').format(parsedDate);

    return (formattedDate);
  }
}
