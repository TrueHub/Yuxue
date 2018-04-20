package vone.person.com.yuxue.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vone.person.com.yuxue.BaseActivity;
import vone.person.com.yuxue.BaseFragment;
import vone.person.com.yuxue.R;
import vone.person.com.yuxue.rxbeans.Land;
import vone.person.com.yuxue.tools.RequestPermissionUtils;
import vone.person.com.yuxue.tools.RxBus;

import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_ACCESS_COARSE_LOCATION;
import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_ACCESS_FINE_LOCATION;
import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_CALL_PHONE;
import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_CAMERA;
import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_GET_ACCOUNTS;
import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE;
import static vone.person.com.yuxue.tools.RequestPermissionUtils.PERMISSION_READ_PHONE_STATE;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MSL MainActivity";
    private ViewPager mViewPager;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();
    private TabLayout mTableLayout;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String[] permissions = new String[]{
                PERMISSION_READ_EXTERNAL_STORAGE,
                PERMISSION_CALL_PHONE,
                PERMISSION_CAMERA,
                PERMISSION_ACCESS_FINE_LOCATION,
                PERMISSION_ACCESS_COARSE_LOCATION,
                PERMISSION_READ_PHONE_STATE,
                PERMISSION_GET_ACCOUNTS
        };
        RequestPermissionUtils.requestPermission(this, permissions, "权限给哦给哦给嘛 ლ(⌒▽⌒ლ)");

        initFrags();
        initTableLayout();

    }

    private void initTableLayout() {

        mTableLayout.setBackgroundColor(getResources().getColor(R.color.alp_colorWhite));
        mTableLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorBgRed));//指示条的颜色
        mTableLayout.setSelectedTabIndicatorHeight(10);
        mTableLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < tabs.size(); i++) {
            TabLayout.Tab tab = mTableLayout.getTabAt(i);
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.m_tablelayout, mTableLayout, false);
            textView.setText(tabs.get(i));
            if (tab != null) {
                tab.setCustomView(textView);
            }
        }
    }

    private void initFrags() {

        fragmentList.add(new Frg_Timer());
        fragmentList.add(new Frg_Notes());

        tabs.add("挑战");
        tabs.add("记录");

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                RxBus.getDefault().send(Land.PauseTime);
            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTableLayout = (TabLayout) findViewById(R.id.tableLayout);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
