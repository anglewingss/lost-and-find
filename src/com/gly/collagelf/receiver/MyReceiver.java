package com.gly.collagelf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.gly.collagelf.activity.PushActivity;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		Log.d("MyTag", "onReceive - " + intent.getAction());

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("�յ����Զ�����Ϣ����Ϣ�����ǣ�"
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// �Զ�����Ϣ����չʾ��֪ͨ������ȫҪ������д����ȥ����
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("�յ���֪ͨ");
			// �����������Щͳ�ƣ�������Щ��������
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			System.out.println("�û��������֪ͨ");
			// ����������Լ�д����ȥ�����û���������Ϊ
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String message = bundle.getString(JPushInterface.EXTRA_ALERT);
			Log.i("MyTag", "********message:"+message);
			Intent intent2 = new Intent(context, PushActivity.class); // �Զ���򿪵Ľ���
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.putExtra("title", title);
			intent2.putExtra("message", message);
			context.startActivity(intent2);

		} else {
			Log.d("MyTag", "Unhandled intent - " + intent.getAction());
		}
	}

}
