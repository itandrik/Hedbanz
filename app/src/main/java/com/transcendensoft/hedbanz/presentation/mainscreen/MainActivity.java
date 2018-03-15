package com.transcendensoft.hedbanz.presentation.mainscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.customview.widget.VerticalViewPager;
import com.transcendensoft.hedbanz.presentation.mainscreen.menu.MenuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.verticalViewPager) VerticalViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this, this);

        initViewPager();
    }

    private void initViewPager(){
        MainScreenFragmentAdapter adapter = new MainScreenFragmentAdapter.Holder(getSupportFragmentManager())
                .add(new MainFragment())
                .add(new MenuFragment())
                .set();
        mViewPager.setAdapter(adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    public void openMenu(){
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }
}
