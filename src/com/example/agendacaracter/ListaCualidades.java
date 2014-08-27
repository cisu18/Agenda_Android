package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaCualidades extends Activity {
	private ListView lista_mensual;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_cualidades);
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
        TextView lista_cualidades = (TextView)findViewById(R.id.textView1);
        lista_cualidades.setTypeface(miPropiaTypeFace);

        lista_mensual=(ListView)findViewById(R.id.listView1);
        
        
        new ReadWeatherJSONFeedTask().execute("http://192.168.0.55/Agenda_WS/cualidad/cualidades/format/json");
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_cualidades, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	private class ReadWeatherJSONFeedTask extends AsyncTask
    <String, Void, String> {
        protected String doInBackground(String... urls) {
        	
            return readJSONFeed(urls[0]);
        }
 
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
            	Log.e("etorno","correcto"); 
                Toast.makeText(getBaseContext(), 
                		jsonArray.toString(),
                 
                 Toast.LENGTH_SHORT).show();
                //ArrayList<E>
                
                //ArrayAdapter<JSONArray> adaptadorlista=new ArrayAdapter<JSONArray>(this, android.R.layout.simple_list_item_1,jsonArray);
                //lista_mensual.setAdapter(adaptadorlista);
            } catch (Exception e) {
                Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
            }          
        }
    }
	
	
	
	
	public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {

                
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("JSON", "No se ha podido descargar archivo");
            }
        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }
	
	
	
	
	
	
	
}



