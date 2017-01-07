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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

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
 * �ҵ�����ҳ��
 * 
 * @author ������
 * 
 */
public class MyFindActivity extends BaseActivity implements BaseInterface {

	private ImageView findBack;
	private LinearLayout beishangLL;
	private ListView findLv;
	private int screenWidth;// ��Ļ���
	private List<MessageInfo> myFindMessageInfos = new ArrayList<MessageInfo>();
	private HomeAdapter myFindHomeAdapter;// ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myfind);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		findBack = (ImageView) findViewById(R.id.act_myfind_back_iv);
		findLv = (ListView) findViewById(R.id.act_myfind_lv);
		beishangLL = (LinearLayout) findViewById(R.id.act_myfind_beishang_ll);
	}

	@Override
	public void initData() {

		// ���ص���һҳ
		findBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//����ɾ��
		findLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new Builder(MyFindActivity.this);
				builder.setMessage("ȷ��Ҫɾ����");
				builder.setPositiveButton("ȷ��", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MessageInfo messageInfos = new MessageInfo();
						messageInfos.setObjectId(myFindMessageInfos.get(position).getObjectId());
						messageInfos.delete(new UpdateListener() {

						    @Override
						    public void done(BmobException e) {
						        if(e==null){
						            Log.i("bmob","�ɹ�");
						            initOperat();
						        }else{
						            Log.i("bmob","ʧ�ܣ�"+e.getMessage()+","+e.getErrorCode());
						        }
						    }
						});
					}
				});
				builder.setNegativeButton("ȡ��", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MyFindActivity.this, "���ѷ������β�����", 0).show();
					}
				});
				builder.show();
				return true;
			}
		});
	}

	@Override
	public void initOperat() {
		// ListView������
		myFindHomeAdapter = new HomeAdapter(this, myFindMessageInfos,
				screenWidth);
		findLv.setAdapter(myFindHomeAdapter);
		
		// --and����1,��ǰ�û�id
		BmobQuery<MessageInfo> eq1 = new BmobQuery<MessageInfo>();
		eq1.addWhereEqualTo("messageUserId", MyApplication.person.getObjectId());
		// --and����2,����Ϊ��ʧ�
		BmobQuery<MessageInfo> eq2 = new BmobQuery<MessageInfo>();
		eq2.addWhereEqualTo("messageType", "����");
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
					if (messageInfos.size()==0) {
						beishangLL.setVisibility(View.VISIBLE);
						findLv.setVisibility(View.GONE);
					}else {
						// �Ѵӷ������õ������ݸ�ȫ����Ϣ����
						myFindMessageInfos = messageInfos;
						// ����������������Դ
						myFindHomeAdapter.updateInfos(myFindMessageInfos);
					}
				} else {
					Log.i("bmob",
							"ʧ�ܣ�" + e.getMessage() + "," + e.getErrorCode());
				}
			}
		});

	}

}
