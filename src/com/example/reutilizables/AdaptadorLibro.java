package com.example.reutilizables;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agendacaracter.R;
import com.example.entidad.Libro;

public class AdaptadorLibro extends ArrayAdapter<Libro> {
	ArrayList<Libro> listLibro;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public AdaptadorLibro(Context context, int resource,
			ArrayList<Libro> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		listLibro = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.ivLibroImagen = (ImageView) v.findViewById(R.id.ivImgLibro);
			holder.tvTituloLibro = (TextView) v.findViewById(R.id.tvTitLibro);
			holder.tvIdLibro = (TextView) v.findViewById(R.id.tvidLibroxml);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.ivLibroImagen.setImageResource(R.drawable.ic_launcher);
		new DownloadImageTask(holder.ivLibroImagen).execute(listLibro.get(
				position).getUrlImagen());
		holder.tvTituloLibro.setText(listLibro.get(position).getTitulo());
		return v;

	}

	static class ViewHolder {
		public ImageView ivLibroImagen;
		public TextView tvTituloLibro;
		public TextView tvIdLibro;

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap bitimagen = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				bitimagen = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return bitimagen;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}

	}
}