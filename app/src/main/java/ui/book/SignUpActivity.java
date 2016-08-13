package ui.book;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import control.book.Controller;

public class SignUpActivity extends Activity implements View.OnClickListener{

    private EditText name,code;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        name = (EditText) findViewById(R.id.newName);
        code = (EditText) findViewById(R.id.newCode);
        findViewById(R.id.newSignUp).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (name.getText().length()==0 || code.getText().length()==0){
            Toast.makeText(this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
        } else {
            new Thread() {
                public void run() {
                    Controller control = new Controller(handler);
                    control.checkSignUpMessage( true, false, name.getText().toString(), code.getText().toString());
                }
            }.start();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle=message.getData();
            result=bundle.getString("result");
            Toast.makeText(SignUpActivity.this,result,Toast.LENGTH_SHORT).show();
        }
    };
}
