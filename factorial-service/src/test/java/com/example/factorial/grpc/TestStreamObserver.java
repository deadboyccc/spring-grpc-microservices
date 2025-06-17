package com.example.factorial.grpc;

import io.grpc.stub.StreamObserver;

public class TestStreamObserver implements StreamObserver<math.MathResponse> {
    private math.MathResponse response;
    private Throwable error;
    private boolean completed = false;
    
    @Override
    public void onNext(math.MathResponse value) {
        this.response = value;
    }
    
    @Override
    public void onError(Throwable t) {
        this.error = t;
    }
    
    @Override
    public void onCompleted() {
        this.completed = true;
    }
    
    public math.MathResponse getResponse() {
        return response;
    }
    
    public Throwable getError() {
        return error;
    }
    
    public boolean isError() {
        return error != null;
    }
    
    public boolean isCompleted() {
        return completed;
    }
} 