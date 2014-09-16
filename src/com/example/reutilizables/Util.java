package com.example.reutilizables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
public class Util {
	
	public static ProgressDialog dialog;

	
	@SuppressLint("SimpleDateFormat") public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(ahora);
	}
	
	public static void compartir(Context context,String title,String post) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, post+"\n\nAgenda Carácter\nCLM Developers - CLM Editores");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, "Compartir en"));
	}
	
	public static String readJSONFeed(String url, Context context) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);		
		try {
			HttpResponse response = httpClient.execute(httpGet);
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
				
				Toast.makeText(context, "Util: Error Interno -> readJSONFeed. Status Code", Toast.LENGTH_SHORT).show();
				
			}
		} catch (Exception e) {
			Toast.makeText(context, "Util: Error Interno -> readJSONFeed. "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return stringBuilder.toString();
	}
	public static void MostrarDialog(Context contexto){
		
		dialog = new ProgressDialog(contexto);
		dialog.setMessage("Cargando, profavor espere");
		dialog.setTitle("Conectando con el servidor");
		dialog.show();
		dialog.setCancelable(false);
		
	}
	public static void CerrarDialog(){
		
		dialog.dismiss();
	}	
	
	
	
	
	
	
	
	
}
