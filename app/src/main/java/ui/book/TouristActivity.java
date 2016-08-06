package ui.book;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class TouristActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentList = new ArrayList<>();

        Fragment firstFragment = new TouristFragment();
        Fragment secondFragment = new BookFragment();
        Fragment thirdFragment = new AccountFragment();

        fragmentList.add(firstFragment);
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);
        viewPager.setAdapter( new FragmentPageAdapter( getSupportFragmentManager(), fragmentList ) );
        viewPager.setCurrentItem( 0 );//设置当前显示标签页为第一页
    }
}