package com.gly.collagelf.utils;


import com.gly.collagelf.baseactivity.BaseActivity;

import android.content.Intent;
import android.net.Uri;

/**
 * ��ת���ü�ͼƬ�Ĺ�����
 * @author ������
 * ��Ҫ���������ĺ�ͼƬ�����·��
 */
public class CropPictureUtil {

	public static void cropPicture(BaseActivity context,Uri uri){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		//����
		intent.setType("image/*");
		// ����
		intent.putExtra("crop", "true");
		// ���б���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// ���е����ص�
		intent.putExtra("outputX", 450);
		intent.putExtra("outputY", 450);

		intent.putExtra("scale", true);

		intent.putExtra("return-data", false);
		// ��ŵ�λ��
		intent.putExtra("output", uri);
		context.startActivityForResult(intent, 200);
	}
}
