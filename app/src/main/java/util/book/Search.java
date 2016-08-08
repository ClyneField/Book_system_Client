package util.book;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import model.book.Book;
import ui.book.R;

public class Search extends LinearLayout implements View.OnClickListener {
    /**
     * 输入框
     */
    private EditText textInput;

    /**
     * 删除键
     */
    private ImageView deleteView;

    /**
     * 上下文对象
     */
    private Context searchContext;

    /**
     * 弹出列表
     */
    private ListView popList;

    /**
     * 提示adapter
     */
    private ArrayAdapter<Book> hintAdapter;

    /**
     * 自动补全adapter
     */
    private ArrayAdapter<Book> autoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchListener callBack;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchListener(SearchListener listener) {
        callBack = listener;
    }

    /**
     * 构造方法，加载布局
     */
    public Search(Context context, AttributeSet attrs) {
        super(context, attrs);
        searchContext = context;
        LayoutInflater.from(context).inflate(R.layout.tourist, this);
        initViews();
    }

    /**
     * 初始化控件，设置监听器
     */
    private void initViews() {
        textInput = (EditText) findViewById(R.id.tourist_editText);
        deleteView = (ImageView) findViewById(R.id.tourist_delete);
        popList = (ListView) findViewById(R.id.list_tourist);

        deleteView.setOnClickListener(this);
        popList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                //set edit text
                String text = popList.getAdapter().getItem(i).toString();
                textInput.setText(text);
                textInput.setSelection(text.length());
                //hint list view gone and result list view show
                popList.setVisibility(View.GONE);
                notifyStartSearching();
            }
        });

        textInput.addTextChangedListener(new EditChangedListener());
        textInput.setOnClickListener(this);
        textInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    popList.setVisibility(GONE);
                    notifyStartSearching();
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     */
    private void notifyStartSearching(){

        if (callBack != null) {
            callBack.onSearch(textInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) searchContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput( 0, InputMethodManager.HIDE_NOT_ALWAYS );
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if ( !"".equals(charSequence.toString()) ) {
                deleteView.setVisibility(VISIBLE);
                popList.setVisibility(VISIBLE);
                if ( autoCompleteAdapter != null && popList.getAdapter() != autoCompleteAdapter ) {
                    popList.setAdapter(autoCompleteAdapter);
                }
                //更新autoComplete数据
                if (callBack != null) {
                    callBack.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                deleteView.setVisibility(GONE);
                if (hintAdapter != null) {
                    popList.setAdapter(hintAdapter);
                }
                popList.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchListener {
        /**
         * 更新自动补全内容
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);
        /**
         * 开始搜索
         * @param text 传入输入框的文本
         */
        void onSearch(String text);
        /**
         * 提示列表项点击时回调方法 (提示/自动补全)
         */
        void onTipsItemClick(String text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tourist_editText:
                popList.setVisibility(VISIBLE);
                break;
            case R.id.tourist_delete:
                textInput.setText("");
                deleteView.setVisibility(GONE);
                break;
            case R.id.tourist_back:
                ((Activity) searchContext).finish();
                break;
        }
    }
}