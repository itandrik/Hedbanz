package com.transcendensoft.hedbanz.view.fragment;
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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.adapter.MainScreenFragmentAdapter;
import com.transcendensoft.hedbanz.view.custom.widget.MainViewPager;
import com.transcendensoft.hedbanz.view.custom.widget.VerticalViewPager;
import com.transcendensoft.hedbanz.view.custom.transform.DefaultTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment that contains view pager that shows
 * two tabs: room creation and room list.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class MainFragment extends Fragment {
    public static final String VIEWPAGER_KEY = "viewpager";
    public static final String POSITION_KEY = "position";
    @BindView(R.id.mainViewPager) MainViewPager mViewPager;

    public static Fragment newInstance(int position, VerticalViewPager viewPager) {
        Bundle args = new Bundle();
        args.putInt(POSITION_KEY, position);
        args.putSerializable(VIEWPAGER_KEY, viewPager);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        initViewPager();
        return view;
    }

    private void initViewPager(){
        MainScreenFragmentAdapter adapter = new MainScreenFragmentAdapter.Holder(getChildFragmentManager())
                .add(new RoomsFragment())
                .add(new CreateRoomFragment())
                .set();
        mViewPager.setPageTransformer(false, new DefaultTransformer());
        mViewPager.setAdapter(adapter);
    }
}
