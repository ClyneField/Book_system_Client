package ui.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    List<Map<String, Object>> bookListMap = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getList();
        // ----- 图书列表获取完毕，创建适配器 ----- //
        bookshelfAdapter = new BookshelfAdapter( context,
                bookListMap,
                R.layout.bookshelf_adapter,
                new String[]{"name", "author","date"},
                new int[]{R.id.bookshelfTitle, R.id.bookshelfAuthor, R.id.bookshelfDate});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bookshelf_listview, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_bookshelf); //加载列表
        listView.setAdapter(bookshelfAdapter); //设置适配器
        listView.setOnItemClickListener(this); //注册监听器

        SearchView searchView = (SearchView) view.findViewById(R.id.searchBookshelf);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查询");

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
                Controller control = new Controller();
                BookList booklist = control.searchBook();
                setList(booklist);
            }
        }.start();
    }

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

            bookListMap.add(book_map);
        }
    }
}