package xyz.fz.springBootVertx.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import xyz.fz.springBootVertx.util.BaseUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    private boolean success;

    private String message;

    private Object data;

    private String redirect;

    private String forward;

    public static String ofSuccess() {
        return "{\"success\": true}";
    }

    public static String ofData(Object data) {
        Result result = new Result();
        result.success = true;
        result.data = data;
        return BaseUtil.toJson(result);
    }

    public static String ofMessage(String message) {
        return "{\"success\": false, \"message\": " + message + "}";
    }

    public static String ofRedirect(String redirect) {
        return "{\"success\": false, \"redirect\": " + redirect + "}";
    }

    public static String ofForward(String forward) {
        return "{\"success\": false, \"forward\": " + forward + "}";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }
}
