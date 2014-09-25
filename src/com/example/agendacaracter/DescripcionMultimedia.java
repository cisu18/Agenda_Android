package com.example.agendacaracter;

import java.io.InputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.entidad.Multimedia;
import com.example.reutilizables.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DescripcionMultimedia extends Activity {
	private ImageView imvLibroDescripcion;
	private TextView txvDesripcionLibro;
	private TextView txvTituloLibro;
	private TextView txvAutorLibro;
	private TextView txvEdicionLibro;
	ArrayList<Multimedia> listadoMultimedia;
	DescargarImagen adaptadorImagen;

	Multimedia multimedia = new Multimedia();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripcion_libro);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		
		Typeface tfHelveticaBold = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		Typeface miDescripcionTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);
		
		txvAutorLibro  = (TextView) findViewById(R.id.txv_autor_multimedia_descripcion);
		txvAutorLibro.setTypeface(miPropiaTypeFace);

		txvDesripcionLibro = (TextView) findViewById(R.id.txv_descripcion_multimedia);
		txvDesripcionLibro.setTypeface(miDescripcionTypeFace);
		
		txvEdicionLibro = (TextView) findViewById(R.id.txv_edicion_multimedia_descripcion);
		txvEdicionLibro.setTypeface(miDescripcionTypeFace);
		
		txvTituloLibro = (TextView) findViewById(R.id.txv_titulo_multimedia_descripcion);
		txvTituloLibro.setTypeface(tfHelveticaBold);

		TextView txvMensajeReserva = (TextView) findViewById(R.id.txv_mensaje_reserva);
		txvMensajeReserva.setTypeface(miPropiaTypeFace);

		Button btnReservaAqui = (Button) findViewById(R.id.btn_reserva_aqui);
		btnReservaAqui.setTypeface(tfHelveticaBold);

		imvLibroDescripcion = (ImageView) findViewById(R.id.imv_multimedia_descripcion);

		Bundle bundle = getIntent().getExtras();
		String parametroIdMultimedia = bundle.getString("id multimedia");
		String parametroUrlImagenMultimedia = bundle.getString("url multimedia");

		new JSONAsyncTask()
				.execute("http://192.168.0.55/Agenda_WS/multimedia/multimedia_byid/format/json/id/"
						+ parametroIdMultimedia);
		new DescargarImagen().execute(parametroUrlImagenMultimedia);

	}

	class JSONAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(DescripcionMultimedia.this);
		}

		@Override
		protected String doInBackground(String... urls) {

			return Util.readJSONFeed(urls[0], getApplicationContext());
		}

		protected void onPostExecute(String result) {

			try {
				listadoMultimedia = new ArrayList<Multimedia>();
				JSONObject object = new JSONObject();
				JSONArray jarray = new JSONArray(result);

				object = jarray.getJSONObject(0);

				multimedia.setIdMultimedia(object.getString("id"));
				multimedia.setTituloMultimedia(object.getString("titulo"));
				multimedia.setUrlImagenMultimedia(object.getString("img"));
				multimedia.setDescripcionMultimedia(object.getString("descripcion"));
				listadoMultimedia.add(multimedia);
				
				txvEdicionLibro.setText(multimedia.getIdMultimedia());
				txvTituloLibro.setText(multimedia.getTituloMultimedia());
				txvDesripcionLibro.setText(multimedia.getDescripcionMultimedia());
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(
						getApplicationContext(),
						"No se pudieron obtener datos del servidor: Descripcion de libro",
						Toast.LENGTH_LONG).show();
			}

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

			imvLibroDescripcion.setImageBitmap(result);
			Util.cerrarDialogLoad();
		}

	}

}