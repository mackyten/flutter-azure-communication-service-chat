package AzureCommunicationServices;

import com.azure.android.communication.chat.ChatThreadClient;
import com.azure.android.communication.chat.models.*;

import AzureCommunicationServices.MyChatClient;

public class SendChatMessage {
    private final String accessToken;
    private final String chatThreadId;
    private final String content;
    private final String displayName;

    public SendChatMessage(String accessToken, String chatThreadId, String content, String displayName) {
        this.accessToken = accessToken;
        this.chatThreadId = chatThreadId;
        this.content = content;
        this.displayName = displayName;
    }

    public void handle() {

        MyChatClient myChatClient = new MyChatClient(this.accessToken);
        ChatThreadClient chatThreadClient = myChatClient.handle().getChatThreadClient(chatThreadId);

        SendChatMessageOptions sendChatMessageOptions = new SendChatMessageOptions()
                .setContent(this.content)
                .setType(ChatMessageType.TEXT)
                .setSenderDisplayName(this.displayName);

        chatThreadClient.sendMessage(sendChatMessageOptions);

    }
}
