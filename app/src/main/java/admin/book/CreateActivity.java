package admin.book;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import control.book.Controller;
import ui.book.R;

public class CreateActivity extends Activity implements OnClickListener {

    private EditText name;
    private EditText author;
    private EditText date;

    private String book_name;
    private String book_author;
    private String book_date;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_create);

        name = (EditText) findViewById(R.id.create_name);
        author = (EditText) findViewById(R.id.create_author);
        date = (EditText) findViewById(R.id.create_date);

        findViewById(R.id.create).setOnClickListener(CreateActivity.this);

    }

    public void onClick(View view) {

        book_name = name.getText().toString();
        book_author = author.getText().toString();
        book_date = date.getText().toString();

        if (book_name.equals("") || book_author.equals("") || book_date.equals(""))
            Toast.makeText(CreateActivity.this,"图书信息不能为空",Toast.LENGTH_SHORT).show();
        else {
            // ----- 开启新线程打包图书信息 ----- /
            new Thread() {
                public void run() {
                    Controller controller = new Controller(handler);
                    controller.createBook(book_name, book_author, book_date);
                }
            }.start();
            // ----- 将输入框信息清空 ----- /
            name.setText("");
            author.setText("");
            date.setText("");
        }
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
