package com.transcendensoft.hedbanz.data.network.service;
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

import com.transcendensoft.hedbanz.data.entity.Room;
import com.transcendensoft.hedbanz.data.entity.RoomFilter;
import com.transcendensoft.hedbanz.data.entity.ServerResult;
import com.transcendensoft.hedbanz.data.entity.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface that describes all API methods with server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface ApiService {
    @PUT("user")
    Observable<ServerResult<User>> registerUser(@Body User user);

    @POST("user")
    Observable<ServerResult<User>> authUser(@Body User user);

    @PATCH("user")
    Observable<ServerResult<User>> updateUser(@Body HashMap<String, Object> userMap);

    @GET("rooms/{page}")
    Observable<ServerResult<List<Room>>> getRooms(@Path("page") int page);

    @PUT("rooms")
    Observable<ServerResult<Room>> createRoom(@Body HashMap<String, Object> roomDataMap);

    @POST("rooms/{page}")
    Observable<ServerResult<List<Room>>> filterRooms(
            @Path("page") int page,
            @Body RoomFilter roomFilter);
}
