import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * Created by Administrator on 15/2/2017.
 */
public class PushServer {

    private int port;
    private SocketIOServer server;

    public PushServer(int port) {
        this.port = port;
    }

    public void start() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(this.port);

        server = new SocketIOServer(config);
        server.addEventListener("event", Message.class, new DataListener<Message>() {
            public void onData(SocketIOClient client, Message data, AckRequest ackRequest) {
                // broadcast messages to all clients
                server.getBroadcastOperations().sendEvent("event", data);
            }
        });

        server.start();
    }

    public void push(Counter counter) {
        server.getBroadcastOperations().sendEvent("conter", counter);
    }

    public static class Message {

    }
}
