package AzureCommunicationServices;

import com.azure.android.communication.chat.ChatThreadClient;
import com.azure.android.communication.chat.models.ChatMessage;
import com.azure.android.core.rest.util.paging.PagedIterable;
import java.util.*;
import AzureCommunicationServices.MyChatClient;

public class GetMessagesList {
    private final String chatThreadId;
    private final String accessToken;

    public GetMessagesList(String chatThreadId, String accessToken) {
        this.chatThreadId = chatThreadId;
        this.accessToken = accessToken;
    }

    public List<Map<String, String>> handle() {
        MyChatClient myChatClient = new MyChatClient(this.accessToken);
        ChatThreadClient chatThreadClient = myChatClient.handle().getChatThreadClient(this.chatThreadId);

        List<Map<String, String>> chats = new ArrayList<>();

        try{

            PagedIterable<ChatMessage> listMessages = chatThreadClient.listMessages();
            listMessages.forEach(chatMessage -> {
                Map<String, String> chatMessageMap = new HashMap<>();
                chatMessageMap.put("content", chatMessage.getContent().getMessage().toString());
                chatMessageMap.put("id", chatMessage.getId());
                chatMessageMap.put("senderDisplayName", chatMessage.getSenderDisplayName());
                chatMessageMap.put("createdOn", chatMessage.getCreatedOn().toString());
                chatMessageMap.put("senderId", chatMessage.getSenderCommunicationIdentifier().getRawId());
                chats.add(chatMessageMap);

            });
            Thread.sleep(100);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return chats;
    }
}
