package com.gly.collagelf.activity;

import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.receiver.SMSCodeReceiver;
import com.gly.collagelf.receiver.SMSCodeReceiver.ISMSCodeListener;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;


public class RegisterAct extends BaseActivity implements BaseInterface,OnClickListener{

	private EditText reg_nick_et,reg_pwd_et,reg_repwd_et,reg_phone_et,reg_code_et;
	private String regNick,regPwd,regRepwd,regPhone,regCode;
	private Button reg_getcode_btn,reg_register_btn;
	private ImageButton reg_back_ibtn;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_register);
		initView();
		initData();
		initOperat();
	}
	@Override
	public void initView() {
		reg_nick_et = (EditText) findViewById(R.id.reg_nick_et);
		reg_pwd_et = (EditText) findViewById(R.id.reg_pwd_et);
		reg_repwd_et = (EditText) findViewById(R.id.reg_repwd_et);
		reg_phone_et = (EditText) findViewById(R.id.reg_phone_et);
		reg_code_et = (EditText) findViewById(R.id.reg_code_et);
		reg_getcode_btn = (Button) findViewById(R.id.reg_getcode_btn);
		reg_register_btn = (Button) findViewById(R.id.reg_register_btn);
		reg_back_ibtn = (ImageButton) findViewById(R.id.reg_back_ibtn);
		reg_getcode_btn.setOnClickListener(this);
		reg_register_btn.setOnClickListener(this);
		reg_back_ibtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initOperat() {
		//ע���Ϊ�۲���
		SMSCodeReceiver.setISMSCodeListener(new ISMSCodeListener() {
			//��ȡ����֤��ʱ���ص��˷���
			//����֤�����õ��ı�����
			@Override
			public void setCode(String code) {
				if (code!=null) {
					reg_code_et.setText(code);
				}
			}
		});
	}
	
	//��ť����¼��ļ���
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//������ذ�ť��������ǰactivity���ص���һ��activity
		case R.id.reg_back_ibtn:
			finish();
			break;

		//�����ȡ��֤�룬��ť���뵹��ʱ��ͨ��Bmob�����ֻ�������֤�룬����֤
		case R.id.reg_getcode_btn:
			//��ȡ������е�����
			regPhone = getTVContent(reg_phone_et);
			//�ж��ֻ������Ƿ�Ϸ�
			if (!regPhone
					.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
				toast("�ֻ����벻�Ϸ���");
			}else {
				//�Ϸ�ʱ���뵹��ʱ��������������֤�뵽�ֻ�
				//��������֤��
				BmobSMS.requestSMSCode(RegisterAct.this, regPhone, "¿�Ѿ�",new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if(ex==null){//��֤�뷢�ͳɹ�
				            Log.i("bmob", "����id��"+smsId);//���ڲ�ѯ���ζ��ŷ�������
				        }
					}
				});
				
				//���ð�ť���ɵ��
				reg_getcode_btn.setClickable(false);
				//����ʱ
				CountDownTimer time = new CountDownTimer(60000,1000) {
					
					@Override
					public void onTick(long millisUntilFinished) {
						int s = (int) (millisUntilFinished/1000);
						reg_getcode_btn.setText(s+"s");
					}
					
					//����ʱ����
					@Override
					public void onFinish() {
						reg_getcode_btn.setText("��ȡ��֤��");
						reg_getcode_btn.setClickable(true);
					}
				};
				time.start();
			}
			
			break;
		
		//���ע�ᰴť����֤������������Ƿ�Ϸ����Լ�ͨ��Bmob����֤�Ƿ��ܹ�ע��
		case R.id.reg_register_btn:
			//�رչ㲥������
			SMSCodeReceiver.setISMSCodeListener(null);
			//��ȡ������е�����
			regNick = getTVContent(reg_nick_et);
			regPwd = getTVContent(reg_pwd_et);
			regRepwd = getTVContent(reg_repwd_et);
			regPhone = getTVContent(reg_phone_et);
			regCode = getTVContent(reg_code_et);
			if (regNick.equals("")) {
				toast("�ǳƲ���Ϊ�գ�");
			}else if(!regPwd.matches("^[0-9a-zA-Z]{6,16}")){
				toast("���벻�Ϸ���");
			}else if(!regPwd.equals(regRepwd)){
				toast("�������벻ͬ����ȷ��!");
			}else{
				//ͨ��verifySmsCode��ʽ����֤�ö�����֤�룺
				BmobSMS.verifySmsCode(RegisterAct.this,regPhone, regCode, new VerifySMSCodeListener() {

				    @Override
				    public void done(BmobException ex) {
				        // TODO Auto-generated method stub
				        if(ex==null){//������֤������֤�ɹ�
//				            Log.i("bmob", "��֤ͨ��");
				            //�ж��Ƿ��ܹ�ע��
				            final Person person = new Person();
							person.setNick(regNick);
							person.setPhone(regPhone);
							person.setPwd(regPwd);
							person.save(new SaveListener<String>() {

								@Override
								public void done(String arg0,
										cn.bmob.v3.exception.BmobException e) {
									if (e == null) {
										toast("ע��ɹ�");
										//�ѵ绰���������浽���������
										MyApplication.person = person;
										//ȡ���㲥
										SMSCodeReceiver.setISMSCodeListener(null);
										//�������Լ����ص���½ҳ��
										finish();
									}else{
										Toast.makeText(RegisterAct.this, "ע��ʧ��!", 0).show();
									}
								}
							});
				        }else{
				            Log.i("bmob", "��֤ʧ�ܣ�code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
				        }
				    }
				});
			}
			break;
		}
		
	}

}
