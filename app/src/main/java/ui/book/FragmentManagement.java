package ui.book;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import background.book.DataService;
import client.book.ClientThread;
import control.book.Controller;

/**  类名：FragmentManager；
 *   功能：设置ViewPager；
 *   管理三个Fragment的显示和隐藏；
 */
public class FragmentManagement extends FragmentActivity {

    public DataService.GetBookListFromService getListFromService;
    public Fragment firstFragment,secondFragment,thirdFragment;

    /**
     * 类名：FragmentManagement
     * 方法名：serviceConnection
     * 作用：获得service实例
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            getListFromService = (DataService.GetBookListFromService)iBinder;
            Log.d("FragmentManagement", "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        // ----- 绑定Service ----- //
        Intent intent = new Intent(FragmentManagement.this, DataService.class);
        bindService( intent, serviceConnection, BIND_AUTO_CREATE );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ArrayList<Fragment> fragmentList = new ArrayList<>();
                firstFragment = new TouristFragment();
                secondFragment = new BookshelfFragment();
                thirdFragment = new SignInFragment();

                fragmentList.add(firstFragment);
                fragmentList.add(secondFragment);
                fragmentList.add(thirdFragment);

                FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);

                ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                viewPager.setAdapter(fragmentAdapter);
                viewPager.setCurrentItem(0); //设置首页
            }
        }, 1000);
    }

    public List<Map<String, Object>> getBookListFromServer() {
        return getListFromService.getBookListFromServer();
    }

    public List<Map<String, Object>> getBookListFromDatabase() {
        return getListFromService.getBookListFromDatabase();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onDestroy(){
        Log.d("FragmentManagement", "onDestroy: ");
        // ----- 向服务端发送退出信息，并释放资源 ----- //
        if ( !ClientThread.flag ) {
            new Thread() {
                public void run() {
                    Controller controller = new Controller();
                    controller.exit();
                }
            }.start();
        }
        unbindService(serviceConnection);
        stopService(new Intent(this,DataService.class));

        super.onDestroy();
    }
}