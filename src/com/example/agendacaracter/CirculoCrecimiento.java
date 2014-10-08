package com.example.agendacaracter;

import java.util.ArrayList;

import com.example.entidad.Circulo;
import com.example.reutilizables.AdaptadorCirculos;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

public class CirculoCrecimiento extends Activity {
	private GridView grvListaCirculos;
	private ArrayList<Circulo> lstCirculos;
	private Circulo circulo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circulo_crecimiento);
		grvListaCirculos = (GridView) findViewById(R.id.grv_Lista_Circulos);
		lstCirculos = new ArrayList<Circulo>();

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		TextView txvCabeceraDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);
		txvCabeceraDescripcion.setTypeface(miPropiaTypeFace);

		circulo = new Circulo(
				"Av. Del Ej�rcito 920 - UPN",
				"Sergio Aroca", "Martes 4:00 pm ", "96892383");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Jos� Euguren", "Pastor Jos� Montoya",
				"Martes/ Mi�rcoles 8:00 pm ", "#398025");
		lstCirculos.add(circulo);
		circulo = new Circulo(
				"Urb. Los Cedros, Calle Agua Marina Mz. K Lt 13 - UCT",
				"Anal� Rodr�guez", "Martes 3:00 pm","951380716");
		lstCirculos.add(circulo);
		circulo = new Circulo("Plaza de Armas", "David Alv�n",
				"Mi�rcoles 6:00 pm", "984328905");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Nicol�s de Pi�rola- Restaurant MASSE", "Jos� Montoya",
				"Mi�rcoles 7:30 pm ", "#969690817");
		lstCirculos.add(circulo);
		circulo = new Circulo("H�sares de Jun�n 1199", "Deisy P�rez Guerra",
				"Mi�rcoles 8:30 pm", "991010298");
		lstCirculos.add(circulo);
		circulo = new Circulo("Urb. Vista Hermosa Condominio San Pablo", "David Segura",
				"Mi�rcoles 8:00 pm", "974608521");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Nicol�s de Pi�rola #1106", "Bred Aponte",
				"Jueves 7:30 pm", "994973073");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. V�ctor Larco #1770 - UCV", "D�nae Segura",
				"Jueves 12:00 pm", "943730039");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Am�rica Sur #3145-UPAO", "Yuri Vargas",
				"Jueves 10:30 am", "#956956876");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Nicol�s de Pi�rola #1106", "Cynthia Quiroz",
				"Jueves 7:30 pm", "#968180816");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. V�ctor Larco #1770 - UCV", "Luis Hurtado",
				"Jueves 12:00 pm", "957824408");
		lstCirculos.add(circulo);
		circulo = new Circulo("Moche","Anal� Rodr�guez", "Viernes 8:00 pm","951380716");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Nicol�s de Pi�rola #1106", "Sergio Aroca",
				"Viernes 8:00 pm", "96892383");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. V�ctor Larco #1770 - UCV", "Luis Hurtado",
				"Viernes 8:00 pm", "957824408");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Am�rica Sur #3145-UPAO", "Juan Carlos Otiniano",
				"Viernes 5:00 pm", "#943478729");
		lstCirculos.add(circulo);
		circulo = new Circulo("H�sares de Jun�n 1199", "Joseph Alc�ntara",
				"S�bado 9:30 am", "#968138990");
		lstCirculos.add(circulo);
		circulo = new Circulo("Direcci�n Alterna", "Richard Oruna",
				"S�bado 2:00 pm", "#980031860");
		lstCirculos.add(circulo);
		circulo = new Circulo("Av. Nicol�s de Pi�rola #1106", "Fam. Montoya Boulangger",
				"S�bado 8:00 pm", "#398030");		
		

		AdaptadorCirculos adapterCirculos = new AdaptadorCirculos(this,
				lstCirculos);

		grvListaCirculos.setAdapter(adapterCirculos);

	}

}