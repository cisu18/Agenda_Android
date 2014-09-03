package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity implements OnClickListener {

	public EditText usuario;
	public EditText contrasenia;


	
    private ProgressDialog pDialog;
	    
	    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Myriad_Pro.ttf");

		usuario = (EditText) findViewById(R.id.txtUserName);
		usuario.setTypeface(miPropiaTypeFace);

		contrasenia = (EditText) findViewById(R.id.txtPass);
		contrasenia.setTypeface(miPropiaTypeFace);

		TextView logincon = (TextView) findViewById(R.id.txt_cabecera);

		logincon.setTypeface(miPropiaTypeFace);

		Button iniciosesion = (Button) findViewById(R.id.btnRegister);
		iniciosesion.setTypeface(miPropiaTypeFace);

		Button crearcuenta = (Button) findViewById(R.id.btnCrearcuenta);
		crearcuenta.setTypeface(miPropiaTypeFace);

		iniciosesion.setOnClickListener(this);
		crearcuenta.setOnClickListener(this);

		Log.e("logre", "entrar");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// determine which button was pressed:
		switch (v.getId()) {
		case R.id.btnRegister:
			new ReadUsuarioJSONFeedTask()
					.execute("http://192.168.0.55/Agenda_WS/users/login/format/json");
			break;
		case R.id.btnCrearcuenta:
			Intent i = new Intent(this, CrearCuenta.class);
			startActivity(i);
			break;
		default:

			break;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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

	// AsyncTask is a seperate thread than the thread that runs the GUI
	// Any type of networking should be done with asynctask.
	private class ReadUsuarioJSONFeedTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Iniciando Sesion...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			// Check for success tag
			return readJSONFeed(urls[0]);

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				//Log.e("resultad", result.toString());
				// JSONArray jsonArray = new JSONArray(result);
				// Log.i("Email","Email"+jsonArray.toString());

				JSONObject datos = new JSONObject(result);
				//usuarios = new ArrayList<Usuario>();

				int Id = datos.getInt("id");
				String Usuario = datos.getString("usuario");
				String Email = datos.getString("email");

				Intent in = new Intent(getApplicationContext(),
						MainActivity.class);
				in.putExtra("id Usuario", Id);
				in.putExtra("Nombre Usario", Usuario);
				in.putExtra("Email Usuario", Email);

				startActivity(in);

			} catch (Exception e) {
				Log.d("ReadCualidadesJSONFeedTask", e.getLocalizedMessage());
			}
			// dismiss the dialog once product deleted
			pDialog.dismiss();

		}

	}

	public String readJSONFeed(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);

		try {
			String username = usuario.getText().toString();
			String password = contrasenia.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("tipo", "1"));

			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse response = httpClient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.d("JSON", "No se ha podido descargar archivo");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
		}

		return stringBuilder.toString();
	}

}
