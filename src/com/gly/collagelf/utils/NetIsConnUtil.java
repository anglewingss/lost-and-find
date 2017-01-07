package com.gly.collagelf.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * �жϵ�ǰ�����Ƿ�����
 * @author ������
 *
 */
public class NetIsConnUtil {

	/**
	 * �жϵ�ǰ�����Ƿ�����
	 * @param context  ������
	 * @return  trueΪ���ӣ�falseΪδ����
	 */
	public static boolean netIsConn(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		//��NetworkInfoΪ��ʱ������δ���ӣ���Ϊ��ʱ����δ����
		if (info == null) {
			return false;
		}
		return info.isConnected();
	}
}
