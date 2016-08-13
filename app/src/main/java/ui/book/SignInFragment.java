package ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import control.book.Controller;

/**
 * 类名：SignInFragment
 * 作用：登录账号或切换到注册界面
 */
public class SignInFragment extends Fragment implements View.OnClickListener{

    RadioGroup radioGroup;
    EditText name,password;
    Boolean userType = false;
    String result;

    /**
     * 回调方法：onCreateView
     * 作用：加载布局，获得组件
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.log_in, container, false);

        view.findViewById(R.id.signIn).setOnClickListener(SignInFragment.this);
        view.findViewById(R.id.signUp).setOnClickListener(SignInFragment.this);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged( RadioGroup radioGroup, int checkId ) {
                userType = checkId != R.id.student;
            }
        });
        name = (EditText)view.findViewById(R.id.userName);
        password = (EditText)view.findViewById(R.id.userPassword);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //若点击了登录按钮
            case R.id.signIn:
                //若账号或密码为空
                if (name.getText().length() == 0 || password.getText().length() == 0)
                    Toast.makeText(getContext(), "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                else {
                    new Thread() {
                        public void run() {
                            Controller control = new Controller(handler);
                            control.checkSignInMessage(false, userType, name.getText().toString(), password.getText().toString());
                        }
                    }.start();
                }
                break;
            //若点击了注册按钮
            case R.id.signUp: {
                Intent intent = new Intent();
                intent.setClass(getContext(), SignUpActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            result = bundle.getString("result");
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            if (result.equals("登录成功")) {
                new Thread() {
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }.start();
            }
        }
    };

    public void onDestroy(){
        // ----- 向服务端发送退出信息，并释放资源 ----- //
        new Thread() {
            public void run() {
                Controller controller = new Controller();
                controller.exit();
            }
        }.start();
        super.onDestroy();
    }
}