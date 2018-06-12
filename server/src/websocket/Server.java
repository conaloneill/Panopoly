package websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends WebSocketServer {

    ArrayList<WebSocket> websockets = new ArrayList<WebSocket>();
    ArrayList<WebSocket> players = new ArrayList<WebSocket>();
    HashMap<String, WebSocket> map = new HashMap<String, WebSocket>();

    public Server(int port){
        super(new InetSocketAddress(port));
    }

    public Server(InetSocketAddress address) { super(address); }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }

    public static void main(String[] args) {
        try {
            Server server = new Server(12000);
            server.start();
            System.out.println("Server started on port: " + server.getPort());
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}