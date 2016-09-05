package client.book;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import control.book.Controller;

public class Client {
    private static Socket socket;

    private Client() {
        try {
            socket = new Socket("172.29.36.242", 57783);
            socket.setSoTimeout(3000);
        } catch (SocketTimeoutException e1) {
            Log.d("Client", "Client: ");
        } catch (IOException e2) {
            e2.printStackTrace();
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
