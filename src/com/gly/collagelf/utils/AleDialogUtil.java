package com.gly.collagelf.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AleDialogUtil {

	public static void aleDialog(Context context){
		AlertDialog.Builder build = new Builder(context);
		build.setMessage("���Ҫ�˳���");
		build.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		build.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				System.exit(0);
			}
		});
		build.show();
	}
}
