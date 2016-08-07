package ui.book;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/** FragmentManager功能：管理三个fragment的显示和隐藏
 */
public class FragmentManager extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList<Fragment>fragmentList = new ArrayList<>();

        Fragment firstFragment = new TouristFragment();
        Fragment secondFragment = new BookFragment();
        Fragment thirdFragment = new AccountFragment();

        fragmentList.add(firstFragment);
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);
        viewPager.setAdapter( new FragmentAdapter( getSupportFragmentManager(), fragmentList ) );
        viewPager.setCurrentItem( 0 ); //设置当前显示标签页为第一页
    }
}