package com.clmdev.reutilizables;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clmdev.agendacaracter.R;
import com.clmdev.entidad.Multimedia;
import com.squareup.picasso.Picasso;

public class AdaptadorMultimedia extends ArrayAdapter<Multimedia> {
	ArrayList<Multimedia> listMultimedia;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	Typeface TituloLibro;
	Typeface PrincipalLibro;
	Typeface miDescripcionTypeFace;
	public AdaptadorMultimedia(Context context, int resource,
			ArrayList<Multimedia> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		listMultimedia = objects;

		TituloLibro = Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
		PrincipalLibro = Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");	

		miDescripcionTypeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/GeosansLight_2.ttf");

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.ivMultimediaImagen = (ImageView) v
					.findViewById(R.id.imv_imagen_multimedia);
			holder.tvTitulo = (TextView) v
					.findViewById(R.id.txv_nombre_multimedia);
			holder.tvAutor = (TextView) v
					.findViewById(R.id.txv_autor_multimedia);
			holder.tvAnio = (TextView) v
					.findViewById(R.id.txv_edicion_multimedia);
			holder.tvIdMultimedia = (TextView) v
					.findViewById(R.id.txv_id_multimedia);

			
			TextView nombre = (TextView) v
					.findViewById(R.id.txv_nombre_multimedia);
			nombre.setTypeface(PrincipalLibro);
			TextView autor = (TextView) v
					.findViewById(R.id.txv_autor_multimedia);
			autor.setTypeface(TituloLibro);
			TextView anio = (TextView) v
					.findViewById(R.id.txv_edicion_multimedia);
			anio.setTypeface(miDescripcionTypeFace);


			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Picasso.with(getContext()).load(getItem(position).getUrlImagenMultimedia()).into(holder.ivMultimediaImagen);
		holder.tvTitulo.setText(listMultimedia.get(position)
				.getTituloMultimedia());			
		holder.tvAutor.setText(listMultimedia.get(position).getAutorMultimedia());
		holder.tvAnio.setText(listMultimedia.get(position).getAnioMultimedia());
		

		return v;

	}

	static class ViewHolder {
		public ImageView ivMultimediaImagen;
		public TextView tvTitulo;
		public TextView tvAutor;
		public TextView tvAnio;
		public TextView tvIdMultimedia;
		public TextView tvGeneroEditable;
		
	}

}
