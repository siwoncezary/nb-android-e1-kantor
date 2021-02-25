package pl.recordit.kantor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

public class JsonThread extends Thread {
    String json;
    EditText jsonView;
    Looper looper;

    public JsonThread(EditText jsonView, Looper looper) {
        this.jsonView = jsonView;
        this.looper = looper;
    }

    @Override
    public void run() {
        try {
            json = MainActivity.getJsonFromNBP();
            Handler handler = new Handler(looper);
            Log.i("JSON", json);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    jsonView.setText(json);//odwołujemy się do widoku utworzonego w wątku loopera
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJson() {
        return json;
    }
}
