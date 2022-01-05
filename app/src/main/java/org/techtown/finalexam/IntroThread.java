package org.techtown.finalexam;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends Thread {
    private Handler handler;
    public IntroThread(Handler handler) {
        this.handler = handler;
    }
    //시작 화면이 3초 동안 등장
    @Override
    public void run() {
        Message msg = new Message();
        try {
            Thread.sleep(3000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}