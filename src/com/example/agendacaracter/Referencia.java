package com.example.agendacaracter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.agendacaracter.ListadoMultimedia.JSONAsyncTask;
import com.example.entidad.Multimedia;
import com.example.reutilizables.AdaptadorLibro;
import com.example.reutilizables.Util;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Referencia extends Activity {

	ArrayList<Multimedia> listadoMultimedia;

	AdaptadorLibro adaptadorMultimedia;
	public String tipo="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_referencia);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		listadoMultimedia = new ArrayList<Multimedia>();

		Bundle bundle = getIntent().getExtras();
		String idCualidad = bundle.getString("id cualidad");
		tipo = bundle.getString("tipo multimedia");
		Log.e("ids",idCualidad +" "+tipo);
		final String url = getResources().getString(R.string.url_web_service);
		new JSONAsyncTask()
		.execute(url+"multimedia/lista_multimedia_bycualidadtipo/cualidad/"
				+ idCualidad + "/tipo/" + tipo + "/format/json");

		final GridView grv_Libros_Referencia = (GridView) findViewById(R.id.grv_libros_referencia);
		adaptadorMultimedia = new AdaptadorLibro(getApplicationContext(),
				R.layout.custom_row_lista_libros_referencia, listadoMultimedia);

		grv_Libros_Referencia.setAdapter(adaptadorMultimedia);

		grv_Libros_Referencia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				String idMultimedia = listadoMultimedia.get(position).getIdMultimedia();
				String urlImagenMultimedia = listadoMultimedia.get(position).getUrlImagenMultimedia();
				Intent i = new Intent(getApplicationContext(),
						DescripcionMultimedia.class);
				i.putExtra("id multimedia", idMultimedia);
				i.putExtra("url multimedia", urlImagenMultimedia);
				i.putExtra("tipo", tipo);
				startActivity(i);
			}
		});
	}

	class JSONAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(Referencia.this);
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
					listadoMultimedia.add(multimedia);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(
						getApplicationContext(),
						"No se pudieron obtener datos del servidor: Libros de referencia",
						Toast.LENGTH_LONG).show();
			}

			Util.cerrarDialogLoad();

		}
	}

}
