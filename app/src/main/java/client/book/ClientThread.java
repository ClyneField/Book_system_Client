package client.book;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import control.book.Controller;
import model.book.Book;
import model.book.BookList;
import util.book.Packager;

/** ClientThread功能：*/
public class ClientThread extends Thread {
    private InputStream in = null;
    private OutputStream out = null;
    private static final int SIZE = 1024;
    private String message;
    private Controller controller;
    public static boolean flag = false;

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
        } catch (Exception e) {
            flag = true;
            StringBuilder mes = new StringBuilder("");
            BookList bookList = BookList.getBookList();

            StringBuilder books = new StringBuilder("");
            Packager packager = new Packager();

            for (int i = 0; i < bookList.size(); ++i) {

                Book book = bookList.get(i);
                String str = packager.BookPackage(book.getName(),book.getAuthor(),book.getDate());
                if (i == bookList.size() - 1) {
                    books.append(str);
                } else {
                    books.append(str + "\n");
                }
            }
            mes.append("operate:" + "retrieve" + "\n");
            mes.append("content:" + books.toString() + "\n");
            mes.append("result:" + "网络连接错误" + "\n");
            this.controller.doResponse(mes.toString());
        }
    }

    public void run() {
        try {
            if (!flag) {
                // ----- 向服务端发送数据 ----- //
                send();
                // ----- 读取服务端的响应 ----- //
                byte[] buffer = new byte[SIZE];
                int length = in.read(buffer);
                // ----- 向客户端做出响应 ----- //
                String message = new String(buffer, 0, length, "UTF-8");
                controller.doResponse(message);
            }
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
