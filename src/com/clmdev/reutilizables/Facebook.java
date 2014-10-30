package com.clmdev.reutilizables;

import android.app.Activity;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class Facebook {
	private UiLifecycleHelper uiHelper;
			
	public void publicarMensaje(Activity act, String name, String msg){
		try {
			FacebookDialog shareDialog = createShareDialogBuilderForLink(act,name,msg).build();
	        uiHelper.trackPendingDialogCall(shareDialog.present());
		} catch (Exception e) {
			//Log.e("Login","Cargando "+e.getMessage());
		}
		
	}
	
	private FacebookDialog.ShareDialogBuilder createShareDialogBuilderForLink(Activity act,String name, String msg) {
        return new FacebookDialog.ShareDialogBuilder(act)
                .setName(name)
                .setDescription(msg)
                .setLink("http://clmeditores.com/lanzamientos/")
                .setPicture("http://clmdevelopers.com/agendaws/uploads/ic_launcher.png");
    }
}
