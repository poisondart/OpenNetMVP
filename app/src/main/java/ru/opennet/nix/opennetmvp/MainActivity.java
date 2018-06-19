package ru.opennet.nix.opennetmvp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.news.NewsFragment;
import ru.opennet.nix.opennetmvp.utils.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private final int[] mIconsIDs = {R.drawable.ic_news_tab_icon,
            R.drawable.ic_favs_tab_icon,
            R.drawable.ic_settings_tab_icon};

    private final int[] mIconsIDsPressed = {R.drawable.ic_news_tab_pressed_icon,
            R.drawable.ic_favs_tab_pressed_icon,
            R.drawable.ic_settings_tab_pressed_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupViewPager();
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(mViewPager);
                mTabLayout.getTabAt(0).setIcon(mIconsIDsPressed[0]);
                mTabLayout.getTabAt(1).setIcon(mIconsIDs[1]);
                mTabLayout.getTabAt(2).setIcon(mIconsIDs[2]);
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setupSelectedTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setupUnselectedTab(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //do nothing
            }
        });
    }


    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NewsFragment());
        viewPagerAdapter.addFragment(new TestFragment());
        viewPagerAdapter.addFragment(new TestFragment());
        mViewPager.setAdapter(viewPagerAdapter);
    }

    private void setupSelectedTab(int id){
        switch (id){
            case 0:
                mTabLayout.getTabAt(id).setIcon(mIconsIDsPressed[0]);
                break;
            case 1:
                mTabLayout.getTabAt(id).setIcon(mIconsIDsPressed[1]);
                break;
            case 2:
                mTabLayout.getTabAt(id).setIcon(mIconsIDsPressed[2]);
                break;
        }
    }
    private void setupUnselectedTab(int id){
        switch (id){
            case 0:
                mTabLayout.getTabAt(id).setIcon(mIconsIDs[0]);
                break;
            case 1:
                mTabLayout.getTabAt(id).setIcon(mIconsIDs[1]);
                break;
            case 2:
                mTabLayout.getTabAt(id).setIcon(mIconsIDs[2]);
                break;
        }
    }
}
