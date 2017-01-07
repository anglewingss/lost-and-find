package com.gly.collagelf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * ���ܶ��ŷ���ͨ���۲���ģʽ��������
 * @author ������
 * 1 ���Ž�����
 * 2 �嵥�ļ���ע��
 */
public class SMSCodeReceiver extends BroadcastReceiver {

	//���յ����ŵĻص�����
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Object[] objs = (Object[]) bundle.get("pdus");
		for (int i = 0; i < objs.length; i++) {
			byte[] bytes = (byte[]) objs[i];
			SmsMessage message = SmsMessage.createFromPdu(bytes);
			String data = message.getMessageBody();
			Log.i("MyTag", data);
			String codeData = data.substring(6, 12);
			Log.i("MyTag", codeData);
			getCodeSetCode(codeData);
		}
	}
	
	//����һ���ӿ�
	public interface ISMSCodeListener{
		void setCode(String code);
	}

	//ȫ��ά������ӿڶ���
	public static ISMSCodeListener listener;
	
	//ע��۲���
	public static void setISMSCodeListener(ISMSCodeListener listener ){
		SMSCodeReceiver.listener = listener;
	}
	
	//��ȡ��������֤��
	public void getCodeSetCode(String code){
		if (listener!=null) {
			listener.setCode(code);
		}
	}
}
