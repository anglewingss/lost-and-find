package com.gly.collagelf.activity;

import com.gly.collagelf.R;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.utils.NetIsConnUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * ��ӭ����
 * 
 * ��������
 * 
 * �����ж�
 * 
 * @author ������
 * 
 */
public class WelcomeActivity extends BaseActivity implements BaseInterface {

	private ImageView welcome_iv;
	private Animation animation;// �������������
	private boolean netIsConnflag = false;// �ж������Ƿ����ӣ�Ĭ��δ����
	private ProgressDialog progressDialog;//�����ж��Ƿ������Ľ������Ի���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_welcome);

		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		welcome_iv = (ImageView) findViewById(R.id.welcome_iv);
	}

	@Override
	public void initData() {
		// ��ȡ�������󣬸�ͼƬ���ö�������������죩
		animation = AnimationUtils.loadAnimation(this, R.anim.anim_set);
	}

	@Override
	public void initOperat() {
		welcome_iv.setAnimation(animation);// ��ͼƬ���ö���
		animation.setFillAfter(true);// ͣ�ڶ����������״̬
		animation.start();
		animation.setAnimationListener(new AnimationListener() {

			// ������ʼʱ�Ļص�
			@Override
			public void onAnimationStart(Animation animation) {
				/**
				 * �ж�����״̬(ʹ�ù�����)
				 */
				netIsConnflag = NetIsConnUtil.netIsConn(WelcomeActivity.this);
			}

			// �����ظ�ʱ�Ļص�
			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			// ��������ʱ�Ļص�
			@Override
			public void onAnimationEnd(Animation animation) {
				/**
				 * ����״̬���жϣ�������ת����½���棬û����ʾ��������
				 */
				if (netIsConnflag) {
					Intent intent = new Intent(WelcomeActivity.this,
							LoginAct.class);
					startActivity(intent);
					finish();
				} else {
					toast("����δ����");
					// ����Ի�����ʾ�û�ȥ��������
					alertDialog();
				}
			}
		});
	}

	/**
	 * ����Ի���������ʾ�û���������
	 */
	private void alertDialog() {
		AlertDialog.Builder builder = new Builder(WelcomeActivity.this);
		builder.setTitle("����");
		builder.setCancelable(false);
		builder.setMessage("����δ���ӣ�����������");
		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				/**
				 * ���ȡ����ť���˳�Ӧ��
				 */
				System.exit(0);// �����˳�
			}
		});
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				startActivity(new Intent(Settings.ACTION_SETTINGS));
			}
		});
		builder.show();
	}
	
	/**
	 * �����������ڵĻص������ж� ���û����������磬����ʱ��ͣ5���ٴ�ȥ�ж��û��Ƿ�����
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		new Thread() {
			public void run() {
				/**
				 * 8���ж�һ���Ƿ�����
				 */
				for (int i = 0; i < 8; i++) {
					if (i == 3) {
						netIsConnflag = NetIsConnUtil
								.netIsConn(WelcomeActivity.this);
						if (netIsConnflag) {
							break;
						}
					}
					String message = "......";
					switch (i % 4) {
					case 0:
						message = ".";
						break;
					case 1:
						message = ". .";
						break;
					case 2:
						message = ". . .";
						break;
					case 3:
						message = ". . . .";
						break;
					}
					//����message�����޸�
					final String MyMessage = message;
					//����UIҪ�����߳��н���
					runOnUiThread(new Runnable() {

						//�ж϶Ի����Ƿ�Ϊ��
						public void run() {
							if (progressDialog == null) {
								progressDialog = showProDialog(false, "����ƴ������", MyMessage);
							}else {
								progressDialog.setMessage(MyMessage);
							}
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//���һ���ж��Ƿ�����
				runOnUiThread(new Runnable() {
					public void run() {
						netIsConnflag = NetIsConnUtil.netIsConn(WelcomeActivity.this);
						if (netIsConnflag) {
							Intent intent = new Intent(WelcomeActivity.this,
									LoginAct.class);
							startActivity(intent);
							finish();
						}else {
							progressDialog.dismiss();
							progressDialog = null;
							alertDialog();
						}
					}
				});
			}
		}.start();
	}
}
