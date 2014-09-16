package com.example.agendacaracter;

import java.io.IOException;
import java.io.InputStream;
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
import com.example.reutilizables.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DescripcionLibro extends Activity {
	private ImageView imgLibro;
	private TextView descripcionlibro;
	ArrayList<Libro> listadoLibro;
	DescargarImagen adapter;

	Libro libro = new Libro();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripcion_libro);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		
		Typeface miDescripcionTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");


		TextView title = (TextView) findViewById(R.id.txv_cabecera);

		title.setTypeface(miPropiaTypeFace);

		descripcionlibro = (TextView) findViewById(R.id.txt_descripcion_libro);
		descripcionlibro.setTypeface(miDescripcionTypeFace);

		TextView reservacuenta = (TextView) findViewById(R.id.txv_nombre_cualidad);
		reservacuenta.setTypeface(miPropiaTypeFace);

		Button btnaqui = (Button) findViewById(R.id.btn_reserva_aqui);
		btnaqui.setTypeface(miPropiaTypeFace);

		TextView redesociales = (TextView) findViewById(R.id.txv_label_plan_lectura);
		redesociales.setTypeface(miPropiaTypeFace);

		imgLibro = (ImageView) findViewById(R.id.img_libro_descripcion);

		Bundle bundle = getIntent().getExtras();
		String idLibro = bundle.getString("id libro");
		String url = bundle.getString("url");
		
		new JSONAsyncTask()
				.execute("http://192.168.0.55/Agenda_WS/libro/libro_byid/format/json/id/"
						+ idLibro);
		new DescargarImagen().execute(url);

	}
	
	class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(DescripcionLibro.this);
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {

				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);

					listadoLibro = new ArrayList<Libro>();
					JSONObject object = new JSONObject();
					JSONArray jarray = new JSONArray(data);

					object = jarray.getJSONObject(0);

					libro.setIdLibro(object.getString("libro_id"));
					libro.setTitulo(object.getString("titulo"));
					libro.setUrlImagen(object.getString("urlimg"));
					libro.setDescripcionLibro(object.getString("descripcion"));
					listadoLibro.add(libro);

					return true;
				}

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
			
			descripcionlibro.setText(libro.getDescripcionLibro());
			if (result == false)
				Toast.makeText(getApplicationContext(),
						"Unable to fetch data from server", Toast.LENGTH_LONG)
						.show();

		}
	}

	public class DescargarImagen extends AsyncTask<String, Void, Bitmap> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap Imagen_Libro_Bitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				Imagen_Libro_Bitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return Imagen_Libro_Bitmap;
		}

		protected void onPostExecute(Bitmap result) {

			imgLibro.setImageBitmap(result);
			Util.cerrarDialogLoad();
		}

	}

}
