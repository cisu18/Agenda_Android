package com.clmdev.reutilizables;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class AdaptadorLibro extends ArrayAdapter<Multimedia> {
	ArrayList<Multimedia> listLibro;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	Typeface TituloLibro;

	public AdaptadorLibro(Context context, int resource,
			ArrayList<Multimedia> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		listLibro = objects;

		TituloLibro = Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.ivLibroImagen = (ImageView) v
					.findViewById(R.id.img_item_lista_libros_referencia);
			holder.tvTituloLibro = (TextView) v
					.findViewById(R.id.txv_item_titulo_lista_libros_referencia);
			holder.tvIdLibro = (TextView) v
					.findViewById(R.id.txv_item_id_lista_libros_referencia);

			TextView lista_libros = (TextView) v
					.findViewById(R.id.txv_item_titulo_lista_libros_referencia);
			lista_libros.setTypeface(TituloLibro);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}


		Picasso.with(getContext()).load(getItem(position).getUrlImagenMultimedia()).into(holder.ivLibroImagen);
		holder.tvTituloLibro.setText(listLibro.get(position)
				.getTituloMultimedia());
		return v;

	}

	static class ViewHolder {
		public ImageView ivLibroImagen;
		public TextView tvTituloLibro;
		public TextView tvIdLibro;
	}

}