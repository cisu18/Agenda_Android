package com.example.agendacaracter;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.entidad.Libro;
import com.example.reutilizables.AdaptadorLibro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class Referencia extends Activity {
	
	ArrayList<Libro> actorsList;
	
	AdaptadorLibro adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_referencia);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		

		TextView usuario = (TextView)findViewById(R.id.txt_cabecera);

		usuario.setTypeface(miPropiaTypeFace);

		actorsList = new ArrayList<Libro>();
		
		Bundle bundle = getIntent().getExtras();
		String idCualidad=bundle.getString("id cualidad");
		
		new JSONAsyncTask().execute("http://192.168.0.55/Agenda_WS/cualidad_libro/lista_libro_bycualidad/format/json/id/"+idCualidad);
		
		GridView listview = (GridView)findViewById(R.id.list);
		adapter = new AdaptadorLibro(getApplicationContext(), R.layout.row, actorsList);
		
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), actorsList.get(position).getTitulo(), Toast.LENGTH_LONG).show();				
			}
		});
	}


	class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
		
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Referencia.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);
		}
		
		@Override
		protected Boolean doInBackground(String... urls) {
			try {
				
				//------------------>>
				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					
				
					JSONObject object = new JSONObject();
					JSONArray jarray = new JSONArray(data);
					
					for (int i = 0; i < jarray.length(); i++) {
						object = jarray.getJSONObject(i);
					
						Libro actor = new Libro();
						
						actor.setIdLibro(object.getString("libro_id"));
						actor.setTitulo(object.getString("titulo"));						
						actor.setUrlImagen(object.getString("urlimg"));
						
						actorsList.add(actor);
					}
					return true;
				}
				
				//------------------>>
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result) {
			dialog.cancel();
			adapter.notifyDataSetChanged();
			if(result == false)
				Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

		}
	}
	
	
}

