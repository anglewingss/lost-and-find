package com.gly.collagelf.basefragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
/**
 * fragment�ĸ���
 * @author ������
 *
 */
public class BaseFragment extends Fragment {

	public Activity act;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = activity;
	}

	/**
	 * ��ʾ��Ϣ
	 * @param str
	 */
	public void toast(String str){
		Toast.makeText(act, str, 0).show();
	}
	
	/**
	 * ��LogCat�д�ӡ�����Ϣ�����ڵ���
	 * @param str
	 */
	public void log(String str){
		Log.i("MyTag", str);
	}
	
	/**
	 * ��ȡTextView���û����������
	 * @param tv
	 * @return  ����
	 */
	public String getTVContent(TextView tv){
		String str = tv.getText().toString().trim();
		if (str == null) {
			str = "";
			return str;
		}
		return str;
	}
	
	/**
	 * �������Ի���
	 * @param isCancel �Ƿ��ȡ��
	 * @param title  ����
	 * @param message ����
	 * @return  �������Ի������
	 */
	public ProgressDialog showProDialog(boolean isCancel,CharSequence title,CharSequence message){
		ProgressDialog pDialog = new ProgressDialog(act);
		pDialog.setCancelable(isCancel);//�����Ƿ��ȡ��
		pDialog.setTitle(title);//���ñ���
		pDialog.setMessage(message);//������ʾ������
		pDialog.show();
		return pDialog;
	}
}
