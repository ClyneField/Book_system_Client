package ui.book;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.book.Controller;
import model.book.Book;
import model.book.BookList;

public class BookshelfFragment extends Fragment implements AdapterView.OnItemClickListener{

    BookshelfAdapter bookshelfAdapter;
    BroadcastReceiver broadcastReceiver;
    List<Map<String, Object>> bookListMap = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("BookshelfFragment", "onAttach: ");

        FragmentManagement fragmentManagement = (FragmentManagement) getActivity();
        bookListMap = fragmentManagement.getBookListFromDatabase();

        // ----- 图书列表获取完毕，创建适配器 ----- //
        bookshelfAdapter = new BookshelfAdapter( context,
                bookListMap,
                R.layout.bookshelf_adapter,
                new String[]{"name", "author","date"},
                new int[]{R.id.bookshelfTitle, R.id.bookshelfAuthor, R.id.bookshelfDate});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("BookshelfFragment", "onCreateView: ");
        View view = inflater.inflate(R.layout.bookshelf_listview, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_bookshelf); //加载列表
        listView.setAdapter(bookshelfAdapter); //设置适配器
        listView.setOnItemClickListener(this); //注册监听器

        Log.d("BookshelfFragment", "setOnItemClickListener ");
        SearchView searchView = (SearchView) view.findViewById(R.id.searchBookshelf);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查询");
        Log.d("BookshelfFragment", "searchView");
        new Thread() {
            public void run() {

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("BookMessageAdapterSuccess");
                intentFilter.addAction("BookshelfAdapterSuccess");
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals("BookMessageAdapterSuccess")) {
                            addList();
                            Log.d("BookshelfFragment", "onReceive: ");
                        } else if (intent.getAction().equals("BookshelfAdapterSuccess")) {
                            deleteList(intent.getStringExtra("name"));
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bookshelfAdapter.notifyDataSetChanged();
                            }
                        },100);
                        Log.d("BookshelfFragment", "notifyDataSetChanged:");
                    }
                };
                getContext().registerReceiver(broadcastReceiver, intentFilter);
                Log.d("BookshelfFragment", "run: ");
            }
        }.start();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.setClass(getContext(), BookMessage.class);
        startActivity(intent);
    }

    private void addList(){

        new Thread() {
            public void run() {
                Controller control = new Controller();
                BookList booklist = control.searchBook();

                Book book = booklist.get(booklist.size()-1);

                String name = book.getName();
                String date = book.getDate();
                String author = book.getAuthor();

                Map<String, Object> book_map = new HashMap<>();

                book_map.put("name", name);
                book_map.put("author", author);
                book_map.put("date", date);

                bookListMap.add(book_map);
            }
        }.start();
    }

    private void deleteList(String name){

        Log.d("BookshelfFragment", "deleteList: "+name);

        for (int i = 0; i < bookListMap.size(); ++i) {

            if ( bookListMap.get(i).get("name").toString().equals(name)) {
                bookListMap.remove(i);
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiver);
    }
}