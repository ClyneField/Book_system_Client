package ui.book;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import util.book.Search;

/**
 * 类名：TouristFragment
 * 功能：启动网络线程并接收图书数据；
 * 可添加图书到BookshelfFragment；
 * 可浏览图书详细信息；
 */
public class TouristFragment extends Fragment {

    BookAdapter bookAdapter;
    List<Map<String, Object>> list_item = new ArrayList<>();
    ListView listView;
    String result;
    SearchView searchView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getList();
        // ----- 图书列表获取完毕，创建适配器 ----- //
        bookAdapter = new BookAdapter( getActivity(),
                list_item,
                R.layout.adapter,
                new String[]{"name", "id", "price"},
                new int[]{R.id.tourist_name, R.id.tourist_id, R.id.tourist_price});
        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tourist, container, false);
        listView = (ListView) view.findViewById(R.id.list_tourist);
        listView.setAdapter(bookAdapter);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查询");

        return view;
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
            result = bundle.getString("result");
            // ----- 初始化Response ----- //
            Response response = Response.getResponse();
            // ----- 获得Response当中的图书列表 ----- //
            BookList bookList = response.getBookList();

            for (int i = 0; i < bookList.size(); i++) {
                // ----- 获得图书列表的一本图书信息 ----- //
                Book book = bookList.get(i);

                String name = book.getName();
                String id = book.getId();
                String price = book.getPrice();

                Map<String, Object> book_map = new HashMap<>();

                book_map.put("name", name);
                book_map.put("id", id);
                book_map.put("price", price);

                list_item.add(book_map);
            }
        }
    };
}