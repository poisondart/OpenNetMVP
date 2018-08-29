package ru.opennet.nix.opennetmvp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.favorites.FavoritesFragment;
import ru.opennet.nix.opennetmvp.news.NewsFragment;
import ru.opennet.nix.opennetmvp.utils.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private static final String CHOSEN_TAB_POSITION = "position";
    private int mChosenPosition = 0;

    private final int[] mIconsIDs = {R.drawable.ic_news_tab_icon,
            R.drawable.ic_favs_tab_icon,
            R.drawable.ic_settings_tab_icon};

    private final int[] mIconsIDsPressed = {R.drawable.ic_news_tab_pressed_icon,
            R.drawable.ic_favs_tab_pressed_icon,
            R.drawable.ic_settings_tab_pressed_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mChosenPosition = savedInstanceState.getInt(CHOSEN_TAB_POSITION);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupViewPager();
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(mViewPager);
                for(int i = 0; i < mTabLayout.getTabCount(); i++){
                    if(i == mChosenPosition){
                        mTabLayout.getTabAt(i).setIcon(mIconsIDsPressed[i]);
                    }else{
                        mTabLayout.getTabAt(i).setIcon(mIconsIDs[i]);
                    }
                }
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mChosenPosition = tab.getPosition();
                mTabLayout.getTabAt(mChosenPosition).setIcon(mIconsIDsPressed[mChosenPosition]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mTabLayout.getTabAt(tab.getPosition()).setIcon(mIconsIDs[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mChosenPosition = tab.getPosition();
                mTabLayout.getTabAt(mChosenPosition).setIcon(mIconsIDsPressed[mChosenPosition]);
            }
        });
    }


    private void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NewsFragment());
        viewPagerAdapter.addFragment(new FavoritesFragment());
        viewPagerAdapter.addFragment(new PreferenceFragment());
        mViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CHOSEN_TAB_POSITION, mChosenPosition);
        super.onSaveInstanceState(outState);
    }
}
