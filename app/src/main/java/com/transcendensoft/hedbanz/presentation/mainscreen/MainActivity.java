package com.transcendensoft.hedbanz.presentation.mainscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.custom.widget.VerticalViewPager;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;
import com.transcendensoft.hedbanz.presentation.intro.IntroActivity;
import com.transcendensoft.hedbanz.presentation.menu.MenuFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

import static com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseMessagingService.ACTION_NEW_VERSION_AVAILABLE;

public class MainActivity extends BaseActivity {
    @BindView(R.id.verticalViewPager) VerticalViewPager mViewPager;

    @Inject Lazy<MainFragment> mainFragmentLazy;
    @Inject Lazy<MenuFragment> menuFragmentLazy;
    @Inject PreferenceManager mPreferenceManager;
    private BroadcastReceiver mNewVersionReceiver;

    @Inject
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreRoom();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this, this);
        initViewPager();
        initNewVersionReceiver();

        if(!mPreferenceManager.isTutorialShown()){
            startActivity(new Intent(this, IntroActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNewVersionReceiver, new IntentFilter(ACTION_NEW_VERSION_AVAILABLE));
        if(mPreferenceManager.isAppNewVersionAvailable()){
            showNewVersionAvailableDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNewVersionReceiver);
    }

    private void restoreRoom() {
        if (mPreferenceManager.getCurrentRoomId() > 0) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(getString(R.string.bundle_room_id),
                    mPreferenceManager.getCurrentRoomId());
            startActivity(intent);
        }
    }

    private void initViewPager() {
        MainScreenFragmentAdapter adapter = new MainScreenFragmentAdapter.Holder(getSupportFragmentManager())
                .add(mainFragmentLazy.get())
                .add(menuFragmentLazy.get())
                .set();
        mViewPager.setAdapter(adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    private void initNewVersionReceiver() {
        mNewVersionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ((intent != null) && (intent.getAction() != null) &&
                        intent.getAction().equalsIgnoreCase(ACTION_NEW_VERSION_AVAILABLE)) {
                    showNewVersionAvailableDialog();
                }
            }
        };
    }

    public void openMenu() {
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showServerError() {
        //Stub
    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideAll() {

    }

    private void showNewVersionAvailableDialog() {
        Drawable icon = VectorDrawableCompat.create(
                getResources(), R.drawable.ic_google_play, null);
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.main_menu_new_version_title))
                .setMessage(getString(R.string.main_menu_new_version_message))
                .setPositiveButton(getString(R.string.action_update),
                        (dialog, v) -> openGooglePlayPage(dialog))
                .setNegativeButton(getString(R.string.action_later), (dialog, which) -> dialog.cancel())
                .setIcon(icon)
                .setCancelable(true)
                .setOnCancelListener(dialog -> {
                            mPreferenceManager.setAppNewVersion(false);
                            dialog.dismiss();
                        })
                .show();
    }

    private void openGooglePlayPage(DialogInterface dialog) {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        dialog.dismiss();
        mPreferenceManager.setAppNewVersion(false);
    }
}
