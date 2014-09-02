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
import org.json.JSONObject;


import com.example.entidad.Diario;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Pensamiento extends Activity {
	TextView txtPensamiento;
	TextView txtAutor;
	ArrayList<Diario> lstDiario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pensamiento);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		TextView title = (TextView) findViewById(R.id.textView1);
		title.setTypeface(miPropiaTypeFace);

		txtPensamiento = (TextView) findViewById(R.id.txt_Pensamiento);
		txtPensamiento.setTypeface(miPropiaTypeFace);

		txtAutor = (TextView) findViewById(R.id.txt_AutorPensamiento);
		txtAutor.setTypeface(miPropiaTypeFace);
		String fecha ="01/01/2015";
		
		
		new ReadJSONFeedTask().execute("http://192.168.0.55/Agenda_WS/cualidad_dia/pensamiento/format/json/fecha/"+fecha);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pensamiento, menu);
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

	private class ReadJSONFeedTask extends
			AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				JSONObject datos = new JSONObject();
				lstDiario = new ArrayList<Diario>();

				Diario d = new Diario();
				datos = jsonArray.getJSONObject(0);
				d.setPensamiento(datos.getString("pensamiento"));
				d.setAutorPensamiento(datos.getString("autorpensamiento"));					
				lstDiario.add(d);
				
				txtPensamiento.setText("\""+d.getPensamiento()+"\"");
				txtAutor.setText(d.getAutorPensamiento());
				
				
				
//				lista_mensual.setAdapter(new AdaptadorCualidades(
//						getApplicationContext(), cualidades));
				// adaptadorlista=new
				// ArrayAdapter<Cualidades>(getApplicationContext(),
				// android.R.layout.simple_list_item_1, cualidades);
				// lista_mensual.setAdapter(adaptadorlista);

			} catch (Exception e) {
				Log.e("onPostExecute", e.getLocalizedMessage());
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
		Log.e("Pensamiento",stringBuilder.toString());
		return stringBuilder.toString();
	}
}
