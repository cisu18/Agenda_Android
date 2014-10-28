package com.clmdev.agendacaracter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Multimedia;
import com.clmdev.reutilizables.Util;
import com.squareup.picasso.Picasso;

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
	public String parametroUrlImagenMultimedia;

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
//				SharedPreferences prefe = getSharedPreferences("user",
//						Context.MODE_PRIVATE);
//				int idUsuario = Integer.parseInt(prefe.getString("id", "0"));
//				Intent reserva = new Intent(getApplicationContext(),
//						CodigoReserva.class);
//				reserva.putExtra("idMultimedia", parametroIdMultimedia);
//				reserva.putExtra("idUsuario", String.valueOf(idUsuario));
//				startActivity(reserva);
				
				String url = "http://clmeditores.com/contacto/"; 
				Intent i = new Intent(Intent.ACTION_VIEW); 
				i.setData(Uri.parse(url)); startActivity(i);
			}
		});		

		imvMultimediaDescripcion = (ImageView) findViewById(R.id.imv_multimedia_descripcion);

		Bundle bundle = getIntent().getExtras();
		parametroIdMultimedia = bundle.getString("id multimedia");
		parametroUrlImagenMultimedia = bundle
				.getString("url multimedia");
		parametroTipo = bundle.getString("tipo");

		if (parametroTipo.equals("2")) {
			txvCabeceraDescripcion.setText("SINOPSIS");
		}
		if (parametroTipo.equals("3")) {
			txvCabeceraDescripcion.setText("DESCRIPCIÓN DEL AUDIO");
		}

		usuarioConectado();
		
				

	}
	
	public void usuarioConectado() {
		if (estaConectado()) {

			final String url = getResources().getString(R.string.url_web_service);
			new JSONAsyncTask().execute(url
					+ "multimedia/multimedia_byid/format/json/id/"
					+ parametroIdMultimedia);
		
			Picasso.with(getApplicationContext()).load(parametroUrlImagenMultimedia).into(imvMultimediaDescripcion);

		} else {
			showAlertDialog(DescripcionMultimedia.this, "Conexión a Internet",
					"Tu Dispositivo necesita una conexión a internet.", false);
		}
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
						"No se pudieron obtener datos del servidor: DescripciÓn de libro",
						Toast.LENGTH_LONG).show();
			}
			Util.cerrarDialogLoad();
		}
	}
	
	protected Boolean estaConectado() {
		if (conectadoWifi()) {
			return true;
		} else {
			if (conectadoRedMovil()) {
				return true;
			} else {
				return false;

			}
		}
	}
	protected Boolean conectadoWifi() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	protected Boolean conectadoRedMovil() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon((status) ? R.drawable.ic_action_accept
				: R.drawable.ic_action_cancel);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				 finish();
			}


		});
		alertDialog.show();
		
	}

}
