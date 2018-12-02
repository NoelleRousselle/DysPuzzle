package de.uni.oldenburg.dyspuzzle.handler;

public interface IServiceCallBack {

    void onSuccess(String response);
    void onFailure(String response);
}
