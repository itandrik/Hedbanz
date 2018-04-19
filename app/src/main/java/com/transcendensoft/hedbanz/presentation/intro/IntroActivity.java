package com.transcendensoft.hedbanz.presentation.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.transcendensoft.hedbanz.R;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Add fragments (slides)
        addSlide(AppIntro2Fragment.newInstance("Title 1", "jdfkasljgkl;dsjfg;lkjsdfklgjsk;dljfgkl;sjdf;lkgjkl;sdfg",
                0, ContextCompat.getColor(this, R.color.google_red)));
        addSlide(AppIntro2Fragment.newInstance("Title 2", "jdfkasljgkl;dsjfg;;dljfgkl;sjdf;lkgjkl;sdfg",
                0, ContextCompat.getColor(this, R.color.google_green)));
        addSlide(AppIntro2Fragment.newInstance("Title 3", ";lkgjkl;sdfg",
                0, ContextCompat.getColor(this, R.color.google_yellow)));
        addSlide(AppIntro2Fragment.newInstance("Title 4", "jdfkasljgkl;dsjfg;lk;sdfg",
                0, ContextCompat.getColor(this, R.color.google_blue)));
        showSkipButton(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
