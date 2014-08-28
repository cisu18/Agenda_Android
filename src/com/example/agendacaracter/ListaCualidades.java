package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entidad.Cualidades;
import com.example.reutilizables.AdaptadorCualidades;

public class ListaCualidades extends Activity {
	
	//Declaracion de Variables
	private ListView lista_mensual;
    ArrayList<Cualidades> cualidades;
    private ArrayAdapter<Cualidades> adaptadorlista;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_cualidades);
		
		//Enlazar controles
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Myriad_Pro.ttf");
        TextView lista_cualidades = (TextView)findViewById(R.id.textView1);
        lista_cualidades.setTypeface(miPropiaTypeFace);
      
        lista_mensual=(ListView)findViewById(R.id.listView1);
        
        //llamar al web service
        new ReadCualidadesJSONFeedTask().execute("http://192.168.0.55/Agenda_WS/cualidad/cualidades/format/json");

        //Registar listview para menu contextual
        registerForContextMenu(lista_mensual);
        
        //mensaje de ayuda
        lista_mensual.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		//Object o = lista_mensual.getItemAtPosition(position);
        		//Cualidades fullObject = (Cualidades)o;
        		//Toast.makeText(ListaCualidades.this, "Realiza un toque largo para ver las opciones :D ", Toast.LENGTH_LONG).show();
				openContextMenu(v);
			}  
        });
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_cualidades, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);*/
		switch (item.getItemId()) {
        case R.id.action_inicio:
            Intent mostrarAct = new Intent(this, MainActivity.class);
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
	
	Cualidades cu=new Cualidades();
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info =
	            (AdapterView.AdapterContextMenuInfo)menuInfo;
		
		cu=(Cualidades)lista_mensual.getAdapter().getItem(info.position);
	        menu.setHeaderTitle(cu.getCualidad());
		    MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menu_contextual_cualidades, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info =
		        (AdapterContextMenuInfo) item.getMenuInfo();
    	String id=cu.getId();	
		    switch (item.getItemId()) {
		        
		        case R.id.opcVerLibros:
		        	Intent i=new Intent(this, Referencia.class);
		        	i.putExtra("id cualidad", id);
		        	startActivity(i);
		        	return true;
		        default:
		        	return super.onContextItemSelected(item);

		    }
				
	}
	
	


	private class ReadCualidadesJSONFeedTask extends AsyncTask <String, Void, String> {
        
		protected String doInBackground(String... urls) {
        	return readJSONFeed(urls[0]);
        }
 
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
            	JSONObject datos=new JSONObject();
                cualidades=new ArrayList<Cualidades>();
                
                for (int i = 0; i < jsonArray.length(); i++) {
                	Cualidades c=new Cualidades();
                    datos=jsonArray.getJSONObject(i);
                	c.setId(datos.getString("id"));
                	c.setCualidad(datos.getString("cualidad"));
                	c.setMes(datos.getString("mes"));
                	cualidades.add(c);
					
				}
                                
                lista_mensual.setAdapter(new AdaptadorCualidades(getApplicationContext(), cualidades));
                //adaptadorlista=new ArrayAdapter<Cualidades>(getApplicationContext(), android.R.layout.simple_list_item_1, cualidades);
                //lista_mensual.setAdapter(adaptadorlista);
               
            } catch (Exception e) {
                Log.d("ReadCualidadesJSONFeedTask", e.getLocalizedMessage());
            }          
        }
    }
	
	public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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



