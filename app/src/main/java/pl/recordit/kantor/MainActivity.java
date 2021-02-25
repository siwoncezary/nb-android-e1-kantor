package pl.recordit.kantor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText jsonView = findViewById(R.id.jsonView);
//        JsonThread jsonThread = new JsonThread(jsonView, getMainLooper());
//        jsonThread.start();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = getJsonFromNBP();
                    Handler handler = new Handler(getMainLooper());
                    List<Rate> rates = parseRatesFromNBMJson(json);
                    StringBuffer sb = new StringBuffer();
                    for(Rate r: rates){
                        sb.append(r.toString());
                        sb.append("\n");
                    }
                    //modyfikacja UI tylko przez Handler!!!!
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            jsonView.setText(sb.toString());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    static public String getJsonFromNBP() throws IOException {
        URL url = new URL("http://api.nbp.pl/api/exchangerates/tables/A?format=json");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("user-agent","kantor");
        if (connection.getResponseCode() == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = reader.lines().collect(Collectors.joining());
            return json;
        }
        return "";
    }

    public List<Rate> parseRatesFromNBMJson(String json) throws JSONException {
        List<Rate> result = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        JSONObject object = array.getJSONObject(0);
        //wyciągnąc datę notowania walut i wyświetlić w aktywności
        JSONArray rates = object.getJSONArray("rates");
        for(int i = 0; i < rates.length(); i++){
            JSONObject rateJson = rates.getJSONObject(i);
            String currency = rateJson.getString("currency");
            String code = rateJson.getString("code");
            double mid = rateJson.getDouble("mid");
            Rate currentRate = new Rate(currency, code, mid);
            result.add(currentRate);
        }
        return result;
    }

}