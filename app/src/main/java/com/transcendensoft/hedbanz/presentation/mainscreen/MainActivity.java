package com.transcendensoft.hedbanz.presentation.mainscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;
import com.transcendensoft.hedbanz.presentation.intro.IntroActivity;
import com.transcendensoft.hedbanz.presentation.menu.MenuFragment;
import com.transcendensoft.hedbanz.presentation.roomcreation.CreateRoomFragment;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

import static com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseMessagingService.ACTION_NEW_VERSION_AVAILABLE;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tvMenuRooms)
    AppCompatTextView mTvMenuRooms;
    @BindView(R.id.tvMenuSettings)
    AppCompatTextView mTvMenuProfile;
    @BindView(R.id.fabNewRoom)
    FloatingActionButton mFabNewRoom;
    @BindView(R.id.flCreateRoomFragment)
    FrameLayout mFlCreateRoomFragmentContainer;
    @BindView(R.id.toolbarMain)
    Toolbar mToolbar;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.groupSearchIcon)
    Group mGroupSearchIcon;

    @Inject
    Lazy<CreateRoomFragment> createRoomFragmentLazy;
    @Inject
    Lazy<RoomsFragment> roomsFragmentLazy;
    @Inject
    Lazy<MenuFragment> menuFragmentLazy;

    @Inject
    PreferenceManager mPreferenceManager;
    private BroadcastReceiver mNewVersionReceiver;

    private int redColor;
    private int textPrimaryColor;
    private Fragment activeFragment;

    @Inject
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreRoom();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this, this);

        redColor = ContextCompat.getColor(this, R.color.textDarkRed);
        textPrimaryColor = ContextCompat.getColor(this, R.color.textPrimary);
        mTvToolbarTitle.setText(getString(R.string.rooms_title));

        initNewVersionReceiver();
        initFragmentManager();
        initBottomNavigation();

        if (!mPreferenceManager.isTutorialShown()) {
            startActivity(new Intent(this, IntroActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNewVersionReceiver, new IntentFilter(ACTION_NEW_VERSION_AVAILABLE));
        if (mPreferenceManager.isAppNewVersionAvailable()) {
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

    private void initFragmentManager() {
        activeFragment = roomsFragmentLazy.get();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flPageContainer, menuFragmentLazy.get(), "MenuFragmentTag")
                .hide(menuFragmentLazy.get()).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flPageContainer, roomsFragmentLazy.get(), "RoomsFragmentTag")
                .commit();
    }

    private void initBottomNavigation() {
        mTvMenuRooms.setOnClickListener(v -> {
            if (!(activeFragment instanceof RoomsFragment)) {
                mTvMenuRooms.setTextColor(redColor);
                mTvMenuRooms.setSupportCompoundDrawablesTintList(ColorStateList.valueOf(redColor));
                mTvMenuProfile.setTextColor(textPrimaryColor);
                mTvMenuProfile.setSupportCompoundDrawablesTintList(ColorStateList.valueOf(textPrimaryColor));
                mTvToolbarTitle.setText(getString(R.string.rooms_title));
                mGroupSearchIcon.setVisibility(View.VISIBLE);

                getSupportFragmentManager().beginTransaction().hide(activeFragment).show(roomsFragmentLazy.get()).commit();
                activeFragment = roomsFragmentLazy.get();
            }
        });

        mTvMenuProfile.setOnClickListener(v -> {
            if (!(activeFragment instanceof MenuFragment)) {
                mTvMenuProfile.setTextColor(redColor);
                mTvMenuProfile.setSupportCompoundDrawablesTintList(ColorStateList.valueOf(redColor));
                mTvMenuRooms.setTextColor(textPrimaryColor);
                mTvMenuRooms.setSupportCompoundDrawablesTintList(ColorStateList.valueOf(textPrimaryColor));
                mTvToolbarTitle.setText(getString(R.string.menu_title));
                roomsFragmentLazy.get().hideFilters();
                mGroupSearchIcon.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().hide(activeFragment).show(menuFragmentLazy.get()).commit();
                activeFragment = menuFragmentLazy.get();
            }
        });

        mFabNewRoom.setOnClickListener(v -> {
            mFlCreateRoomFragmentContainer.setVisibility(View.VISIBLE);
            roomsFragmentLazy.get().hideFilters();
            mGroupSearchIcon.setVisibility(View.GONE);
            mTvToolbarTitle.setText(getString(R.string.room_creation_title));

            // TODO add animation
        });
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

    @Override
    public void onBackPressed() {
        if(mFlCreateRoomFragmentContainer.getVisibility() == View.VISIBLE){
            mFlCreateRoomFragmentContainer.setVisibility(View.GONE);
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

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
