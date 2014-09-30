package com.example.reutilizables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class Util {
	
	public static ProgressDialog dialog;

	
	@SuppressLint("SimpleDateFormat") public static String getFechaActual() {
		Date ahora = new Date();
		
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		String fech=formateador.format(ahora).substring(0,5)+"-2015";
		Log.e("FECHAS",fech);
		return fech;
	}
	
	@SuppressLint("SimpleDateFormat") public static String getHoraAlerta() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("HH:mm");
		return formateador.format(ahora);
	}
	
	public static void compartir(Context context,String title,String post) {
		String text=post+"\n\n#Agendacarácter\nCLM Deved";
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT,text );
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		context.startActivity(Intent.createChooser(intent, "Compartir en"));
	}
	
	public static String readJSONFeed(String url, Context context){
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
				Log.e("Error:","No se descargarón los datos del servidor");
				
			}
		} catch (Exception e) {
			Log.e("Error readJSONFeed:","No se descargarón los datos del servidor "+e.getMessage());			
		}
		return stringBuilder.toString();
	}
	
	public static String readJSONFeedPost(String url,String username, String password,
			String email, String firstname, String lastname, String ip) {		
		StringBuilder res = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {

			List<NameValuePair> parametros = new ArrayList<NameValuePair>(4);
			parametros.add(new BasicNameValuePair("username", username));
			parametros.add(new BasicNameValuePair("password", password));
			parametros.add(new BasicNameValuePair("email", email));
			parametros.add(new BasicNameValuePair("firstname", firstname));
			parametros.add(new BasicNameValuePair("lastname", lastname));
			parametros.add(new BasicNameValuePair("ip", ip));									
			parametros.add(new BasicNameValuePair("tipo", "1"));
			httpPost.setEntity(new UrlEncodedFormEntity(parametros));
			HttpResponse response = httpClient.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.e("Code",""+statusCode);
			if (statusCode == 200 || statusCode == 404) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) {
					res.append(line);
				}
				inputStream.close();
			} else {
				res.append("error");				
			}
		} catch (Exception e) {			
			Log.e("Error Util","readJSONFeedPost:"+e.getMessage());
		}
		return res.toString();
	}
	
	public static void MostrarDialog(Context contexto){
		
		dialog = new ProgressDialog(contexto);
		dialog.setMessage("Cargando, por favor espere");
		dialog.setTitle("Conectando con el servidor");
		dialog.show();
		dialog.setCancelable(false);
		
	}
	public static void cerrarDialogLoad(){
		
		dialog.dismiss();
	}	
	
	
	
	
	
	
	
	
}
