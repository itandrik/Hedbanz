package com.transcendensoft.hedbanz.presentation.mainscreen;
/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.custom.transform.DefaultTransformer;
import com.transcendensoft.hedbanz.presentation.custom.widget.MainViewPager;
import com.transcendensoft.hedbanz.presentation.roomcreation.CreateRoomFragment;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsFragment;
import com.transcendensoft.hedbanz.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;

import static com.transcendensoft.hedbanz.utils.TabUtils.getIconEnabledTabWithIndex;
import static com.transcendensoft.hedbanz.utils.TabUtils.getIconForDisabledTabWithIndex;

/**
 * Fragment that contains view pager that shows
 * two tabs: room creation and room list.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class MainFragment extends DaggerFragment implements ViewPager.OnPageChangeListener{
    @BindView(R.id.mainViewPager) MainViewPager mViewPager;
    @BindView(R.id.tlBottomNavigation) TabLayout mTabLayout;
    @BindView(R.id.tvToolbarTitle) TextView mTvToolbarTitle;
    @BindView(R.id.toolbarMain) Toolbar mToolbar;
    private MainScreenFragmentAdapter mAdapter;

    //Tabs
    private TextView mTvTabRooms;
    private TextView mTvTabCreateRoom;

    @Inject Lazy<RoomsFragment> mRoomsFragmentLazy;
    @Inject Lazy<CreateRoomFragment> mCreateRoomFragmentLazy;

    @Inject
    public MainFragment() {
        // Requires empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        initViewPager();
        initTabLayout();

        return view;
    }

    private void initViewPager(){
        RoomsFragment roomsFragment = mRoomsFragmentLazy.get();
        CreateRoomFragment createRoomFragment = mCreateRoomFragmentLazy.get();

        mAdapter = new MainScreenFragmentAdapter.Holder(getChildFragmentManager())
                .add(roomsFragment)
                .add(createRoomFragment)
                .set();
        mAdapter.addFragmentTitle(getString(R.string.rooms_title));
        mAdapter.addFragmentTitle(getString(R.string.room_creation_title));

        mViewPager.setPageTransformer(false, new DefaultTransformer());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initTabLayout(){
        mTabLayout.setupWithViewPager(mViewPager);

        //Init tab icons
        mTvTabRooms = initTab(true, 0, R.layout.tab_main_custom, R.drawable.ic_rooms_enabled);
        mTvTabCreateRoom = initTab(false, 1, R.layout.tab_main_custom, R.drawable.ic_create_room_disabled);

        CharSequence titleSequence = mAdapter.getPageTitle(0);
        if(titleSequence != null) {
            changeTitle(titleSequence.toString());
        }
    }

    private TextView initTab(boolean isActive, int index, @LayoutRes int layoutRes, @DrawableRes int iconRes) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        TextView tabText = (TextView) inflater.inflate(layoutRes, null, false);
        tabText.setText(mAdapter.getPageTitle(index));

        changeTabView(isActive, index, iconRes, tabText);

        TabLayout.Tab tab = mTabLayout.getTabAt(index);
        if (tab != null) {
            tab.setCustomView(tabText);
        }

        return tabText;
    }

    private void changeTabView(boolean isActive, int index, @DrawableRes int iconRes, TextView tabText) {
        if(getActivity() != null) {
            if (isActive) {
                tabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.textDarkRed));
            } else {
                tabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.textSecondary));
            }
            Drawable icon = VectorDrawableCompat.create(getResources(), iconRes, null);
            int size = (int) ViewUtils.dpToPx(getActivity(), 20);
            if (icon != null) {
                icon.setBounds(0, 0, size, size);
                if(index == 0) {
                    tabText.setCompoundDrawables(icon, null, null, null);
                    tabText.setGravity(Gravity.LEFT);
                } else {
                    tabText.setCompoundDrawables(null, null, icon, null);
                    tabText.setGravity(Gravity.RIGHT);
                }
            }
        }
    }

    public void changeTitle(String titleText) {
        mTvToolbarTitle.setText(titleText);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            CharSequence titleSequence = mAdapter.getPageTitle(position);
            if (titleSequence != null) {
                changeTitle(titleSequence.toString());
            }
            changeTab(position);

            if(position == 1) {
                RoomsFragment roomsFragment = (RoomsFragment) mAdapter.getRegisteredFragment(0);
                roomsFragment.hideFilters();
            }

        }catch (ClassCastException e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }

    private void changeTab(int index) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            changeTabView(false, i, getIconForDisabledTabWithIndex(i),
                    getTabTextViewWithIndex(i));
        }
        changeTabView(true, index, getIconEnabledTabWithIndex(index), getTabTextViewWithIndex(index));
    }

    private TextView getTabTextViewWithIndex(int index) {
        switch (index) {
            case 0:
                return mTvTabRooms;
            case 1:
                return mTvTabCreateRoom;
            default:
                return mTvTabRooms;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.fab)
    protected void onMenuOpen(){
        if(getActivity() != null) {
            ((MainActivity) getActivity()).openMenu();
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
