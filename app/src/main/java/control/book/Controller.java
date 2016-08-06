package control.book;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import client.book.Client;
import client.book.ClientThread;
import util.book.Packager;
import util.book.Parser;
import java.io.IOException;
import java.io.OutputStream;
import model.book.Response;

/** Controller功能：
 * 1、使用Packager打包图书信息并开启网络线程；
 * 2、使用handler向UI线程传递服务端的响应信息；
 * 3、退出程序时向服务端发送退出信息。 */

public class Controller {

    private Handler handler;

    public Controller(){

    }

    public Controller(Handler handler){
        this.handler=handler;
    }

    // ----- 打包数据并启动网络线程 ----- //

    public void createBook(String name, String id, String price) {
        // ----- 初始化Packager ----- //
        Packager packager=new Packager();
        // ----- 打包数据 ----- //
        String message=packager.createPackage(name,id,price);
        // ----- 启动网络线程 ----- //
        new ClientThread(Controller.this,message).start();
    }

    public void retrieveBook() {
        Packager packager=new Packager();
        String message=packager.retrievePackage();
        new ClientThread(Controller.this,message).start();
    }

    public void deleteBook(String name) {
        Packager packager=new Packager();
        String message=packager.deletePackage(name);
        new ClientThread(Controller.this,message).start();
    }

    public void updateBook(String name, String id, String price) {
        Packager packager=new Packager();
        String message=packager.updatePackage(name, id, price);
        new ClientThread(Controller.this,message).start();
    }

    // ----- 向客户端做出响应 ----- //
    public void doResponse(String message){
        // ----- 初始化解析器 ----- //
        Parser parser=new Parser();
        Response response=parser.parserResponse(message);
        // ----- 获得操作结果 ----- //
        String result=response.getResult();
        // ----- 使用Bundle传递key为“result”的message ----- //
        Message message_of_response=new Message();
        Bundle bundle=new Bundle();
        bundle.putString("result",result);
        message_of_response.setData(bundle);
        handler.sendMessage(message_of_response);
    }

    // ----- 退出程序时调用 ----- //
    public void exit(){
        Packager packager=new Packager();
        String message=packager.exitPackage();
        OutputStream outputStream;
        try{
            outputStream= Client.getSocket().getOutputStream();
            outputStream.write(message.getBytes("GBK"));
            outputStream.flush();
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            Client.close();
        }
    }
}
