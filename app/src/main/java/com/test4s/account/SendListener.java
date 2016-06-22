package com.test4s.account;

public interface SendListener{
        public abstract void sendToServer(String type,String uid,String info);
    }