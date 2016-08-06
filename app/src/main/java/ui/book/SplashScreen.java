package ui.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Thread() {
            @Override
            public void run() {
                try {
                    int waite = 0;
                    while( _active && ( waite < _splashTime ) ) {
                        sleep(100);
                        if( _active ) {
                            waite += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                    startActivity(new Intent("ui.book.FragmentManager"));
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