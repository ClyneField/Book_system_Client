package ui.book;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.Toast;

import control.book.Controller;

public class DeleteActivity extends Activity implements OnClickListener{

    private EditText d_name;
    private String book_name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete);

        d_name = (EditText) findViewById(R.id.delete_name);
        findViewById(R.id.delete).setOnClickListener(DeleteActivity.this);

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

        book_name = d_name.getText().toString();

        if (book_name.equals(""))
            Toast.makeText(DeleteActivity.this, "图书信息不能为空", Toast.LENGTH_SHORT).show();
        else {
            new Thread() {
                public void run() {
                    Controller controller = new Controller(handler);
                    controller.deleteBook(book_name);
                }
            }.start();
            d_name.setText("");
        }
    }

    private void buildDialog(String result) {
        Builder builder = new Builder(DeleteActivity.this);
        builder.setTitle(result);
        builder.setNegativeButton("返回首页", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {finish();
                }
            });
        builder.setPositiveButton("继续删除", null);
        builder.show();
    }
}
