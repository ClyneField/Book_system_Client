package ui.book;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.book.Controller;
import model.book.Book;
import model.book.BookList;
import model.book.Response;

/**
 * 类名：TouristFragment
 * 作用：启动网络线程并接收图书数据；
 * 可添加图书到BookshelfFragment；
 * 可浏览图书详细信息；
 */
public class TouristFragment extends Fragment implements AdapterView.OnItemClickListener {

    BookMessageAdapter bookMessageAdapter;
    ListView listView; //图书列表
    List<Map<String, Object>> bookListMap = new ArrayList<>(); //图书信息

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getList();
        // ----- 图书列表获取完毕，创建适配器 ----- //
        bookMessageAdapter = new BookMessageAdapter( context,
                bookListMap,
                R.layout.book_message_adapter,
                new String[]{"name", "author","date"},
                new int[]{R.id.bookTitle, R.id.bookAuthor, R.id.bookDate});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_message_listview, container, false);

        listView = (ListView) view.findViewById(R.id.list_tourist); //加载列表
        listView.setAdapter(bookMessageAdapter); //设置适配器
        listView.setOnItemClickListener(this); //注册监听器

        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查询");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // 清除ListView的过滤
                    listView.clearTextFilter();
                } else {
                    // 使用用户输入的内容对ListView的列表项进行过滤
                    listView.setFilterText(newText);
                }
                return true;
            }
        });

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BookMessageAdapter");
        intentFilter.addAction("BookMessageAdapterFailed");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("BookMessageAdapter"))
                    Toast.makeText(getContext(),"添加成功",Toast.LENGTH_SHORT).show();
                else if (intent.getAction().equals("BookMessageAdapterFailed"))
                    Toast.makeText(getContext(),"该图书已存在",Toast.LENGTH_SHORT).show();
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver,intentFilter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.setClass(getContext(), BookMessage.class);
        startActivity(intent);
    }

    public void getList() {
        // ----- 获取图书列表 ----- //
        new Thread() {
            public void run() {
                Controller control = new Controller(handler);
                control.retrieveBook();
            }
        }.start();
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

                bookListMap.add(book_map);
            }
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    };

    public void onDestroy(){
        // ----- 向服务端发送退出信息，并释放资源 ----- //
        new Thread() {
            public void run() {
                Controller controller = new Controller();
                controller.exit();
            }
        }.start();
        super.onDestroy();
    }
}