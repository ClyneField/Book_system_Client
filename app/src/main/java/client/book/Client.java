package client.book;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static Socket socket;

    private Client() {
        try {
            socket = new Socket("172.29.36.242", 57783);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        if (socket == null)
            new Client();
        return socket;
    }

    public static void close() {
        if (socket != null)
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
