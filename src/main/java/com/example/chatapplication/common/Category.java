package com.example.chatapplication.common;

public enum Category {
    ;

    public enum SocketService{
        chat("/chat");

        public final String name;
        SocketService(String name){
            this.name=name;
        }

    }
}
