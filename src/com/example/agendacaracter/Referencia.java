package com.example.agendacaracter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.entidad.Libro;
import com.example.reutilizables.AdaptadorLibro;
import com.example.reutilizables.Util;
import android.app.Activity;
import android.content.Intent;
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

	ArrayList<Libro> listadoLibros;

	AdaptadorLibro adaptadorLibros;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_referencia);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		listadoLibros = new ArrayList<Libro>();

		Bundle bundle = getIntent().getExtras();
		String idCualidad = bundle.getString("id cualidad");

		new JSONAsyncTask()
				.execute("http://192.168.0.55/Agenda_WS/cualidad_libro/lista_libro_bycualidad/format/json/id/"
						+ idCualidad);

		final GridView grv_Libros_Referencia = (GridView) findViewById(R.id.grv_libros_referencia);
		adaptadorLibros = new AdaptadorLibro(getApplicationContext(),
				R.layout.custom_row_lista_libros_referencia, listadoLibros);

		grv_Libros_Referencia.setAdapter(adaptadorLibros);

		grv_Libros_Referencia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				String idLibro = listadoLibros.get(position).getIdLibro();
				String url = listadoLibros.get(position).getUrlImagen();
				Intent i = new Intent(getApplicationContext(),
						DescripcionLibro.class);
				i.putExtra("id libro", idLibro);
				i.putExtra("url", url);
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

			adaptadorLibros.notifyDataSetChanged();
			try {
				JSONObject object = new JSONObject();
				JSONArray jarray = new JSONArray(result);

				for (int i = 0; i < jarray.length(); i++) {
					object = jarray.getJSONObject(i);
					Libro libro = new Libro();
					libro.setIdLibro(object.getString("libro_id"));
					libro.setTitulo(object.getString("titulo"));
					libro.setUrlImagen(object.getString("urlimg"));
					listadoLibros.add(libro);
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
