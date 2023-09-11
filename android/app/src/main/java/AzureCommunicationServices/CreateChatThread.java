package AzureCommunicationServices;

import com.azure.android.communication.chat.models.*;
import com.azure.android.communication.common.*;

import java.util.ArrayList;
import java.util.List;

import AzureCommunicationServices.MyChatClient;

public class CreateChatThread {
    private final String u1id;
    private final String u2Id;
    private final String u1Name;
    private  final String u2Name;
    private final String accessToken;


    public CreateChatThread(String u1id, String u2Id, String u1Name, String u2Name, String accessToken) {
        this.u1id = u1id;
        this.u2Id = u2Id;
        this.u1Name = u1Name;
        this.u2Name = u2Name;
        this.accessToken = accessToken;
    }

    public String handle (){
        MyChatClient myChatClient = new MyChatClient(this.accessToken);

        List<ChatParticipant> participantList = new ArrayList<>();

        participantList.add(
                new ChatParticipant()
                        .setCommunicationIdentifier(new CommunicationUserIdentifier(u1id))
                        .setDisplayName(u1Name)
        );

        participantList.add(
                new ChatParticipant()
                        .setCommunicationIdentifier(new CommunicationUserIdentifier(u2Id))
                        .setDisplayName(u2Name)
        );

        CreateChatThreadOptions createChatThreadOptions = new CreateChatThreadOptions()
                .setTopic(u1Name + " & " + u2Name)
                .setParticipants(participantList);

        CreateChatThreadResult result = myChatClient.handle().createChatThread(createChatThreadOptions);

        return result.getChatThreadProperties().getId();
    }
}
