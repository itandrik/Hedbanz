package com.transcendensoft.hedbanz.domain;
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

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.network.retrofit.NoConnectivityException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Use case that has pagination logic
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */

public abstract class PaginationUseCase<T, ParamUseCase, ParamPaginator>
        extends ObservableUseCase<PaginationState<T>, ParamUseCase>
        implements Paginator<T, ParamPaginator> {
    protected int mCurrentPage = 0;

    public PaginationUseCase(ObservableTransformer mSchedulersTransformer, CompositeDisposable mCompositeDisposable) {
        super(mSchedulersTransformer, mCompositeDisposable);
    }

    @Override
    public PaginationUseCase<T, ParamUseCase, ParamPaginator> loadNextPage() {
        mCurrentPage++;
        return this;
    }

    @Override
    public PaginationUseCase<T, ParamUseCase, ParamPaginator> refresh(ParamPaginator paramPaginator) {
        mCurrentPage = 0;
        return this;
    }

    protected ObservableSource<PaginationState<T>> convertEntitiesToPagingResult(List<T> entities) {
        PaginationState<T> paginationState = new PaginationState<>();

        paginationState.setData(entities)
                .setRefreshed(mCurrentPage <= 0)
                .setHasInternetError(false)
                .setHasServerError(false);

        if (entities == null || entities.isEmpty()) {
            decPage();
        }

        return Observable.just(paginationState);
    }

    protected PaginationState<T> mapPaginationStateBasedOnError(Throwable throwable) {
        Timber.e(throwable);
        PaginationState<T> paginationState = new PaginationState<>();
        paginationState.setRefreshed(mCurrentPage <= 0);

        if (throwable instanceof NoConnectivityException) {
            paginationState
                    .setHasServerError(false)
                    .setHasUnauthorizedError(false)
                    .setHasInternetError(true);
        } else if ((throwable instanceof HttpException && (((HttpException) throwable).code() == 401 ||
                ((HttpException) throwable).code() == 403)) ||
                (throwable instanceof HedbanzApiException &&
                        ((HedbanzApiException) throwable).getServerErrorCode() == 103)) {
            paginationState
                    .setHasServerError(false)
                    .setHasUnauthorizedError(true)
                    .setHasInternetError(false);
        } else {
            paginationState
                    .setHasServerError(true)
                    .setHasUnauthorizedError(false)
                    .setHasInternetError(false);
        }

        decPage();

        return paginationState;
    }

    private void decPage() {
        if (mCurrentPage > 0) {
            mCurrentPage--;
        } else {
            mCurrentPage = 0;
        }
    }
}
