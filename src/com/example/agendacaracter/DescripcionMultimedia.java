package com.example.agendacaracter;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entidad.Multimedia;
import com.example.reutilizables.Util;

public class DescripcionMultimedia extends Activity {
	private ImageView imvMultimediaDescripcion;
	private TextView txvDesripcionMultimedia;
	private TextView txvTituloMultimedia;
	private TextView txvAutorMultimedia;
	private TextView txvEdicionMultimedia;
	ArrayList<Multimedia> lstMultimedia;
	

	Multimedia multimedia = new Multimedia();
	public String parametroTipo;
	public String parametroIdMultimedia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripcion_multimedia);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface tfHelveticaBold = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		Typeface miDescripcionTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/GeosansLight_2.ttf");

		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		txvAutorMultimedia = (TextView) findViewById(R.id.txv_autor_multimedia_descripcion);
		txvAutorMultimedia.setTypeface(miPropiaTypeFace);

		txvDesripcionMultimedia = (TextView) findViewById(R.id.txv_descripcion_multimedia);
		txvDesripcionMultimedia.setTypeface(miDescripcionTypeFace);

		txvEdicionMultimedia = (TextView) findViewById(R.id.txv_edicion_multimedia_descripcion);
		txvEdicionMultimedia.setTypeface(miDescripcionTypeFace);

		txvTituloMultimedia = (TextView) findViewById(R.id.txv_titulo_multimedia_descripcion);
		txvTituloMultimedia.setTypeface(tfHelveticaBold);

		TextView txvMensajeReserva = (TextView) findViewById(R.id.txv_mensaje_reserva);
		txvMensajeReserva.setTypeface(miPropiaTypeFace);

		Button btnReservaAqui = (Button) findViewById(R.id.btn_reserva_aqui);
		btnReservaAqui.setTypeface(tfHelveticaBold);		
		btnReservaAqui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences prefe = getSharedPreferences("user",
						Context.MODE_PRIVATE);
				int idUsuario = Integer.parseInt(prefe.getString("id", "0"));
				Intent reserva = new Intent(getApplicationContext(),
						CodigoReserva.class);
				reserva.putExtra("idMultimedia", parametroIdMultimedia);
				reserva.putExtra("idUsuario", String.valueOf(idUsuario));
				startActivity(reserva);
			}
		});
		//Oculta los botones de "Reserva"
		btnReservaAqui.setVisibility(View.GONE);
		txvMensajeReserva.setVisibility(View.GONE);		

		imvMultimediaDescripcion = (ImageView) findViewById(R.id.imv_multimedia_descripcion);

		Bundle bundle = getIntent().getExtras();
		parametroIdMultimedia = bundle.getString("id multimedia");
		String parametroUrlImagenMultimedia = bundle
				.getString("url multimedia");
		parametroTipo = bundle.getString("tipo");

		if (parametroTipo.equals("2")) {
			txvCabeceraDescripcion.setText("SINOPSIS");
		}
		if (parametroTipo.equals("3")) {
			txvCabeceraDescripcion.setText("DESCRIPCI”N DEL AUDIO");
		}
		final String url = getResources().getString(R.string.url_web_service);
		new JSONAsyncTask().execute(url
				+ "multimedia/multimedia_byid/format/json/id/"
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
				lstMultimedia = new ArrayList<Multimedia>();
				JSONObject object = new JSONObject();
				JSONArray jarray = new JSONArray(result);

				object = jarray.getJSONObject(0);

				if (parametroTipo.equals("1")) {
					txvTituloMultimedia.setText(object.getString("titulo"));
					txvAutorMultimedia.setText(object.getString("autor"));
					txvEdicionMultimedia.setText(object.getString("anio"));
					txvDesripcionMultimedia.setText(object
							.getString("descripcion"));

				}
				if (parametroTipo.equals("2")) {
					txvTituloMultimedia.setText(object.getString("titulo"));
					txvAutorMultimedia.setText(object.getString("autor"));
					txvEdicionMultimedia.setText(object.getString("anio"));

					txvDesripcionMultimedia.setText(object
							.getString("descripcion"));

				}
				if (parametroTipo.equals("3")) {
					txvTituloMultimedia.setText(object.getString("titulo"));
					txvAutorMultimedia.setText(object.getString("autor"));
					txvEdicionMultimedia.setText(object.getString("anio"));
					txvDesripcionMultimedia.setText(object
							.getString("descripcion"));

				}

			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"No se pudieron obtener datos del servidor: Descripci”n de libro",
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

			imvMultimediaDescripcion.setImageBitmap(result);
			Util.cerrarDialogLoad();
		}

	}

}
