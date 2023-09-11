package AzureCommunicationServices;

import com.azure.android.communication.chat.*;
import com.azure.android.communication.common.*;

public class MyChatClient {
    private final String accessToken;
    private final String endPoint = "https://com-service-demo.asiapacific.communication.azure.com/";

    public MyChatClient(String accessToken) {
        this.accessToken = accessToken;
    }

    public com.azure.android.communication.chat.ChatClient handle() {
        try {

            CommunicationTokenCredential userCredential = new CommunicationTokenCredential(this.accessToken);

            final ChatClientBuilder builder = new ChatClientBuilder();
            builder.endpoint(this.endPoint).credential(userCredential);
            return builder.buildClient();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }


    public String getEndPoint() {
        return endPoint;
    }
}
