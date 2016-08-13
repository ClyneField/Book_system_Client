package ui.book;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import util.book.DatabaseConnection;

/**  类名：FragmentManager；
 *   功能：设置ViewPager；
 *   管理三个Fragment的显示和隐藏；
 *
 */
public class FragmentManager extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        DatabaseConnection.setContext(this.getApplicationContext());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList<Fragment>fragmentList = new ArrayList<>();

        Fragment firstFragment = new TouristFragment();
        Fragment secondFragment = new BookshelfFragment();
        Fragment thirdFragment = new SignInFragment();

        fragmentList.add(firstFragment);
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);
        viewPager.setAdapter( new FragmentAdapter( getSupportFragmentManager(), fragmentList ) );
        viewPager.setCurrentItem( 0 ); //设置首页
    }
}