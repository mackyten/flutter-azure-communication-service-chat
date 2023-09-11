package com.example.v3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import AzureCommunicationServices.SendChatMessage;
import AzureCommunicationServices.GetAllChatThreads;
import AzureCommunicationServices.GetMessagesList;
import AzureCommunicationServices.RealtimeNotification;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import AzureCommunicationServices.CreateChatThread;
import android.os.Bundle;



public class MainActivity extends FlutterActivity{
    private static final String CHANNEL = "acs-android";
    private static MethodChannel methodChannel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        methodChannel = new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL);




        methodChannel.setMethodCallHandler(
                (call, result) -> {
                    Map<String, Object> arguments = call.arguments();

                    if (call.method.equals("test")) {


                    } else if (call.method.equals("create-chat-thread")) {

                        if (arguments == null) {
                            throw new IllegalArgumentException("Arguments cannot be empty");
                        }
                        String u1id = (String) arguments.get("u1id");
                        String u2id = (String) arguments.get("u2id");
                        String u1Name = (String) arguments.get("u1Name");
                        String u2Name = (String) arguments.get("u2Name");
                        String accessToken = (String) arguments.get("accessToken");

                        try {
                            CreateChatThread chatThreadId = new CreateChatThread(u1id, u2id, u1Name, u2Name, accessToken);
                            String newThreadId = chatThreadId.handle();
                            result.success(newThreadId);
                        } catch (Error e) {
                            result.error("ERROR Creating Chat thread: ", e.getMessage().toString(), null);
                        }

                    } else if (call.method.equals("get-chat-messages")) {
                        if (arguments == null) {
                            throw new IllegalArgumentException("Arguments cannot be empty");
                        }
                        String chatThreadId = (String) arguments.get("chatThreadId");
                        String accessToken = (String) arguments.get("accessToken");
                        try {
                            GetMessagesList messagesList = new GetMessagesList(chatThreadId, accessToken);
                            List<Map<String, String>> messages = messagesList.handle();
                            System.out.println(messages);
                            result.success(messages);
                        } catch (Error e) {
                            result.error("ERROR Creating Chat thread: ", e.getMessage().toString(), null);
                        }

                    } else if (call.method.equals("send-chat-message")) {
                        if (arguments == null) {
                            throw new IllegalArgumentException("Arguments cannot be empty");
                        }
                        String chatThreadId = (String) arguments.get("chatThreadId");
                        String accessToken = (String) arguments.get("accessToken");
                        String content = (String) arguments.get("content");
                        String displayName = (String) arguments.get("displayName");


                        SendChatMessage sendMessage =
                                new SendChatMessage(accessToken, chatThreadId, content, displayName);

                        sendMessage.handle();
                        result.success(true);


                    } else if (call.method.equals("request-all-chat-threads")) {
                        if (arguments == null) {
                            throw new IllegalArgumentException("Arguments cannot be empty");
                        }
                        String accessToken = (String) arguments.get("accessToken");
                        GetAllChatThreads requestAllChatThreads = new GetAllChatThreads(accessToken);
                        result.success(requestAllChatThreads.Handle());
                    } else if (call.method.equals("realtime-notification")) {

                        if (arguments == null) {
                            throw new IllegalArgumentException("Arguments cannot be empty");
                        }

                        String accessToken = (String) arguments.get("accessToken");

                        RealtimeNotification realtimeNotifications = new RealtimeNotification(accessToken, methodChannel, this);
                        realtimeNotifications.startRealtimeNotifications();
                        result.success(true);

                    } else {
                        result.notImplemented();
                    }
                }
        );

    }

    public void callFlutter() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("notify", 1);
        methodChannel.invokeMethod("updateCount", arguments);
    }



}



