package com.transcendensoft.hedbanz.data.network;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.transcendensoft.hedbanz.data.models.MessageDTO;
import com.transcendensoft.hedbanz.data.network.retrofit.HedbanzApiErrorHandlerFactory;
import com.transcendensoft.hedbanz.data.network.retrofit.MessageDeserializer;
import com.transcendensoft.hedbanz.data.network.service.ApiService;
import com.transcendensoft.hedbanz.data.network.source.ApiDataSource;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module that provides all API services
 * and Retrofit instances
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@Module(includes = NetworkModule.class)
public class ApiServiceModule {
    @Provides
    @ApplicationScope
    public ApiService provideHedbanzApiService(Retrofit hedbanzApiRetrofit) {
        return hedbanzApiRetrofit.create(ApiService.class);
    }

    @Provides
    @ApplicationScope
    public Gson provideGson(MessageDeserializer messageDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(MessageDTO.class, messageDeserializer)
                .setLenient()
                .create();
    }

    /*@Provides
    @ApplicationScope
    public RuntimeTypeAdapterFactory provideRuntimeTypeAdapterFactory() {
        String questionType = String.valueOf(MessageType.GUESS_WORD_THIS_USER.getId());
        RuntimeTypeAdapterFactory<MessageDTO> runtimeTypeAdapterFactory =
                RuntimeTypeAdapterFactory
                        .of(MessageDTO.class, "type");

        for (MessageType messageType : MessageType.values()) {
            if (messageType.equals(MessageType.GUESS_WORD_THIS_USER)) {
                runtimeTypeAdapterFactory.registerSubtype(QuestionDTO.class, questionType);
            } else {
                runtimeTypeAdapterFactory.registerSubtype(MessageDTO.class, questionType);
            }
        }
        return runtimeTypeAdapterFactory;
    }*/

    @Provides
    @ApplicationScope
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(HedbanzApiErrorHandlerFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(ApiDataSource.BASE_URL)
                .build();
    }
}
