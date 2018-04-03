package com.transcendensoft.hedbanz.presentation.game.list.holder;
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
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents typing indicator with animation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class TypingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvTyping) TextView mTvTyping;
    @BindView(R.id.ivTyping) ImageView mIvTyping;

    private Context mContext;

    public TypingViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = context;
    }

    public void bindTypingText(List<User> users){
        String text = null;
        if(users == null || users.isEmpty()){
            throw new RuntimeException("Typing indicator. Users list is NULL or empty.");
        } else if(users.size() == 1){
            text = mContext.getString(R.string.game_typing_one_user, users.get(0).getLogin());
        } else if(users.size() == 2){
            text = mContext.getString(R.string.game_typing_two_user,
                    users.get(0).getLogin(),
                    users.get(1).getLogin());
        } else {
            text = mContext.getString(R.string.game_typing_several_users);
        }
        mTvTyping.setText(text);
    }

    public void bindTypingIndicatorImage(){
        final AnimatedVectorDrawableCompat avd =
                AnimatedVectorDrawableCompat.create(mContext, R.drawable.typing_anim);
        mIvTyping.setImageDrawable(avd);

        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                avd.start();
            }
        });

        avd.start();
    }
}
