package pl.recordit.kantor;

import android.util.Log;

public class MyLogger extends Thread{
    @Override
    public void run() {
        int count = 10;
        while(count-- > 0){
            Log.i("THREAD","Logujemy!!! " + count);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
