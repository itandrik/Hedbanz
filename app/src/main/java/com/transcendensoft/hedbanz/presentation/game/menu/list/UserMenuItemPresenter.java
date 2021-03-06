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

import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
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
    private PublishSubject<User> mItemClickObservable = PublishSubject.create();
    private Disposable disposable;

    public UserMenuItemPresenter(ObservableTransformer schedulersTransformer) {
        this.mSchedulersTransformer = schedulersTransformer;
    }

    @Override
    protected void updateView() {
        if (model != null) {
            updateUserView();
        }
    }

    @Override
    public void setModel(RxUser model) {
        super.setModel(model);
        if(disposable == null || disposable.isDisposed()){
            disposable = model.userObservable()
                    .compose(applySchedulers())
                    .subscribe(user -> {
                        model.setUser(user);
                        updateUserView();
                    }, Timber::e);
        }
    }

    private void updateUserView() {
        view().setIcon(model.getUser().getIconId().getResId());
        view().setStatus(model.getUser().getPlayerStatus());
        view().setName(model.getUser().getLogin());
        view().setIsWinner(model.getUser().isWinner());
        view().setWord(model.getUser());
        view().setWordVisible(model.getUser());
        view().getClickObservable(model.getUser())
                .subscribe(mItemClickObservable);
    }

    @Override
    public void destroy() {
        disposable.dispose();
        disposable = null;
    }

    @SuppressWarnings("unchecked")
    private <S> ObservableTransformer<S, S> applySchedulers() {
        return (ObservableTransformer<S, S>) mSchedulersTransformer;
    }

    public PublishSubject<User> getItemClickObservable() {
        return mItemClickObservable;
    }
}
