package com.clmdev.reutilizables;

import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
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
			holder.tvGenero = (TextView) v
					.findViewById(R.id.txv_genero_multimedia);
			holder.tvIdMultimedia = (TextView) v
					.findViewById(R.id.txv_id_multimedia);
			holder.tvGeneroEditable=(TextView) v.findViewById(R.id.txv_genero);
			
			TextView nombre = (TextView) v
					.findViewById(R.id.txv_nombre_multimedia);
			nombre.setTypeface(TituloLibro);

			TextView url = (TextView) v
					.findViewById(R.id.txv_genero_multimedia);
			url.setTypeface(TituloLibro);
			
			TextView titulo = (TextView) v.findViewById(R.id.txv_titulo);
			titulo.setTypeface(PrincipalLibro);
			
			TextView genero = (TextView) v.findViewById(R.id.txv_genero);
			genero.setTypeface(PrincipalLibro);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Picasso.with(getContext()).load(getItem(position).getUrlImagenMultimedia()).into(holder.ivMultimediaImagen);
		holder.tvTitulo.setText(listMultimedia.get(position)
				.getTituloMultimedia());
		if(listMultimedia.get(position).getGeneroMultimedia().equals("")){
			holder.tvGeneroEditable.setText("");
			
		}else {
			holder.tvGeneroEditable.setText("Género:");
			holder.tvGenero.setText(listMultimedia.get(position).getGeneroMultimedia());
		}

		return v;

	}

	static class ViewHolder {
		public ImageView ivMultimediaImagen;
		public TextView tvTitulo;
		public TextView tvGenero;
		public TextView tvIdMultimedia;
		public TextView tvGeneroEditable;
		
	}

}
