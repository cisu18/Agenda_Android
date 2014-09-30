package com.example.agendacaracter;

import java.util.ArrayList;

import com.example.entidad.Circulos;
import com.example.reutilizables.AdaptadorCirculos;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

public class CirculoCrecimiento extends Activity {
	GridView grvListaCirculos;
	ArrayList<Circulos> listacirculos;
	Circulos circulo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circulo_crecimiento);
		grvListaCirculos = (GridView) findViewById(R.id.grv_Lista_Circulos);
		listacirculos = new ArrayList<Circulos>();

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		circulo = new Circulos(
				"Av. Del Ej�rcito 920 - Urb. El Molino-UPN",
				"Sergio Aroca", "Martes 4:00 PM ", "96892383");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Jos� Euguren", "Pastor Jos� Montoya",
				"Martes/ Mi�rcoles 8:00 PM ", "#398025");
		listacirculos.add(circulo);
		circulo = new Circulos(
				"Urb. Los Cedros, Calle Agua Marina Mz. K Lt 13 - UCT",
				"Anal� Rodr�guez", "Martes 3:00 PM","951380716");
		listacirculos.add(circulo);
		circulo = new Circulos("Plaza de Armas", "David Alv�n",
				"Mi�rcoles 6:00 PM", "984328905");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Nicol�s de Pierola- Restauran MASSE", "Jos� Montoya",
				"Mi�rcoles 7:30 PM ", "#969690817");
		listacirculos.add(circulo);
		circulo = new Circulos("H�sares de Junin 1199", "Deisy Per�z Guerra",
				"Mi�rcoles 8:30 PM", "991010298");
		listacirculos.add(circulo);
		circulo = new Circulos("Urb. Vista Hermoza Condominio San Pablo", "David Segura",
				"Mi�rcoles 8:00 PM", "974608521");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Nicol�s de Pi�rola #1106", "Bred Aponte",
				"Jueves 7:30 PM", "994973073");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Avenida V�ctor Larco #1770 - UCV", "Danae Segura",
				"Jueves 12:00 PM", "943730039");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Avenida Am�rica Sur #3145-UPAO", "Yuri Vargas",
				"Jueves 10:30 AM", "#956956876");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Nicol�s de Pi�rola #1106", "Cynthia Quiroz",
				"Jueves 7:30 PM", "#968180816");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Avenida V�ctor Larco #1770 - UCV", "Luis Hurtado",
				"Jueves 12:00 PM", "957824408");
		listacirculos.add(circulo);
		circulo = new Circulos("Moche","Anal� Rodr�guez", "Viernes 8:00 PM","951380716");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Nicol�s de Pi�rola #1106", "Sergio Aroca",
				"Viernes 8:00 PM", "96892383");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Avenida V�ctor Larco #1770 - UCV", "Luis Hurtado",
				"Viernes 8:00 PM", "957824408");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Avenida Am�rica Sur #3145-UPAO", "Juan Carlos Otiniano",
				"Viernes 5:00 PM", "#943478729");
		listacirculos.add(circulo);
		circulo = new Circulos("H�sares de Junin 1199", "Joseph Alcantara",
				"S�bado 9:30 AM", "#968138990");
		listacirculos.add(circulo);
		circulo = new Circulos("Sin Direcc�n", "Richard Oruna",
				"S�bado 2:00 PM", "#980031860");
		listacirculos.add(circulo);
		circulo = new Circulos("Av. Nicol�s de Pi�rola #1106", "Fam. Montoya Boulangger",
				"S�bado 8:00 PM", "#398030");		
		

		AdaptadorCirculos adapterCirculos = new AdaptadorCirculos(this,
				listacirculos);

		grvListaCirculos.setAdapter(adapterCirculos);

	}

}