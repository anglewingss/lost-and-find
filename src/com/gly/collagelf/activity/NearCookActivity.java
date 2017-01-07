package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.NearCookAdapter;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Cook;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.myview.XListView;
import com.gly.collagelf.myview.XListView.IXListViewListener;
import com.gly.collagelf.utils.FindCookInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindCookInfoUtil.FindCookInfoListener;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;
/**
 * ������ʳ
 * @author ������
 *
 */
public class NearCookActivity extends BaseActivity implements BaseInterface{

	private ImageView cBack;
	private XListView cLv;
	
	private List<Cook> myCookInfos = new ArrayList<Cook>();
	private NearCookAdapter cookAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_near_cook);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		cBack = (ImageView) findViewById(R.id.cook_back);
		cLv = (XListView) findViewById(R.id.cook_lv);
	}

	@Override
	public void initData() {
		cookAdapter = new NearCookAdapter(this,myCookInfos);
		cLv.setAdapter(cookAdapter);
		//��ѯ������Ϣ
		FindCookInfoUtil.findCookInfo(1, null, 0, 0, new FindCookInfoListener() {
			
			@Override
			public void getCookInfo(List<Cook> cookInfos, BmobException ex) {
				if (ex == null) {
					//�Ѵӷ������õ������ݸ�ȫ����Ϣ����
					myCookInfos = cookInfos;
					//����������������Դ
					cookAdapter.updateInfos(myCookInfos);
				}else {
					log("���Ҹ�����ʳ�������"+ex.getMessage());
				}
			}
		});
		
		//XListView������ˢ�ºͼ��ظ���
		cLv.setPullRefreshEnable(true);
		cLv.setPullLoadEnable(true);
		cLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				//����ˢ��
				FindCookInfoUtil.findCookInfo(1, null, 0, 10, new FindCookInfoListener() {
					
					@Override
					public void getCookInfo(List<Cook> cookInfos, BmobException ex) {
						if (ex == null) {
							//����������������Դ
							cookAdapter.updateInfos(cookInfos);
							toast("ˢ�³ɹ���"+cookInfos.size());
						}else {
							log("ˢ��ʧ��"+ex.getMessage());
						}
						//�ر�ˢ��
						cLv.stopRefresh();
						
					}
				});
			}
			
			@Override
			public void onLoadMore() {
				//���ظ���,�����ϴμ��ص�n������
				FindCookInfoUtil.findCookInfo(2, null, myCookInfos.size(), 10, new FindCookInfoListener() {
					
					@Override
					public void getCookInfo(List<Cook> cookInfos, BmobException ex) {
						if (ex == null) {
							myCookInfos.addAll(cookInfos);
							//����������������Դ
							cookAdapter.updateInfos(myCookInfos);
						}else {
							log("���ظ���ʧ��"+ex.getMessage());
						}
						//�رռ��ظ���
						cLv.stopLoadMore();
						
					}
				});
			}
		});
	}

	@Override
	public void initOperat() {
		cBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
