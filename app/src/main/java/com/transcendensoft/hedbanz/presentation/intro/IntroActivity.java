package com.transcendensoft.hedbanz.presentation.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;

public class IntroActivity extends AppIntro2 {
    private PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Add fragments (slides)
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_page_1_title),
                getString(R.string.tutorial_page_1_text), 0,
                ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_page_2_title),
                getString(R.string.tutorial_page_2_text), 0,
                ContextCompat.getColor(this, R.color.pink_light)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_page_3_title),
                getString(R.string.tutorial_page_3_text), 0,
                ContextCompat.getColor(this, R.color.google_green)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_page_4_title),
                getString(R.string.tutorial_page_4_text), 0,
                ContextCompat.getColor(this, R.color.google_blue)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_page_5_title),
                getString(R.string.tutorial_page_5_text), 0,
                ContextCompat.getColor(this, R.color.teal_light)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_page_6_title),
                getString(R.string.tutorial_page_6_text), 0,
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.textPrimary),
                ContextCompat.getColor(this, R.color.textPrimary)));

        showSkipButton(true);
        showStatusBar(false);
        setDepthAnimation();

        mPreferenceManager = new PreferenceManager(this);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        mPreferenceManager.setIsTutorialShown(true);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        mPreferenceManager.setIsTutorialShown(true);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    @Override
    public void onBackPressed() {
        mPreferenceManager.setIsTutorialShown(true);
        super.onBackPressed();
    }
}
