package ui.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import control.book.Controller;

public class MainActivity extends Activity implements OnClickListener{

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        findViewById(R.id.m_create).setOnClickListener(MainActivity.this);
        findViewById(R.id.m_delete).setOnClickListener(MainActivity.this);
        findViewById(R.id.m_update).setOnClickListener(MainActivity.this);
        findViewById(R.id.m_retrieve).setOnClickListener(MainActivity.this);
    }

    public void onClick(View v){

        Intent intent =new Intent();

        switch(v.getId()) {
            case R.id.m_create:
                intent.setClass(MainActivity.this, CreateActivity.class);
                startActivity(intent);
                break;

            case R.id.m_delete:
                intent.setClass(MainActivity.this, DeleteActivity.class);
                startActivity(intent);
                break;

            case R.id.m_update:
                intent.setClass(MainActivity.this, UpdateActivity.class);
                startActivity(intent);
                break;

            case R.id.m_retrieve:
                intent.setClass(MainActivity.this, RetrieveActivity.class);
                startActivity(intent);
                break;
        }
    }

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