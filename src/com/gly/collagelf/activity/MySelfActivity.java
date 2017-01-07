package com.gly.collagelf.activity;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ��������
 * 
 * @author ������
 * 
 */
public class MySelfActivity extends BaseActivity implements BaseInterface,
		OnClickListener {

	private ImageButton mineBack;
	private ImageView mineHead;
	private TextView mineNick, minePhone;
	
	// ���غͻ���ͷ��
	private ImageLoader mImageLoader;
	private BmobFile userhead;// �û�ͷ��
	// �����û�ͷ��
	public File savePersonHeadFile = new File("sdcard/myhead.jpg");
	//����
	private LinearLayout mineSetting,minelove,mineCook,mineXsh,mineSt,mineZy,minePxh,mineKy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myself);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		mineBack = (ImageButton) findViewById(R.id.frag_mine_back);
		mineHead = (ImageView) findViewById(R.id.frag_mine_head);
		mineNick = (TextView) findViewById(R.id.frag_mine_nick);
		minePhone = (TextView) findViewById(R.id.frag_mine_phone);
		mineBack.setOnClickListener(this);
		mineHead.setOnClickListener(this);
		
		mineSetting = (LinearLayout) findViewById(R.id.act_mine_setting);
		mineSetting.setOnClickListener(this);
		minelove = (LinearLayout) findViewById(R.id.act_mine_love);
		minelove.setOnClickListener(this);
		mineCook = (LinearLayout) findViewById(R.id.act_mine_cook);
		mineCook.setOnClickListener(this);
		mineXsh = (LinearLayout) findViewById(R.id.act_mine_xueshenghui);
		mineXsh.setOnClickListener(this);
		mineSt = (LinearLayout) findViewById(R.id.act_mine_shetuan);
		mineSt.setOnClickListener(this);
		mineZy = (LinearLayout) findViewById(R.id.act_mine_zhiyuan);
		mineZy.setOnClickListener(this);
		minePxh = (LinearLayout) findViewById(R.id.act_mine_pingxihu);
		minePxh.setOnClickListener(this);
		mineKy = (LinearLayout) findViewById(R.id.act_mine_kaoyan);
		mineKy.setOnClickListener(this);
	}

	@Override
	public void initData() {

		// ��ȡͼƬ���غͻ���Ĳ�������
		mImageLoader = ImageLoaderUtil.getInstance(this);
		// ���ôӷ�������ȡ���ݵĹ����࣬ע��۲��ߣ��ӷ������õ���Ҫ�����ݣ��û�ͷ��
		FindPersonInfoUtil.findPersonInfo(MyApplication.person.getObjectId(),
				new FindPersonInfoListener() {

					@Override
					public void getPersonInfo(List<Person> person) {
						if (person != null) {
							log("�û���ID�ǣ�"+MyApplication.person.toString());
							mineNick.setText("�ǳƣ�"+person.get(0).getNick());
							minePhone.setText("�˻���"+person.get(0).getPhone());
							// ��ȡͷ�����õ��ؼ���
							String uri = person.get(0).getHead().getFileUrl();
							DisplayImageOptions options = ImageLoaderUtil
									.getOpt();
							/**
							 * ����һ��ͼƬ��URI ����������ʾͼƬ�Ŀؼ� ��������ͼƬ������
							 */
							mImageLoader.displayImage(uri, mineHead, options);
						}
					}
				});
	}

	@Override
	public void initOperat() {

	}

	// ����¼��ļ���
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frag_mine_back:
			finish();
			break;
		case R.id.frag_mine_head:

			// ���ͷ�񣬵����Ի������û�ѡ�����ջ��߽������
			AlertDialog.Builder builder = new Builder(this);
			builder.setNegativeButton("�����ѡȡ",
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ���"�����ѡȡ"���������ѡ�񲢼���ͼƬ
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							// ����
							intent.setType("image/*");
							// ����
							intent.putExtra("crop", "true");
							// ���б���
							intent.putExtra("aspectX", 1);
							intent.putExtra("aspectY", 1);
							// ���е����ص�
							intent.putExtra("outputX", 300);
							intent.putExtra("outputY", 300);

							intent.putExtra("scale", true);

							intent.putExtra("return-data", false);
							// ��ŵ�λ��
							intent.putExtra("output",
									Uri.fromFile(savePersonHeadFile));
							startActivityForResult(intent, 200);
						}
					});
			builder.setPositiveButton("����ѡȡ",
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE, null);
							// ��ŵ�λ��
							intent.putExtra("output",
									Uri.fromFile(savePersonHeadFile));
							startActivityForResult(intent, 300);
						}
					});
			builder.show();
			break;
		case R.id.act_mine_setting://��������
			startActivity(new Intent(this, MyselfSettingActivity.class));
			break;
		case R.id.act_mine_love://�ҵ��ղ�
			startActivity(new Intent(this, MyselfLoveActivity.class));
			break;
		case R.id.act_mine_cook://������ʳ
			startActivity(new Intent(this, NearCookActivity.class));
			break;
		case R.id.act_mine_xueshenghui:
			toast("��δ��ͨ!");
			break;
		case R.id.act_mine_shetuan:
			toast("��δ��ͨ!");
			break;
		case R.id.act_mine_pingxihu:
			toast("��δ��ͨ!");
			break;
		case R.id.act_mine_kaoyan:
			toast("��δ��ͨ!");
			break;
		case R.id.act_mine_zhiyuan:
			toast("��δ��ͨ!");
			break;
		}
	}

	// ͷ���ȡ�Ļص�����
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 200:
			userHeadUpLoading();
			break;
		case 300:
			
			//��������ĵ���Ƭ���м���
			Intent intent = new Intent("com.android.camera.action.CROP");
		    intent.setDataAndType(Uri.fromFile(savePersonHeadFile), "image/*");
		    intent.putExtra("crop", "true");
		    intent.putExtra("aspectX", 1);
		    intent.putExtra("aspectY", 1);
		    intent.putExtra("outputX", 300);
		    intent.putExtra("outputY", 300);
		    intent.putExtra("scale", true);
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(savePersonHeadFile));
		    intent.putExtra("return-data", false);
		    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		    intent.putExtra("noFaceDetection", true); // no face detection
		    startActivityForResult(intent, 400);
			break;
		case 400:
			if (Uri.fromFile(savePersonHeadFile)!=null&&data!=null) {
				userHeadUpLoading();
			}else {
				toast("ͷ�����ʧ��!");
			}
			break;
		}
	}

	/**
	 * ѡ��ͷ��������ϴ���������,�����û���Ϣ
	 */
	public void userHeadUpLoading() {
		final ProgressDialog upLoadingProDialog = showProDialog(false, null,
				null);
//		toast("ͷ���Ѿ�ѡ�����");
		final Person person = new Person();
		person.setHead(new BmobFile(savePersonHeadFile));
		person.getHead().upload(new UploadFileListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {
					// ͷ���ϴ��ɹ�
//					toast("�ϴ��ɹ�");
					person.update(MyApplication.person.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException ex) {
									if (ex == null) {
										upLoadingProDialog.dismiss();
//										toast("�û���Ϣ���³ɹ�");
										Bitmap bitmap = BitmapFactory
												.decodeFile(savePersonHeadFile
														.getAbsolutePath());
										mineHead.setImageBitmap(bitmap);
										person.setHead(new BmobFile(
												savePersonHeadFile));
									}
								}
							});
				}
			}
		});
	}

}
