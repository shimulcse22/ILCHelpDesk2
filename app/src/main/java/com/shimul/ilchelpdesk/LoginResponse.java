package com.shimul.ilchelpdesk;

public class LoginResponse {
    private RequestModel user;
   // private ApiKeyForm apiKey;

    public LoginResponse() {
    }

    public LoginResponse(RequestModel user) {
        this.user = user;
    }

    public RequestModel getUser() {
        return user;
    }

    public void setUser(RequestModel user) {
        this.user = user;
    }


}
