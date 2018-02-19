package com.transcendensoft.hedbanz.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.adapter.MainScreenFragmentAdapter;
import com.transcendensoft.hedbanz.view.custom.widget.VerticalViewPager;
import com.transcendensoft.hedbanz.view.fragment.MainFragment;
import com.transcendensoft.hedbanz.view.fragment.MenuFragment;

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
                .add(MainFragment.newInstance(1, mViewPager))
                .add(new MenuFragment())
                .set();
        mViewPager.setAdapter(adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    @Override
    public void onBackPressed() {
        mViewPager.setCurrentItem(0, true);
    }
}
