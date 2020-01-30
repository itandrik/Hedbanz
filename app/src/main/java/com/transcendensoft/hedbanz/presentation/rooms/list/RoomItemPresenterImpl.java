package com.transcendensoft.hedbanz.presentation.rooms.list;
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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.interactor.rooms.IsRoomPasswordCorrectInteractor;
import com.transcendensoft.hedbanz.domain.interactor.rooms.exception.RoomCreationException;
import com.transcendensoft.hedbanz.domain.validation.RoomError;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;
import com.transcendensoft.hedbanz.presentation.roomcreation.models.RoomIcons;
import com.transcendensoft.hedbanz.presentation.roomcreation.models.StickerIcons;
import com.transcendensoft.hedbanz.utils.AndroidUtils;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter from MVP pattern that processes concrete
 * room view item from list of rooms on main screen.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */

public class RoomItemPresenterImpl extends BasePresenter<Room, RoomItemContract.View>
        implements RoomItemContract.Presenter {
    private IsRoomPasswordCorrectInteractor mIsRoomPasswordCorrectInteractor;
    private AlertDialog mRoomPasswordDialog;

    @Inject
    public RoomItemPresenterImpl(IsRoomPasswordCorrectInteractor isRoomPasswordCorrectInteractor) {
        this.mIsRoomPasswordCorrectInteractor = isRoomPasswordCorrectInteractor;
    }

    @Override
    protected void updateView() {
        if (model != null) {
            if (model.getId() != -1) {
                view().setCurAndMaxPlayers(model.getCurrentPlayersNumber(), model.getMaxPlayers());
                view().setName(model.getName());
                view().setIsProtected(model.isWithPassword());
                view().setIsActive(model.isActive());
                view().setIcon(RoomIcons.Companion.getIconByServerId(model.getIconId()),
                        StickerIcons.Companion.getIconByServerId(model.getStickerId()));
                view().showCard();
            } else {
                view().showLoadingItem();
            }
        }
    }

    @Override
    public void destroy() {
        mIsRoomPasswordCorrectInteractor.dispose();
    }

    public void onClickRoom() {
        Context context = view().provideContext();
        if (context != null) {
            if (!model.isWithPassword()) {
                startGameActivity(context, null);
            } else {
                showRoomPasswordAlertDialog(context);
            }
        }
    }

    private void startGameActivity(Context context, String password) {
        Intent intent = new Intent(context, GameActivity.class);

        intent.putExtra(context.getString(R.string.bundle_room_id), (Long) model.getId());
        intent.putExtra(context.getString(R.string.bundle_room_password), password);
        intent.putExtra(context.getString(R.string.bundle_is_active_game), (Boolean) model.isActive());
        context.startActivity(intent);
    }

    private void showRoomPasswordAlertDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_room_password,
                view().getItemView(), false);
        EditText etRoomPassword = view.findViewById(R.id.etPassword);
        TextView tvPasswordError = view.findViewById(R.id.tvErrorPassword);

        view.findViewById(R.id.btnSubmitRoom).setOnClickListener(v -> {
            Room room = new Room.Builder()
                    .setPassword(etRoomPassword.getText().toString().trim())
                    .setWithPassword(true)
                    .setId(model.getId())
                    .build();
            processRoomPassword(room, tvPasswordError);
        });

        mRoomPasswordDialog = new AlertDialog.Builder(context).setView(view).show();
        mRoomPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void processRoomPassword(Room room, TextView tvPasswordError) {
        view().showLoadingDialog();
        tvPasswordError.setVisibility(View.GONE);
        mIsRoomPasswordCorrectInteractor.execute(room,
                (obj) -> { },
                err -> {
                    processRoomPasswordOnError(err, tvPasswordError);
                    view().hideLoadingDialog();
                },
                () -> {
                    startGameActivity(view().provideContext(), room.getPassword());
                    mRoomPasswordDialog.dismiss();
                    view().hideLoadingDialog();
                });
    }

    private void processRoomPasswordOnError(Throwable err, TextView tvPasswordError) {
        if ((err instanceof RoomCreationException)) {
            RoomCreationException exception = (RoomCreationException) err;
            for (RoomError roomError : exception.getErrors()) {
                processRoomError(roomError, tvPasswordError);
            }
        } else {
            Timber.e(err);
            Context context = view().provideContext();
            if (context != null) {
                AndroidUtils.showLongToast(context, context.getString(R.string.error_server));
            }
        }
    }

    private void processRoomError(RoomError roomError, TextView tvPasswordError) {
        Context context = view().provideContext();
        if (context != null) {
            switch (roomError) {
                case EMPTY_PASSWORD:
                case INVALID_PASSWORD:
                case INCORRECT_PASSWORD:
                    tvPasswordError.setText(context.getString(roomError.getErrorMessage()));
                    tvPasswordError.setVisibility(View.VISIBLE);
                    break;
                case UNDEFINED_ERROR:
                default:
                    Timber.e(context.getString(roomError.getErrorMessage()));
                    tvPasswordError.setText(context.getString(R.string.error_server));
            }
        }
    }
}
