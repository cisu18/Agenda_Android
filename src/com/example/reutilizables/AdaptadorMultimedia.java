package com.example.reutilizables;

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

import com.example.agendacaracter.R;
import com.example.entidad.Multimedia;

public class AdaptadorMultimedia extends ArrayAdapter<Multimedia> {
	ArrayList<Multimedia> listMultimedia;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	Typeface TituloLibro;

	public AdaptadorMultimedia(Context context, int resource,
			ArrayList<Multimedia> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		listMultimedia = objects;

		TituloLibro = Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.ivMultimediaImagen = (ImageView) v
					.findViewById(R.id.imv_imagen_multimedia);
			holder.tvNombre = (TextView) v
					.findViewById(R.id.txv_nombre_multimedia);
			holder.tvUrl = (TextView) v.findViewById(R.id.txv_url_multimedia);

			TextView nombre = (TextView) v
					.findViewById(R.id.txv_nombre_multimedia);
			nombre.setTypeface(TituloLibro);

			TextView url = (TextView) v.findViewById(R.id.txv_url_multimedia);
			url.setTypeface(TituloLibro);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.ivMultimediaImagen.setImageResource(R.drawable.ic_launcher);
		new DownloadImageTask(holder.ivMultimediaImagen).execute(listMultimedia
				.get(position).getUrlImagenMultimedia());
		holder.tvNombre.setText(listMultimedia.get(position)
				.getNombreMultimedia());
		holder.tvUrl.setText(listMultimedia.get(position).getUrlMultimedia());

		return v;

	}

	static class ViewHolder {
		public ImageView ivMultimediaImagen;
		public TextView tvNombre;
		public TextView tvUrl;

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
				
				int width = bitimagen.getWidth();
				int height = bitimagen.getHeight();
				int newWidth = 70;
				int newHeight = 90;
				
				// calculamos el escalado de la imagen destino
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				// para poder manipular la imagen 
				// debemos crear una matriz
				
				Matrix matrix = new Matrix();
				// resize the Bitmap
				matrix.postScale(scaleWidth, scaleHeight);
				// volvemos a crear la imagen con los nuevos valores
//				Bitmap resizedBitmap
				bitimagen= Bitmap.createBitmap(bitimagen, 0, 0,width, height, matrix, true);
				// si queremos poder mostrar nuestra imagen tenemos que crear un
				// objeto drawable y así asignarlo a un botón, imageview...
				
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return bitimagen;
//			return new BitmapDrawable(resizedBitmap);
			
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}

	}

}
