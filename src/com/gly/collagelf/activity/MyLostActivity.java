package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;

/**
 * �ҵ�ʧ��ҳ��
 * 
 * @author ������
 * 
 */
public class MyLostActivity extends BaseActivity implements BaseInterface {

	private ImageView lostBack;
	private ListView lostLv;
	private LinearLayout beishangLL;
	private int screenWidth;// ��Ļ���
	private List<MessageInfo> myLostMessageInfos = new ArrayList<MessageInfo>();
	private HomeAdapter myHomeAdapter;// ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_mylost);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		lostBack = (ImageView) findViewById(R.id.act_mylost_back_iv);
		lostLv = (ListView) findViewById(R.id.act_mylost_lv);
		beishangLL = (LinearLayout) findViewById(R.id.act_mylost_beishang_ll);
	}

	@Override
	public void initData() {
		// ���ص���һҳ
		lostBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		// ����ɾ��
		lostLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(MyLostActivity.this);
				builder.setMessage("ȷ��Ҫɾ����");
				builder.setPositiveButton("ȷ��",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MessageInfo messageInfos = new MessageInfo();
								messageInfos.setObjectId(myLostMessageInfos
										.get(position).getObjectId());
								messageInfos.delete(new UpdateListener() {

									@Override
									public void done(BmobException e) {
										if (e == null) {
											Log.i("bmob", "�ɹ�");
											// ˢ��
											initOperat();
										} else {
											Log.i("bmob",
													"ʧ�ܣ�" + e.getMessage()
															+ ","
															+ e.getErrorCode());
										}
									}
								});
							}
						});
				builder.setNegativeButton("ȡ��",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(MyLostActivity.this,
										"���ѷ������β�����", 0).show();
							}
						});
				builder.show();
				return true;
			}
		});
	}

	@Override
	public void initOperat() {
		myHomeAdapter = new HomeAdapter(this, myLostMessageInfos, screenWidth);
		lostLv.setAdapter(myHomeAdapter);

		// --and����1,��ǰ�û�id
		BmobQuery<MessageInfo> eq1 = new BmobQuery<MessageInfo>();
		eq1.addWhereEqualTo("messageUserId", MyApplication.person.getObjectId());
		// --and����2,����Ϊ��ʧ�
		BmobQuery<MessageInfo> eq2 = new BmobQuery<MessageInfo>();
		eq2.addWhereEqualTo("messageType", "ʧ��");
		// �����װ������and����
		List<BmobQuery<MessageInfo>> andQuerys = new ArrayList<BmobQuery<MessageInfo>>();
		andQuerys.add(eq1);
		andQuerys.add(eq2);
		// ��ѯ��������and��������
		BmobQuery<MessageInfo> query = new BmobQuery<MessageInfo>();
		query.and(andQuerys);
		query.order("-createdAt");
		query.findObjects(new FindListener<MessageInfo>() {
			@Override
			public void done(List<MessageInfo> messageInfos, BmobException e) {
				if (e == null) {
					if (messageInfos.size() == 0) {
						beishangLL.setVisibility(View.VISIBLE);
						lostLv.setVisibility(View.GONE);
					} else {
						// �Ѵӷ������õ������ݸ�ȫ����Ϣ����
						myLostMessageInfos = messageInfos;
						// ����������������Դ
						myHomeAdapter.updateInfos(myLostMessageInfos);
					}
				} else {
					Log.i("bmob",
							"ʧ�ܣ�" + e.getMessage() + "," + e.getErrorCode());
				}
			}
		});
	}

}
