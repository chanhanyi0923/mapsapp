package com.hanyi.mapsapp;

public interface IDataCallback<T> {
    void onComplete(T result);
    void onFailed(Exception exception);
}
