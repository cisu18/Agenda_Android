package com.example.agendacaracter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);	
        /*Intent i = new Intent(this,MainActivity.class);

		startActivity(i);
        
        Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
		
        TextView title = (TextView)findViewById(R.id.textView1);
		title.setTypeface(miPropiaTypeFace);
		
		TextView cualidad = (TextView)findViewById(R.id.textView2);
		cualidad.setTypeface(miPropiaTypeFace);
		
		TextView versiculo = (TextView)findViewById(R.id.textView5);
		versiculo.setTypeface(miPropiaTypeFace);
		
		TextView textobiblico = (TextView)findViewById(R.id.textView6);
		textobiblico.setTypeface(miPropiaTypeFace);
		
		TextView planlectura = (TextView)findViewById(R.id.textView3);
		planlectura.setTypeface(miPropiaTypeFace);
		
		TextView textos = (TextView)findViewById(R.id.textView4);
		textos.setTypeface(miPropiaTypeFace);*/
       
		estaConectado();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
          //  return true;
        //}
        //return super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
	        case R.id.action_listar:
	            Intent mostrarAct = new Intent(this, ListaCualidades.class);
	            startActivity(mostrarAct);
	            finish();
	            return true;
	        case R.id.action_cerrar_sesion:
	        	Intent mostrarPreg = new Intent(this, Login.class);
	            startActivity(mostrarPreg);
	            finish();
	            return true;
	        default:
	            return false;
    	}
    }
    
    
	protected Boolean estaConectado(){
    	if(conectadoWifi()){
	    	//showAlertDialog(MainActivity.this, "Conexion a Internet",
	    	//"Tu Dispositivo tiene Conexion a Wifi.", true);
	    	return true;
    	}else{
	    	if(conectadoRedMovil()){
		    	//showAlertDialog(MainActivity.this, "Conexion a Internet",
		    	//"Tu Dispositivo tiene Conexion Movil.", true);
		    	return true;
	    	}
			else{
				showAlertDialog(MainActivity.this, "Conexion a Internet",
						"Tu Dispositivo necesita una conexion a internet.", false);
						
				return false;
				
			}
    	}
	}
    
    protected Boolean conectadoWifi(){
    	ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (connectivity != null) {
    	NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    	if (info != null) {
		    	if (info.isConnected()) {
		    	return true;
		    	}
	    	}
    	}
    	return false;
	}
    	 
	protected Boolean conectadoRedMovil(){
	ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
		NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) {
				if (info.isConnected()) {
				return true;
				}
			}
		}
	return false;
	}
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
    	AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    	alertDialog.setTitle(title);
    	alertDialog.setMessage(message);
    	alertDialog.setIcon((status) ? R.drawable.ic_action_accept : R.drawable.ic_action_cancel);
    	 
	    	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    	
	    	public void onClick(DialogInterface dialog, int which) {
	    	 finish();
	    	}
	    	
	    	});
    	 
    	alertDialog.show();
	}   	
    
   
}
