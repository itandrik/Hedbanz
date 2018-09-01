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

import java.util.List;

/**
 * Class that contains list of entities with
 * pagination control flags like error, finish and so on.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class PaginationState<T> {
    private List<T> data;
    private boolean hasServerError;
    private boolean hasInternetError;
    private boolean hasUnauthorizedError;
    private boolean isRefreshed;

    public List<T> getData() {
        return data;
    }

    public PaginationState<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public boolean isHasServerError() {
        return hasServerError;
    }

    public PaginationState<T> setHasServerError(boolean hasServerError) {
        this.hasServerError = hasServerError;
        return this;
    }

    public boolean isHasInternetError() {
        return hasInternetError;
    }

    public PaginationState<T> setHasInternetError(boolean hasInternetError) {
        this.hasInternetError = hasInternetError;
        return this;
    }

    public boolean isHasUnauthorizedError() {
        return hasUnauthorizedError;
    }

    public PaginationState<T> setHasUnauthorizedError(boolean hasUnauthorizedError) {
        this.hasUnauthorizedError = hasUnauthorizedError;
        return this;
    }

    public boolean isRefreshed() {
        return isRefreshed;
    }

    public PaginationState<T> setRefreshed(boolean refreshed) {
        isRefreshed = refreshed;
        return this;
    }
}
