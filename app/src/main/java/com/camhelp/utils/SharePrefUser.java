package com.camhelp.utils;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.camhelp.common.CommonGlobal;
import com.camhelp.entity.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by storm on 2017-08-08.
 * SharedPreferences存取用户对象
 */

public class SharePrefUser {
    private String TAG = "SharePrefUser";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public void saveUser(User user) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(CommonGlobal.userobj, temp);
            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public User getUser() {
        String temp = pref.getString(CommonGlobal.userobj, "");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            user = (User) ois.readObject();
        } catch (IOException e) {
            L.d(TAG, e.toString());
        } catch (ClassNotFoundException e1) {
            L.d(TAG, e1.toString());
        }
        return user;
    }
}
