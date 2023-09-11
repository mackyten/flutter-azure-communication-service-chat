package AzureCommunicationServices;

import com.azure.android.communication.chat.ChatThreadClient;
import com.azure.android.communication.chat.models.ChatParticipant;
import com.azure.android.communication.chat.models.ChatThreadItem;
import com.azure.android.core.rest.util.paging.PagedIterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllChatThreads {
    private final String accessToken;

    public GetAllChatThreads(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Map<String, Object>> Handle() {

        MyChatClient myChatClient = new MyChatClient(this.accessToken);

        PagedIterable<ChatThreadItem> chatThreads = myChatClient.handle().listChatThreads();
        List<Map<String, Object>> threads = new ArrayList<>();

        for (ChatThreadItem chatThreadItem : chatThreads) {
            String chatThreadId = chatThreadItem.getId();

            ChatThreadClient chatThreadClient = myChatClient.handle().getChatThreadClient(chatThreadId);
            PagedIterable<ChatParticipant> listParticipants = chatThreadClient.listParticipants();

            List<String> participantsName = new ArrayList<>();

            listParticipants.forEach(chatParticipant -> {
                participantsName.add(chatParticipant.getDisplayName());
            });

            Map<String, Object> threadsMap = new HashMap<>();

            threadsMap.put("id", chatThreadId);
            threadsMap.put("participants", participantsName);
            threadsMap.put("topic", chatThreadItem.getTopic());

            threads.add(threadsMap);

        }
        return threads;
    }
}
