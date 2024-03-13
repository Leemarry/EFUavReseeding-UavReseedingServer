package com.bear.reseeding.model;

import okhttp3.OkHttpClient;

/**
 * @author N.
 * @date 2024-03-13 17:23
 */
public class OkHttpClientSingleton {
    private OkHttpClientSingleton() {}

    private static class OkHttpClientHolder {
        private static final OkHttpClient INSTANCE = new OkHttpClient();
    }

    public static OkHttpClient getInstance() {
        return OkHttpClientHolder.INSTANCE;
    }
}