import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 15/2/2017.
 */
public class PushServer {

    Logger log = LoggerFactory.getLogger(PushServer.class);

    private int port;
    private SocketIOServer server;

    public PushServer(int port) {
        this.port = port;
    }

    public void start() {
        Configuration config = new Configuration();
//        config.setHostname("mobile.img-fx.com");
//        config.setHostname("58.185.12.149");
//        config.setHostname("192.168.1.103");
//        config.setHostname("0.0.0.0");
        config.setPort(this.port);

        server = new SocketIOServer(config);

        server.start();

        System.out.println("\n[PushServer] Push Server instance started on port : " + this.port);
    }

    public void push(PushCounter counter) {
        log.debug("Pushing counter : " + counter.toString());
        server.getBroadcastOperations().sendEvent("counter", counter);
    }

}
