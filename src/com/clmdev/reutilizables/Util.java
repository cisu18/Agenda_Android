package com.clmdev.reutilizables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.clmdev.agendacaracter.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

public class Util {

	public static ProgressDialog dialog;

	@SuppressLint("SimpleDateFormat")
	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		String fech = formateador.format(ahora);
		return fech;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getHoraAlerta() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("HH:mm");
		return formateador.format(ahora);
	}
	
	public static String readJSONFeed(String url, Context context) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream,"utf-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}

				inputStream.close();
			} else {
				Log.e("Error:", "No se descargaron los datos del servidor");
			}
		} catch (Exception e) {
			Log.e("Error readJSONFeed:",
					"No se descargaron los datos del servidor "
							+ e.getMessage());
		}
		return stringBuilder.toString();
	}

	public static String readJSONFeedPost(String url, String username,
			String email, String firstname, String lastname) {

		StringBuilder res = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			List<NameValuePair> parametros = new ArrayList<NameValuePair>(5);
			parametros.add(new BasicNameValuePair("username", username));
			parametros.add(new BasicNameValuePair("email", email));
			parametros.add(new BasicNameValuePair("firstname", firstname));
			parametros.add(new BasicNameValuePair("lastname", lastname));
			parametros.add(new BasicNameValuePair("tipo", "1"));
			httpPost.setEntity(new UrlEncodedFormEntity(parametros,"utf-8"));
			HttpResponse response = httpClient.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
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
			Log.e("Error Util", "readJSONFeedPost:" + e.getMessage());
		}
		return res.toString();
	}

	public static void MostrarDialog(Context contexto) {
		dialog = new ProgressDialog(contexto);
		dialog.setMessage("Cargando, por favor espere");
		dialog.show();
		dialog.setCancelable(false);
	}

	public static void cerrarDialogLoad() {
		dialog.dismiss();
	}
	
	public static void compartir(Activity context,String title,String post) {		
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
	    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	    sharingIntent.setType("text/plain");
	    String shareBody = post;

	    PackageManager pm = context.getPackageManager();
	    List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
	    for(final ResolveInfo app : activityList) {

	         String packageName = app.activityInfo.packageName;
	         Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
	         targetedShareIntent.setType("text/plain");
	         targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
	         if(!TextUtils.equals(packageName, "com.facebook.katana")){
	        	 targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	             targetedShareIntent.setPackage(packageName);
		         targetedShareIntents.add(targetedShareIntent);
	         }


	    }

	    Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Compartir");
	    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
	    context.startActivity(chooserIntent);
	}
	
	public static void mostrarMenuCompartir(Activity v, String t, String p){
		final Activity view= v;
		final String title=t;
		final String post=p;
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                view);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Compartir");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        		view,
                android.R.layout.select_dialog_item);
        arrayAdapter.add("Facebook");
        arrayAdapter.add("Otros");
        builderSingle.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
        		
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);                        
                        if(strName.equals("Facebook")){
                        	new Facebook().publicarMensaje(view, title, post);
                        }else if(strName.equals("Otros")){
                        	compartir(view, title, post);                        	
                        }
                        
                    }
                });
        builderSingle.show();
	}

}
