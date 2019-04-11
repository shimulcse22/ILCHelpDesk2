package com.shimul.ilchelpdesk;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponseBody<T> {


    private int responseCode;
    private String responseMessage;
    private T items;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public T getItems() {
        return items;
    }

        public void setItems(T items) {
            this.items = items;
    }

}
