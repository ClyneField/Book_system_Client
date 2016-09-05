package ui.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import background.book.DataService;

/**
 * 类名：SplashScreen
 * 作用：startService，搜索本地数据库，访问服务端数据库
 */
public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //开启service
        Intent intent = new Intent(SplashScreen.this, DataService.class);
        startService(intent);

        new Thread() {
            @Override
            public void run() {
                try {
                    int waite = 0;
                    while( _active && ( waite < _splashTime ) ) {
                        sleep(100);
                        waite += 100;
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                    startActivity(new Intent("ui.book.FragmentManagement"));
                }
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }
}