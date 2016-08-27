package background.book;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.book.Controller;
import model.book.Book;
import model.book.BookList;
import model.book.Response;
import ui.book.SplashScreen;
import util.book.DatabaseConnection;

public class DataService extends Service{

    GetBookListFromService getBookListFromService = new GetBookListFromService();
    List<Map<String, Object>> bookListFromDatabase = new ArrayList<>();
    List<Map<String, Object>> bookListFromServer = new ArrayList<>();

    public class GetBookListFromService extends Binder {

        public List<Map<String, Object>> getBookListFromServer() {
            Log.d("DatabaseService", "getBookListFromServer: ");
            return bookListFromServer;
        }

        public List<Map<String, Object>> getBookListFromDatabase() {
            Log.d("DatabaseService", "getBookListFromDatabase: ");
            return bookListFromDatabase;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("DataService", "onBind: ");
        return getBookListFromService;
    }

    @Override
    public void onCreate() {
        DatabaseConnection.setContext(getApplicationContext());

        // ----- 获取图书列表 ----- //
        new Thread() {
            public void run() {
                Controller control = new Controller(handler);
                control.retrieveBook();

                BookList booklist = control.searchBook();
                setList(booklist);
            }
        }.start();

        Log.d("DataService", "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("DataService", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    // ----- 获得Controller返回的message ----- //
    Handler handler = new Handler() {

        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String result = bundle.getString("result");
            // ----- 初始化Response ----- //
            Response response = Response.getResponse();
            // ----- 获得Response当中的图书列表 ----- //
            BookList bookList = response.getBookList();

            for (int i = 0; i < bookList.size(); i++) {
                // ----- 获得图书列表的一本图书信息 ----- //
                Book book = bookList.get(i);

                String name = book.getName();
                String date = book.getDate();
                String author = book.getAuthor();

                Map<String, Object> book_map = new HashMap<>();

                book_map.put("name", name);
                book_map.put("author", author);
                book_map.put("date", date);

                bookListFromServer.add(book_map);
                Log.d("DataService", "handleMessage: ");
            }
        }
    };

    // ----- 获得Controller返回的bookList ----- //
    private void setList(BookList booklist) {

        for (int i = 0; i < booklist.size(); ++i) {

            Book book = booklist.get(i);

            String name = book.getName();
            String date = book.getDate();
            String author = book.getAuthor();

            Map<String, Object> book_map = new HashMap<>();

            book_map.put("name", name);
            book_map.put("author", author);
            book_map.put("date", date);

            bookListFromServer.add(book_map);
        }
    }
}
