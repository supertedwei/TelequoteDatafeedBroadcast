import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * Created by tedwei on 2/15/17.
 */
public class Main {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3000);
        System.out.println("Socket IO server starting ...");

        final SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient client) {
                System.out.println("client : " + client);
            }
        });
        server.addEventListener("new message", EventData.class, new DataListener<EventData>() {
            public void onData(SocketIOClient client, EventData data, AckRequest ackRequest) {
                // broadcast messages to all clients
                System.out.println("new message : " + data);
                server.getBroadcastOperations().sendEvent("event", data);
            }
        });

        server.start();

        while(true) {
            server.getBroadcastOperations().sendEvent("event", "Test Event : " + System.currentTimeMillis());
            try {
                Thread.sleep(1000L);
                System.out.println("sending event: ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        try {
//            Thread.sleep(Integer.MAX_VALUE);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        server.stop();
    }
}
