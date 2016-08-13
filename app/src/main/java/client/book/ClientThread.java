package client.book;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import control.book.Controller;

/** ClientThread功能：*/
public class ClientThread extends Thread {
    private InputStream in = null;
    private OutputStream out = null;
    private static final int SIZE = 1024;
    private String message;
    private Controller controller;

    public ClientThread(Controller controller, String message) {
        // ----- 获取服务端IP和端口 ----- //
        Socket socket = Client.getSocket();
        // ----- 获取handler ----- //
        this.controller = controller;
        // ----- 获取打包信息 ----- //
        this.message = message;
        try {
            // ----- 打开输入输出流 ----- //
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // ----- 向服务端发送数据 ----- //
            send();
            // ----- 读取服务端的响应 ----- //
            byte[] buffer = new byte[SIZE];
            int length = in.read(buffer);
            // ----- 向客户端做出响应 ----- //
            String message = new String( buffer, 0, length, "UTF-8" );
            controller.doResponse(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send() {
        try {
            out.write(message.getBytes("GBK"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
