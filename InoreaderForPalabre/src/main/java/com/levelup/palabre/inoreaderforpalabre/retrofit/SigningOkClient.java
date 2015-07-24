/*
 * Copyright (C) 2013 Patrik �kerfeldt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.levelup.palabre.inoreaderforpalabre.retrofit;

import com.levelup.palabre.inoreaderforpalabre.inoreader.HttpRequestAdapter;
import com.levelup.palabre.inoreaderforpalabre.inoreader.InoreaderKeys;

import java.io.IOException;

import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * This is a helper class, a {@code retrofit.client.UrlConnectionClient} to use
 * when building your {@code retrofit.RestAdapter}.
 */
public class SigningOkClient extends OkClient {


    private final String token;

    public SigningOkClient(String token) {
        this.token = token;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Request requestToSend = request;
        final HttpRequestAdapter httpRequestAdapter = new HttpRequestAdapter(request);

        httpRequestAdapter.setHeader("Authorization", "GoogleLogin auth=" + token);
        httpRequestAdapter.setHeader("AppId", InoreaderKeys.APP_ID);
        httpRequestAdapter.setHeader("AppKey", InoreaderKeys.APP_KEY);

        requestToSend = (Request) httpRequestAdapter.unwrap();

        return super.execute(requestToSend);
    }

}
