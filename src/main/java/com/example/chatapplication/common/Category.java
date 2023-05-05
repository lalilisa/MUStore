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


    public enum Role{
        USER,
        ADMIN
    };
    public enum ErrorCodeEnum {
        INTERNAL_SERVER_ERROR,
        URI_NOT_FOUND,
        INVALID_PARAMETER,
        INVALID_FORMAT;

        private ErrorCodeEnum() {
        }
    };
}
