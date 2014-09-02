package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Pensamiento extends Activity  implements OnClickListener {
	TextView txtPensamiento;
	TextView txtAutor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pensamiento);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		TextView title = (TextView) findViewById(R.id.txt_cabecera);
		title.setTypeface(miPropiaTypeFace);

		txtPensamiento = (TextView) findViewById(R.id.txt_pensamiento);
		txtPensamiento.setTypeface(miPropiaTypeFace);

		txtAutor = (TextView) findViewById(R.id.txt_autorPensamiento);
		txtAutor.setTypeface(miPropiaTypeFace);
		
		TextView registrarse = (TextView) findViewById(R.id.txt_Evaluacion);		
		registrarse.setOnClickListener(this);
		
		String fecha = getFechaActual().substring(0, 5);
		fecha="01-04";
		
		new ReadJSONFeedTask().execute("http://192.168.0.55/Agenda_WS/cualidad_dia/pensamiento/format/json/fecha/"+fecha);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_Evaluacion:
			Toast t = Toast.makeText(getApplicationContext(),"Funciona el textview", Toast.LENGTH_SHORT);
			t.show();
			break;
		case R.id.txt_LibroReferencia:
			Toast ti = Toast.makeText(getApplicationContext(),"Funciona el textview 2", Toast.LENGTH_SHORT);
			ti.show();
			break;
		}
		
	}
	
	public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);        
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
				datos = jsonArray.getJSONObject(0);				
				
				Toast t = Toast.makeText(getApplicationContext(),getFechaActual(), Toast.LENGTH_SHORT);
				t.show();
				txtPensamiento.setText("\""+datos.getString("pensamiento")+"\"");
				txtAutor.setText(datos.getString("autorpensamiento"));
						
				
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
				Log.e("JSON", "No se ha podido descargar archivo");
			}
		} catch (Exception e) {
			Log.e("readJSONFeed", e.getLocalizedMessage());
		}
		
		return stringBuilder.toString();
	}	
}
