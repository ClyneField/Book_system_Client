package ui.book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import control.book.Controller;
import admin.book.MainActivity;
import student.book.StudentActivity;

/**
 * 类名：SignInFragment
 * 作用：登录账号或切换到注册界面
 */
public class SignInFragment extends Fragment implements View.OnClickListener{

    Boolean userType = false; //true代表admin，false代表student
    CheckBox checkBox;
    EditText name,password;
    RadioGroup radioGroup;
    String result;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

        View view = inflater.inflate(R.layout.sign_in, container, false);

        Log.d("SignInFragment", "onCreateView: ");
        view.findViewById(R.id.signIn).setOnClickListener(SignInFragment.this);
        view.findViewById(R.id.signUp).setOnClickListener(SignInFragment.this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        name = (EditText)view.findViewById(R.id.userName);
        password = (EditText)view.findViewById(R.id.userPassword);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged( RadioGroup radioGroup, int checkId ) {
                userType = checkId != R.id.student;
            }
        });

        checkBox = (CheckBox)view.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor = sharedPreferences.edit();
                editor.putBoolean("remember_password",b);
                editor.apply();
            }
        });

        Boolean isRemember = sharedPreferences.getBoolean("remember_password",false);
        if (isRemember) {
            String account = sharedPreferences.getString("account","");
            String password = sharedPreferences.getString("password","");
            this.name.setText(account);
            this.password.setText(password);
            checkBox.setChecked(true);
        }

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
                    editor = sharedPreferences.edit();
                    if (checkBox.isChecked()) {
                        editor.putBoolean("remember_password",true);
                        editor.putString("account", name.getText().toString());
                        editor.putString("password", password.getText().toString());
                    } else {
                        editor.clear();
                    }
                    new Thread() {
                        public void run() {
                            Controller control = new Controller(handler);
                            control.checkSignInMessage(false, userType, name.getText().toString(), password.getText().toString());
                        }
                    }.start();
                    editor.apply();
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
                if (userType) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), StudentActivity.class);
                    startActivity(intent);
                }
            }
        }
    };
}