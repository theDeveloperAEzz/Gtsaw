package com.example.thedeveloper.gtsaw.module;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Class that represent the message.
 */
public class Chat {
    public String message;
    public String id2;
    public String id1;
    public long time;

    public Chat() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Chat(String message, String id1,String id2) {
        this.message = message;
        this.id1 = id1;
        this.id2=id2;
        time= 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id) {
        this.id1= id;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
