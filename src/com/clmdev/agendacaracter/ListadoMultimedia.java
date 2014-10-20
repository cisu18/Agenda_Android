package com.clmdev.agendacaracter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Multimedia;
import com.clmdev.reutilizables.AdaptadorMultimedia;
import com.clmdev.reutilizables.Util;

public class ListadoMultimedia extends Activity {

	ArrayList<Multimedia> listadoMultimedias;

	AdaptadorMultimedia adaptadorMultimedia;
	public String tipo = "";
	public String nombreCualidad = "";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_listado_multimedia);
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		listadoMultimedias = new ArrayList<Multimedia>();
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		Bundle bundle = getIntent().getExtras();
		String idCualidad = bundle.getString("id cualidad");
		tipo = bundle.getString("tipo multimedia");
		nombreCualidad = bundle.getString("Nombre Cualidad");
		
		
		if (tipo.equals("2")) {
			txvCabeceraDescripcion.setText("PELÍCULAS Y SERIES");
		}
		if (tipo.equals("3")) {
			txvCabeceraDescripcion.setText("AUDIOS Y CONFERENCIAS");
		}
		Log.e("ids", idCualidad + " " + tipo);

		final String url = getResources().getString(R.string.url_web_service);
		new JSONAsyncTask().execute(url
				+ "multimedia/lista_multimedia_bycualidadtipo/cualidad/"
				+ idCualidad + "/tipo/" + tipo + "/format/json");

		final ListView lsvListaMultimedia = (ListView) findViewById(R.id.lsv_Lista_peliculas);
		adaptadorMultimedia = new AdaptadorMultimedia(getApplicationContext(),
				R.layout.custom_row_multimedia, listadoMultimedias);

		lsvListaMultimedia.setAdapter(adaptadorMultimedia);

		lsvListaMultimedia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String idMultimedia = listadoMultimedias.get(position)
						.getIdMultimedia();
				String urlImagenMultimedia = listadoMultimedias.get(position)
						.getUrlImagenMultimedia();
				Intent intent = new Intent(getApplicationContext(),
						DescripcionMultimedia.class);
				intent.putExtra("id multimedia", idMultimedia);
				intent.putExtra("url multimedia", urlImagenMultimedia);
				intent.putExtra("tipo", tipo);
				startActivity(intent);
			}

		});
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
					multimedia.setUrlImagenMultimedia(object.getString("img"));
					multimedia.setGeneroMultimedia(object.getString("genero"));
					listadoMultimedias.add(multimedia);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(
						getApplicationContext(),
						"No se encontraron recursos relacionados con "+nombreCualidad,
						Toast.LENGTH_LONG).show();
			}

			Util.cerrarDialogLoad();

		}
	}
}
