package com.example.misaya.socketclient;

public class CheckNull {
    private String username = "";
    private String password = "";

    public CheckNull(String username,String password){
        this.username = username;
        this.password = password;
    }

    public Boolean check(){
        if(username.equals("")||password.equals("")){
            return false;
        }else{
            if(username.contains(" ")||password.contains(" ")){
                return  false;
            }else {
                return true;
            }
        }
    }
}
