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

public class UpdateActivity extends Activity implements OnClickListener{
    private EditText name;
    private EditText author;
    private EditText date;
    
    private String book_name;
    private String book_date;
    private String book_price;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_update);

        name = (EditText) findViewById(R.id.update_name);
        author = (EditText) findViewById(R.id.update_id);
        date = (EditText) findViewById(R.id.update_price);
        findViewById(R.id.update).setOnClickListener(UpdateActivity.this);

    }

    private Handler handler=new Handler(){
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle=message.getData();
            String result=bundle.getString("result");
            buildDialog(result);
        }
    };

    public void onClick(View view) {
        book_name = name.getText().toString();
        book_date = author.getText().toString();
        book_price = date.getText().toString();

        if (book_name.equals("") || book_date.equals("") || book_price.equals(""))
            Toast.makeText(UpdateActivity.this,"图书信息不能为空",Toast.LENGTH_SHORT).show();
        else {
            new Thread() {
                public void run() {
                    Controller controller = new Controller(handler);
                    controller.updateBook(book_name, book_date, book_price);

                    name.setText("");
                    author.setText("");
                    date.setText("");
                }
            }.start();
        }
    }

    private void buildDialog(String result) {
        Builder builder = new Builder(UpdateActivity.this);
        builder.setTitle(result);
        builder.setNegativeButton("返回首页", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.setPositiveButton("继续修改", null);
        builder.show();
    }
}
