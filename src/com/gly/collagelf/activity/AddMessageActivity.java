package com.gly.collagelf.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.AddMessageSpinnerAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baidumap.BaiduMapActivity;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.utils.CropPictureUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * ���ʧ�����������Ϣ�ľ���ҳ��
 * 
 * @author ������
 * 
 */ 
public class AddMessageActivity extends BaseActivity implements BaseInterface,
		OnClickListener {

	// ���غ��ύ��ť
	private ImageView addBack, addUp;
	// ���⣬���飬�绰����ؼ�
	private EditText addTitle, addDesc, addUsername, addPhone;
	// ����ѡ��ؼ�
	private Spinner addSpinner;
	private String addType;// ���û�ѡ�������
	private String[] names = {"һ��ͨ", "�ֻ�", "�·�" ,"��Ʒ","Ǯ��","�鱾","Կ��","����","����"};// ����Դ
	// ���ͼƬ�Ŀؼ�
	private LinearLayout linearTop, linearButtom;
	private ImageView addPic;
	private List<Bitmap> pics = new ArrayList<Bitmap>();
	private String[] uploadPicsPaths;// Ҫ�ϴ���ͼƬ�����飬�����ϴ�
	private int width;// ͼƬ�Ŀ��
	private int height;
	// �ͼƬ�Ļ���·��
	private File actionPic = new File("sdcard/uploadPic.jpg");
	// ѡ��ص�ؼ�
	private TextView selectMap, finallyMap;
	// �û�ѡ��ĵص�ľ�γ��
	private double actionLong;// ����
	private double actionLa;// γ��
	private String city;// �û�ѡ��ĵص����ڵĳ���

	private ProgressDialog uploadProDialog;// ��ϴ�ʱ�Ľ������Ի���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_message);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		// �����
		addTitle = (EditText) findViewById(R.id.act_add_title);
		addDesc = (EditText) findViewById(R.id.act_add_message);
		addUsername = (EditText) findViewById(R.id.act_add_username);
		addPhone = (EditText) findViewById(R.id.act_add_phone);

		// ���غ��ύ��ť
		addBack = (ImageView) findViewById(R.id.act_add_back);
		addUp = (ImageView) findViewById(R.id.act_add_up);
		addBack.setOnClickListener(this);
		addUp.setOnClickListener(this);

		// ���͵�������
		addSpinner = (Spinner) findViewById(R.id.act_add_spinner);

		// ѡ��ͼƬ
		linearTop = (LinearLayout) findViewById(R.id.act_linearTop);
		linearButtom = (LinearLayout) findViewById(R.id.act_linearBottom);

		// ��ͼ
		selectMap = (TextView) findViewById(R.id.act_add_selectmap_tv);
		finallyMap = (TextView) findViewById(R.id.act_add_finallymap_tv);
		finallyMap.setText(MyApplication.getAddress());
		actionLong = MyApplication.getLongitude();
		actionLa = MyApplication.getLatitude();
		selectMap.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// �����б�spinner
		addSpinner.setAdapter(new AddMessageSpinnerAdapter(this, names));

		addSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// ��¼�û�ѡ�������
				addType = names[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	@Override
	public void initOperat() {

		/**
		 * �ϴ�ͼƬ
		 */
		// ��ȡ��Ļ�Ŀ��
		width = getWindowManager().getDefaultDisplay().getWidth() / 3;
		height = width / 3 * 2;
		// ��̬����ӿؼ�
		addPic = new ImageView(this);
		addPic.setLayoutParams(new LayoutParams(width, height));
		addPic.setImageResource(R.drawable.gather_send_img_add);

		// ��linearLayout��̬�����ͼƬ
		linearTop.addView(addPic);

		// ����Ӱ�ť��ӵ���¼��������ӣ��������ѡ��ͼƬ
		addPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ʹ�ù����ദ����ת�ͼ���
				CropPictureUtil.cropPicture(AddMessageActivity.this,
						Uri.fromFile(actionPic));
			}
		});
	}

	// ��ť�������
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.act_add_back:
			finish();
			// ��ջ����е�ͼƬ
			actionPic.delete();
			// ��ռ����е�ͼƬ
			pics.clear();
			break;

		// �ύ
		case R.id.act_add_up:
			uploadProDialog = showProDialog(false, null, "�����ϴ�...");
			publishMessage();
			// ��ջ����е�ͼƬ
			actionPic.delete();
			// ��ռ����е�ͼƬ
			pics.clear();
			break;
		// ��ͼ
		case R.id.act_add_selectmap_tv:
			startActivityForResult(new Intent(this, BaiduMapActivity.class),
					555);
			break;
		}
	}

	/**
	 * ������Ϣ
	 */
	private void publishMessage() {
		final String messageTitle = addTitle.getText().toString().trim();
		final String messageGoodsType = addType;
		final String messageType = (String) MyApplication.getDate(true, "messageType");
		final String messageDesc = addDesc.getText().toString().trim();
		final String messageUsername = addUsername.getText().toString().trim();
		final String messagePhone = addPhone.getText().toString().trim();
		final String messageAddress = finallyMap.getText().toString().trim();
		if (messageTitle == null || messageTitle.equals("")) {
			toast("���ⲻ��Ϊ�գ�");
			uploadProDialog.dismiss();
			return;
		}
		if (messageDesc == null || messageDesc.equals("")) {
			toast("���鲻��Ϊ�գ�");
			uploadProDialog.dismiss();
			return;
		}
		if (messageUsername == null || messageUsername.equals("")) {
			toast("��ϵ�˲���Ϊ�գ�");
			uploadProDialog.dismiss();
			return;
		}
		if (!messagePhone
				.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
			toast("�ֻ����벻�Ϸ���");
			uploadProDialog.dismiss();
			return;
		}
		// �����ϴ�ͼƬ
		// ��ȡͼƬλ�õ�����
		uploadPicsPaths = new String[pics.size()];
		// �Ѽ����е�ͼƬ��ŵ������ļ���
		for (int i = 0; i < pics.size(); i++) {
			File path = new File("sdcard/messagepic/upload");
			if (!path.exists()) {
				path.mkdirs();
			}
			File filePath = new File(path, "message" + i + ".jpg");
			try {
				pics.get(i).compress(CompressFormat.JPEG, 100,
						new FileOutputStream(filePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// ��ͼƬ��·�����뵽������
			uploadPicsPaths[i] = filePath.getAbsolutePath();
		}

		if (uploadPicsPaths.length < 1) {
			toast("��ѡ��ͼƬ��");
			uploadProDialog.dismiss();
			return;
		}

		// �����ϴ�ͼƬ��������
		BmobFile.uploadBatch(uploadPicsPaths, new UploadBatchListener() {

			@Override
			public void onSuccess(List<BmobFile> bmobFiles, List<String> strings) {
				// �ϴ��ɹ�
				if (bmobFiles.size() < uploadPicsPaths.length) {
					return;
				}
				// toast("ͼƬ�ϴ��ɹ�");
				MessageInfo messageInfo = new MessageInfo();
				messageInfo.setMessageName(messageTitle);// ��Ϣ����
				messageInfo.setMessageType(messageType);// ��Ϣ������
				messageInfo.setMessageGoodsType(messageGoodsType);//��Ʒ������
				messageInfo.setMessageDesc(messageDesc);// ��Ϣ����
				messageInfo.setMessageUsername(messageUsername);
				messageInfo.setMessagePhone(messagePhone);
				messageInfo.setMessageAddress(messageAddress);// ��Ϣ�ĵ�ַ
				messageInfo.setMessageUserId(MyApplication.person.getObjectId());// ������Ϣ���û���Id
				// ��Ϣ�ĵص���Ϣ(��γ��)
				messageInfo.setMessageLocation(new BmobGeoPoint(actionLong,
						actionLa));
				messageInfo.setMessagePics(bmobFiles);// ��Ϣ��ͼƬ

				// �û���д����Ϣ�ύ����������ActionInfo����
				messageInfo.save(new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException ex) {
						if (ex == null) {
							toast("��ӻ��Ϣ�ɹ�");
							uploadProDialog.dismiss();
							//ȡ����λ
							MyApplication.mLocationClient.stop();
							finish();
						}
					}
				});
			}

			@Override
			public void onProgress(int arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	/**
	 * ѡ���ͼʱ�����û�ѡ��ĵص���Ե���ͼ���ı����� �������ѡ��ͼƬ�����к󣬰Ѵ�����ͼƬ��ʾ���ؼ���
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ѡ���ͼ
		if (resultCode == 555) {
			String name = data.getStringExtra("name");
			city = data.getStringExtra("city");
			actionLong = data.getDoubleExtra("actionLong", 0);
			actionLa = data.getDoubleExtra("actionLa", 0);
			finallyMap.setText(name);
			//ȡ����λ
//			MyApplication.mLocationClient.stop();
		}
		// ���к��ͼƬ��ʾ���ؼ���
		if (requestCode == 200) {
			if (data != null) {
				showPicture();
			}
		}
	}

	/**
	 * ��ͼƬ��ʾ��������
	 */
	private void showPicture() {

		// ��list�����ͼƬ
		Bitmap bitmap = BitmapFactory.decodeFile(actionPic.getAbsolutePath());
		pics.add(bitmap);
		// �ж��Ƿ��ܷ���
		if (pics.size() < 3) {// �����1��2��ͼƬ
			linearTop.removeAllViews();
			for (int i = 0; i < pics.size(); i++) {
				Bitmap currBitmap = pics.get(i);
				// ��̬����ӿؼ�
				ImageView imageView = new ImageView(AddMessageActivity.this);
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(currBitmap);
				linearTop.addView(imageView);

			}
			linearTop.addView(addPic);
		} else if (pics.size() == 3) {// �����������ʱ������ӵİ�ť���
			linearTop.removeViewAt(2);
			Bitmap currBitmap = pics.get(2);
			ImageView imageView = new ImageView(AddMessageActivity.this);
			imageView.setLayoutParams(new LayoutParams(width, height));
			imageView.setImageBitmap(currBitmap);
			linearTop.addView(imageView);
			linearButtom.addView(addPic);
		} else if (pics.size() < 6) {// ��ӵ�4��5����Ƭ
			linearButtom.removeAllViews();
			for (int i = 3; i < pics.size(); i++) {
				Bitmap currBitmap = pics.get(i);
				// ��̬����ӿؼ�
				ImageView imageView = new ImageView(AddMessageActivity.this);
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(currBitmap);
				linearButtom.addView(imageView);

			}
			linearButtom.addView(addPic);
		} else if (pics.size() == 6) {// ��ӵ�����ʱ������ӵİ�ť���
			linearButtom.removeViewAt(2);
			Bitmap currBitmap = pics.get(5);
			ImageView imageView = new ImageView(AddMessageActivity.this);
			imageView.setLayoutParams(new LayoutParams(width, height));
			imageView.setImageBitmap(currBitmap);
			linearButtom.addView(imageView);
		}
	}
}
