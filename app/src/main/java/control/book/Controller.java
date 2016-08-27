package control.book;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import client.book.Client;
import client.book.ClientThread;
import model.book.Book;
import model.book.BookList;
import util.book.Packager;
import util.book.Parser;
import java.io.IOException;
import java.io.OutputStream;
import model.book.Response;

/**
 * 类名：Controller
 * 功能：
 * 1、使用Packager打包图书/账户信息并开启网络线程；
 * 2、使用handler向UI线程传递服务端的响应信息；
 * 3、退出程序时向服务端发送退出信息。 */

public class Controller {

    private Handler handler;

    public Controller(){}

    public Controller(Handler handler){
        this.handler=handler;
    }

    /**
     * 类名：Controller
     * 方法名：createBook；retrieveBook；updateBook；deleteBook
     * 功能：打包数据并启动网络线程
      */
    public void createBook(String name, String id, String author) {
        // ----- 初始化Packager ----- //
        Packager packager=new Packager();
        // ----- 打包数据 ----- //
        String message=packager.createPackage(name,id,author);
        // ----- 启动网络线程 ----- //
        new ClientThread(Controller.this,message).start();
    }

    public void retrieveBook() {
        Packager packager=new Packager();
        String message=packager.retrievePackage();
        new ClientThread(Controller.this,message).start();
    }

    public void updateBook(String name, String id, String author) {
        Packager packager=new Packager();
        String message=packager.updatePackage(name, id, author);
        new ClientThread(Controller.this,message).start();
    }

    public void deleteBook(String name) {
        Packager packager=new Packager();
        String message=packager.deletePackage(name);
        new ClientThread(Controller.this,message).start();
    }

    /**
     * 类名：Controller
     * 方法名：doResponse
     * 功能：向客户端做出响应
      */
    public void doResponse(String message){
        // ----- 初始化解析器 ----- //
        Parser parser=new Parser();
        // ----- 解析操作结果 ----- //
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

    /**
     * 方法名：Controller
     * 类名：checkSignInMessage
     * 检测账号与密码是否正确
     * @param type
     * @param account
     * @param name
     * @param password
     */
    public void checkSignInMessage(Boolean type, Boolean account, String name, String password ){
        Packager packager = new Packager();
        String message = packager.signInPackage(type,account,name,password);
        new ClientThread(Controller.this,message).start();
    }

    public void checkSignUpMessage( Boolean type,Boolean account,String name, String password ){
        Packager packager = new Packager();
        String message = packager.signUpPackage(type,account,name,password);
        new ClientThread(Controller.this,message).start();
    }

    //启动数据库
    public BookList searchBook() {
        return BookList.getBookList();
    }

    /**
     * 类名：Controller
     * 方法名：addToDatabase
     * 作用：调用BookList.getBookListFromServer()方法，获得数据库图书信息
     * 调用BookList.insert(book)方法，插入该图书信息
     * @param name
     * @param author
     * @param date
     * @return
     */
    public boolean addToDatabase(String name, String author, String date) {

        BookList booklist = BookList.getBookList();
        Book book = new Book(name, author, date);
        if (booklist.insert(book))
            return true;
        else
            return false;
    }

    public boolean deleteFromDatabase(String name, String author, String date) {

        BookList booklist = BookList.getBookList();
        Book book = new Book(name, author, date);
        if (booklist.delete(book))
            return true;
        else
            return false;
    }

    /**
     * 类名：Controller
     * 方法名：exit
     * 作用：退出程序时调用
     */
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
