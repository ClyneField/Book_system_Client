package ui.book;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import control.book.Controller;

public class CreateActivity extends Activity implements OnClickListener {

    private EditText c_name;
    private EditText c_id;
    private EditText c_price;
    private String book_name;
    private String book_date;
    private String book_price;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create);

        c_name = (EditText) findViewById(R.id.create_name);
        c_id = (EditText) findViewById(R.id.create_id);
        c_price = (EditText) findViewById(R.id.create_price);

        findViewById(R.id.create).setOnClickListener(CreateActivity.this);

    }

    // ----- 从Controller接收服务端的响应信息 ----- /
    private Handler handler=new Handler(){
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle=message.getData();
            String result=bundle.getString("result");
            buildDialog(result);
        }
    };

    public void onClick(View view) {

        book_name = c_name.getText().toString();
        book_date = c_id.getText().toString();
        book_price = c_price.getText().toString();

        if (book_name.equals("") || book_date.equals("") || book_price.equals(""))
            Toast.makeText(CreateActivity.this,"图书信息不能为空",Toast.LENGTH_SHORT).show();
        else {
            // ----- 开启新线程打包图书信息 ----- /
            new Thread() {
                public void run() {
                    Controller controller = new Controller(handler);
                    controller.createBook(book_name, book_date, book_price);
                }
            }.start();
            // ----- 将输入框信息清空 ----- /
            c_name.setText("");
            c_id.setText("");
            c_price.setText("");
        }
    }

    private void buildDialog(String result) {
        Builder builder = new Builder(CreateActivity.this);
        builder.setTitle(result);
        builder.setNegativeButton("返回首页", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.setPositiveButton("继续增加", null);
        builder.show();
    }
}
