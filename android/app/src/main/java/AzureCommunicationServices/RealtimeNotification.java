package AzureCommunicationServices;

import android.app.Activity;

import com.azure.android.communication.chat.ChatClient;
import com.azure.android.communication.chat.models.ChatEventType;
import com.azure.android.communication.chat.models.ChatMessageReceivedEvent;
import com.azure.android.communication.chat.models.ChatUserEvent;
import com.azure.android.communication.chat.models.RealTimeNotificationCallback;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class RealtimeNotification {
    final String token;
    final MethodChannel methodChannel;
    final Activity activity;

    public RealtimeNotification(String token, MethodChannel methodChannel, Activity activity) {
        this.token = token;
        this.methodChannel = methodChannel;
        this.activity = activity;
    }

    public void startRealtimeNotifications() {
        MyChatClient myChatClient = new MyChatClient(this.token);
        ChatClient chatClient = myChatClient.handle();

        chatClient.startRealtimeNotifications(null, throwable -> {
            if (throwable != null) {
                System.out.println("STARTING REALTIME NOTIFICATION ENCOUNTERED AN ERROR: "+ throwable.getMessage());
            }
        });

        RealTimeNotificationCallback callback = chatEvent -> {
            if (chatEvent instanceof ChatUserEvent){
                ChatUserEvent chatUserEvent = (ChatUserEvent) chatEvent;

                if(chatUserEvent instanceof ChatMessageReceivedEvent) {
                    ChatMessageReceivedEvent chatMessageReceivedEvent = (ChatMessageReceivedEvent) chatEvent;

                    Map<String, Object> chatData = new HashMap<>();

                    String content = chatMessageReceivedEvent.getContent();
                    String id = chatMessageReceivedEvent.getId();
                    String senderDisplayName = chatMessageReceivedEvent.getSenderDisplayName();
                    String createdOn = chatMessageReceivedEvent.getCreatedOn().toString();
                    String senderId = chatMessageReceivedEvent.getSender().getRawId();

                    chatData.put("content", content);
                    chatData.put("id", id);
                    chatData.put("senderDisplayName", senderDisplayName);
                    chatData.put("createdOn", createdOn);
                    chatData.put("senderId", senderId);

                    System.out.println("NEW MESSAGE HAS RECEIVED: " + chatData);

                    activity.runOnUiThread(() -> {
                        methodChannel.invokeMethod("onDataReceived", chatData);
                    });

                }
            }
        };
        chatClient.addEventHandler(ChatEventType.CHAT_MESSAGE_RECEIVED, callback);
    }
}
