package com.transcendensoft.hedbanz.data.prefs;
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
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.transcendensoft.hedbanz.data.entity.User;

/**
 * Wrapper for SharedPreferences with
 * business model keys
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class PreferenceManager {
    private SharedPreferences mPreferences;

    private static final String PREF_NAME = "HedbanzPreferences";
    private static final String IS_AUTHORISED = "isAuthorised";
    private static final String USER_ENTITY = "user";

    public PreferenceManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
    }

    private SharedPreferences.Editor getEditor() {
        return mPreferences.edit();
    }

    public void setIsAuthorised(boolean isAuthorised){
        getEditor().putBoolean(IS_AUTHORISED, isAuthorised).apply();
    }

    public boolean isAuthorised(){
        return mPreferences.getBoolean(IS_AUTHORISED, false);
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        getEditor().putString(USER_ENTITY, json).apply();
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = mPreferences.getString(USER_ENTITY, "{}");

        return gson.fromJson(json, User.class);
    }
}
