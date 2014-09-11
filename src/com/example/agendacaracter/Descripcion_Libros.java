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

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Descripcion_Libros extends Activity {
	private ImageView imagenLibro;
	private TextView descripcionlibro;
	ArrayList<Libro> listadoLibro;
	DescargarImagen adapter;

	Libro libro = new Libro();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripcion__libros);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		TextView title = (TextView) findViewById(R.id.txt_cabecera);

		title.setTypeface(miPropiaTypeFace);

		descripcionlibro = (TextView) findViewById(R.id.txt_Descripcion_Libro);
		descripcionlibro.setTypeface(miPropiaTypeFace);

		TextView reservacuenta = (TextView) findViewById(R.id.txt_Nombre_Cualidad);
		reservacuenta.setTypeface(miPropiaTypeFace);

		Button btnaqui = (Button) findViewById(R.id.button1);
		btnaqui.setTypeface(miPropiaTypeFace);

		TextView redesociales = (TextView) findViewById(R.id.lbl_Plan_Lectura);
		redesociales.setTypeface(miPropiaTypeFace);

		imagenLibro = (ImageView) findViewById(R.id.img_Libro_Descripcion);

		Bundle bundle = getIntent().getExtras();
		String idLibro = bundle.getString("id libro");
		String url = bundle.getString("url");
		
		new JSONAsyncTask()
				.execute("http://192.168.0.55/Agenda_WS/libro/libro_byid/format/json/id/"
						+ idLibro);
		new DescargarImagen().execute(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.descripcion__libros, menu);
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

	class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Descripcion_Libros.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			try {

				// ------------------>>
				HttpGet httppost = new HttpGet(urls[0]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// StatusLine stat = response.getStatusLine();
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
					Log.e("Lista", "" + object.getString("urlimg"));

					return true;
				}

				// ------------------>>

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
			descripcionlibro.setText(libro.getDescripcionLibro());
			// adapter.notifyDataSetChanged();
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
			dialog = new ProgressDialog(Descripcion_Libros.this);
			dialog.setMessage("Loading, please wait");
			dialog.setTitle("Connecting server");
			dialog.show();
			dialog.setCancelable(false);
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

			imagenLibro.setImageBitmap(result);
			dialog.dismiss();
		}

	}

}
