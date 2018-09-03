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
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.Locale;

/**
 * Wrapper for SharedPreferences with
 * business model keys
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class PreferenceManager {
    private SharedPreferences mPreferences;

    private static final String PREF_NAME = "HedbanzPreferences";
    private static final String IS_AUTHORISED = "isAuthorised";
    private static final String USER_ENTITY = "user";
    private static final String FIREBASE_TOKEN = "firebaseToken";
    private static final String CURRENT_ROOM_ID = "currentRoomId";
    private static final String FIREBASE_TOKEN_BINDED = "firebaseTokenBinded";
    private static final String AUTHORIZATION_TOKEN = "authorizationToken";
    private static final String APP_NEW_VERSION = "appNewVersion";
    private static final String LOCALE = "locale";
    private static final String IS_USER_KICKED = "isUserKicked";
    private static final String IS_LAST_USER = "isLastUser";
    private static final String IS_TUTORIAL_SHOWN = "isTutorialShown";

    public PreferenceManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
    }

    private SharedPreferences.Editor getEditor() {
        return mPreferences.edit();
    }

    public void setIsAuthorised(boolean isAuthorised) {
        getEditor().putBoolean(IS_AUTHORISED, isAuthorised).apply();
    }

    public boolean isAuthorised() {
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
        if (json.equals("{}")) {
            return null;
        } else {
            return gson.fromJson(json, User.class);
        }
    }

    public void setFirebaseToken(String token) {
        getEditor().putString(FIREBASE_TOKEN, token).apply();
    }

    public String getFirebaseToken() {
        return mPreferences.getString(FIREBASE_TOKEN, "");
    }

    public void setCurrentRoomId(long currentRoomId) {
        getEditor().putLong(CURRENT_ROOM_ID, currentRoomId).apply();
    }

    public long getCurrentRoomId() {
        return mPreferences.getLong(CURRENT_ROOM_ID, -1);
    }

    public void setFirebaseTokenBinded(boolean firebaseTokenBinded) {
        getEditor().putBoolean(FIREBASE_TOKEN_BINDED, firebaseTokenBinded).apply();
    }

    public boolean getFirebaseTokenBinded() {
        return mPreferences.getBoolean(FIREBASE_TOKEN_BINDED, false);
    }

    public void setAuthorizationToken(String token) {
        getEditor().putString(AUTHORIZATION_TOKEN, token).apply();
    }

    public String getAuthorizationToken() {
        return mPreferences.getString(AUTHORIZATION_TOKEN, "");
    }

    public void setAppNewVersion(boolean isNewVersionAvailable) {
        getEditor().putBoolean(APP_NEW_VERSION, isNewVersionAvailable).apply();
    }

    public boolean isAppNewVersionAvailable() {
        return mPreferences.getBoolean(APP_NEW_VERSION, false);
    }

    public void setLocale(String locale){
        getEditor().putString(LOCALE, locale).apply();
    }

    public String getLocale(){
        return mPreferences.getString(LOCALE, Locale.getDefault().getLanguage());
    }

    public void setIsUserKicked(boolean isUserKicked){
        getEditor().putBoolean(IS_USER_KICKED, isUserKicked).apply();
    }

    public boolean isUserKicked(){
        return mPreferences.getBoolean(IS_USER_KICKED, false);
    }

    public void setIsLastUser(boolean isLastUser){
        getEditor().putBoolean(IS_LAST_USER, isLastUser).apply();
    }

    public boolean isLastUser(){
        return mPreferences.getBoolean(IS_LAST_USER, false);
    }

    public void setIsTutorialShown(boolean isTutorialShown){
        getEditor().putBoolean(IS_TUTORIAL_SHOWN, isTutorialShown).apply();
    }

    public boolean isTutorialShown(){
        return mPreferences.getBoolean(IS_TUTORIAL_SHOWN, false);
    }
}
