package ui.book;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import control.book.Controller;

public class UpdateActivity extends Activity implements OnClickListener{
    private EditText u_name;
    private EditText u_id;
    private EditText u_price;
    private String book_name;
    private String book_date;
    private String book_price;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update);

        u_name = (EditText) findViewById(R.id.update_name);
        u_id = (EditText) findViewById(R.id.update_id);
        u_price = (EditText) findViewById(R.id.update_price);
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
        book_name = u_name.getText().toString();
        book_date = u_id.getText().toString();
        book_price = u_price.getText().toString();

        if (book_name.equals("") || book_date.equals("") || book_price.equals(""))
            new Builder(UpdateActivity.this).setMessage("图书信息不能为空").show();
        else {
            new Thread() {
                public void run() {
                    Controller controller = new Controller(handler);
                    controller.updateBook(book_name, book_date, book_price);
                    u_name.setText("");
                    u_id.setText("");
                    u_price.setText("");
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
