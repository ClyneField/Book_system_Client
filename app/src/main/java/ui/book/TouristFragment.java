package ui.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import background.book.DataService;

/**
 * 类名：TouristFragment
 * 作用：
 * 启动网络线程并接收图书数据；
 * 可添加图书到BookshelfFragment；
 * 可浏览图书详细信息；
 */
public class TouristFragment extends Fragment implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    TouristAdapter touristAdapter;
    SearchView searchView;
    SwipeRefreshLayout mSwipeLayout;
    ListView listView; //图书列表
    List<Map<String, Object>> bookListMap = new ArrayList<>(); //图书信息

    @Override
    public void onAttach(Context context) {
        Log.d("TouristFragment", "onAttach: ");
        super.onAttach(context);

        FragmentManagement fragmentManagement = (FragmentManagement) getActivity();
        bookListMap = fragmentManagement.getBookListFromServer();

        // ----- 图书列表获取完毕，创建适配器 ----- //
        touristAdapter = new TouristAdapter(
                context,
                bookListMap,
                R.layout.book_message_adapter,
                new String[]{"name", "author", "date"},
                new int[]{R.id.bookTitle, R.id.bookAuthor, R.id.bookDate});
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("TouristFragment", "onCreateView: ");
        View view = inflater.inflate(R.layout.book_message_listview, container, false);

        listView = (ListView) view.findViewById(R.id.list_tourist); //加载列表
        listView.setAdapter(touristAdapter); //设置适配器
        listView.setOnItemClickListener(this); //注册监听器


        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_purple),
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查询");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // ----- 点击搜索按钮时，执行更新操作 ----- //
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (changeList(s))
                    touristAdapter.notifyDataSetChanged();
                else
                    Toast.makeText(getContext(),"该图书不存在",Toast.LENGTH_SHORT).show();
                searchView.setIconified(true);
                Log.d("searchView", "onQueryTextSubmit: ");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ( newText.length() == 0 ) {
                    // 清除ListView的过滤
                    listView.clearTextFilter();
                    Log.d("length == 0", "onQueryTextChange: ");
                } else {
                    // 使用用户输入的内容对ListView的列表项进行过滤
                    listView.setFilterText(newText);
                    Log.d("length != 0", "onQueryTextChange: ");
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.setClass(getContext(), BookMessage.class);
        startActivity(intent);
    }

    public boolean changeList (String name) {
        Iterator iterator = bookListMap.iterator();
        while (iterator.hasNext()) {
            HashMap map = (HashMap)iterator.next();
            if (map.get("name").equals(name)) {
                return true;
            } else
                iterator.remove();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d("TouristFragment", "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        Message message = handler.obtainMessage();
        message.what = 0;
        handler.sendMessage(message);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (obtainMessage().what == 0) {
                getContext().startService(new Intent(getActivity(), DataService.class));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManagement fragmentManagement = (FragmentManagement) getActivity();
                        bookListMap = fragmentManagement.getBookListFromServer();
                        touristAdapter.notifyDataSetChanged();
                        mSwipeLayout.setRefreshing(false);
                    }
                },100);

                Log.d("TouristFragment", "handleMessage: ");
            }
        }
    };
}