package com.example.agendacaracter;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.entidad.Cualidades;
import com.example.entidad.Libro;
import com.example.reutilizables.AdaptadorCualidades;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Referencia extends Activity {
	private GridView lista_libros;
	
	ArrayList<Libro> libros;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_referencia);
		
		/*Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		

		TextView usuario = (TextView)findViewById(R.id.txt_cabecera);

		usuario.setTypeface(miPropiaTypeFace);
		*/
		lista_libros=(GridView)findViewById(R.id.gvLibros);
		
		Bundle bundle = getIntent().getExtras();
		String idCualidad=bundle.getString("id cualidad");
		new ReadLibrosJSONFeedTask().execute("http://192.168.0.55/Agenda_WS/cualidad_libro/lista_libro_bycualidad/format/json/id/"+idCualidad);
		
		

        //bundle.getString("id cualidad");
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.referencia, menu);
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
	
	private class ReadLibrosJSONFeedTask extends
	AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {

				JSONArray jsonArray = new JSONArray(result);
				
				JSONObject datos = new JSONObject();
				libros = new ArrayList<Libro>();

				for (int i = 0; i < jsonArray.length(); i++) {
					Libro l = new Libro();
					datos = jsonArray.getJSONObject(i);
					l.setIdLibro(Integer.parseInt(datos.getString("libro_id")));
					l.setTituloLibro(datos.getString("titulo"));
					l.setUrlImagen(datos.getString("urlimg"));
					libros.add(l);
				}
				Log.e("Entre",datos.toString());

				ArrayAdapter<Libro> adapter=
						new ArrayAdapter<Libro>(getApplicationContext(), android.R.layout.simple_list_item_1, libros);
				
				//ArrayAdapter<Libro> adapter=new ArrayAdapter<Libro>();
				
				lista_libros.setAdapter(adapter);
				
				
				// adaptadorlista=new
				// ArrayAdapter<Cualidades>(getApplicationContext(),
				// android.R.layout.simple_list_item_1, cualidades);
				// lista_mensual.setAdapter(adaptadorlista);

			} catch (Exception e) {
				Log.d("ReadCualidadesJSONFeedTask", e.getLocalizedMessage());
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
