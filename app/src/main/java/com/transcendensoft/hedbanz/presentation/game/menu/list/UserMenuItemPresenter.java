package com.transcendensoft.hedbanz.presentation.game.menu.list;
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

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;

import io.reactivex.ObservableTransformer;
import timber.log.Timber;

/**
 * Presenter from MVP pattern that processes concrete
 * user from game mode side bar menu.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class UserMenuItemPresenter extends BasePresenter<RxUser, UserMenuItemContract.View>
        implements UserMenuItemContract.Presenter {
    private ObservableTransformer mSchedulersTransformer;

    public UserMenuItemPresenter(ObservableTransformer schedulersTransformer) {
        this.mSchedulersTransformer = schedulersTransformer;
    }

    @Override
    protected void updateView() {
        if (model != null) {
            updateUserView();

            addDisposable(model.userObservable()
                    .compose(applySchedulers())
                    .subscribe(user -> {
                        model.setUser(user);
                        updateUserView();
                    }, Timber::e));
        }
    }

    private void updateUserView() {
        //TODO change this shit
        view().setIcon(R.drawable.logo);
        view().setIsAfk(model.getUser().isAFK());
        view().setIsFriend(model.getUser().isFriend());
        view().setName(model.getUser().getLogin());
        view().setWord(model.getUser().getWord());
    }

    @Override
    public void destroy() {
        // stub
    }

    @Override
    public void onClickUser() {

    }

    @SuppressWarnings("unchecked")
    private <S> ObservableTransformer<S, S> applySchedulers() {
        return (ObservableTransformer<S, S>) mSchedulersTransformer;
    }
}
