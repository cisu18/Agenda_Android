package com.example.agendacaracter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.entidad.Multimedia;
import com.example.reutilizables.AdaptadorMultimedia;
import com.example.reutilizables.Util;

public class ListadoMultimedia extends Activity {

	ArrayList<Multimedia> listadoMultimedias;

	AdaptadorMultimedia adaptadorMultimedia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peliculas_series);

		listadoMultimedias = new ArrayList<Multimedia>();

		Bundle bundle = getIntent().getExtras();
		String idCualidad = bundle.getString("id cualidad");
		String tipo = bundle.getString("tipo multimedia");
		Log.e("ids",idCualidad +tipo);
		new JSONAsyncTask()
				.execute("http://192.168.0.55/Agenda_WS/multimedia/lista_multimedia_bycualidadtipo/cualidad/"
						+ idCualidad + "/tipo/" + tipo + "/format/json");

		final ListView lsvListaMultimedia = (ListView) findViewById(R.id.lsv_Lista_peliculas);
		adaptadorMultimedia = new AdaptadorMultimedia(getApplicationContext(),
				R.layout.custom_row_multimedia, listadoMultimedias);

		lsvListaMultimedia.setAdapter(adaptadorMultimedia);

		lsvListaMultimedia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String idMultimedia = listadoMultimedias.get(position)
						.getIdMultimedia();
				String urlImagenMultimedia = listadoMultimedias.get(position)
						.getUrlImagenMultimedia();
				// Intent intent = new
				// Intent(Intent.ACTION_VIEW,Uri.parse(url));
				Intent intent = new Intent(getApplicationContext(),
						DescripcionMultimedia.class);
				intent.putExtra("id multimedia", idMultimedia);
				intent.putExtra("url multimedia", urlImagenMultimedia);
				startActivity(intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.peliculas_series, menu);
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

	class JSONAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(ListadoMultimedia.this);
		}

		@Override
		protected String doInBackground(String... urls) {
			return Util.readJSONFeed(urls[0], getApplicationContext());
		}

		protected void onPostExecute(String result) {

			adaptadorMultimedia.notifyDataSetChanged();
			try {
				JSONObject object = new JSONObject();
				JSONArray jarray = new JSONArray(result);

				for (int i = 0; i < jarray.length(); i++) {
					object = jarray.getJSONObject(i);
					Multimedia multimedia = new Multimedia();
					multimedia.setIdMultimedia(object.getString("id"));
					multimedia.setTituloMultimedia(object.getString("titulo"));
					multimedia.setUrlImagenMultimedia(object
							.getString("img"));
					listadoMultimedias.add(multimedia);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(
						getApplicationContext(),
						"No se pudieron obtener datos del servidor: Peliculas y series",
						Toast.LENGTH_LONG).show();
			}

			Util.cerrarDialogLoad();

		}
	}
}