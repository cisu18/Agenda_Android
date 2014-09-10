package com.example.reutilizables;

import android.content.Context;
import android.content.Intent;

public class Social {

	public static void compartir(Context context,String title,String post) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, post);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(Intent.createChooser(intent, "Compartir en"));
	}
}
