package com.gly.collagelf.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.activity.CurrItemMessageActivity;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.basefragment.BaseFragment;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.myview.XListView;
import com.gly.collagelf.myview.XListView.IXListViewListener;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FindFragment extends BaseFragment {

	private XListView find_lv;
	private List<MessageInfo> findMessageInfos = new ArrayList<MessageInfo>();
	private int screenWidth;// ��Ļ���
	private HomeAdapter myHomeAdapter;// ������

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_find, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		screenWidth = act.getWindowManager().getDefaultDisplay().getWidth();
		find_lv = (XListView) act.findViewById(R.id.find_lv);
		initData();
		initOperat();
	}

	/**
	 * �ؼ����ݵĳ�ʼ��
	 */
	private void initData() {
		// ListView������
		myHomeAdapter = new HomeAdapter(act, findMessageInfos, screenWidth);
		find_lv.setAdapter(myHomeAdapter);
		
		find_lv.setOnItemClickListener(new OnItemClickListener() {

			//parent�Ǹ���ͼ��viewΪ��ǰ��ͼ��position�ǵ�ǰ��ͼ��adpter��λ�ã�id�ǵ�ǰ��ͼView��ID.
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(act, CurrItemMessageActivity.class);
				intent.putExtra("index", position);
				act.startActivity(intent);
			}
		});

		// XListView������ˢ�ºͼ��ظ���
		find_lv.setPullRefreshEnable(true);
		find_lv.setPullLoadEnable(true);
		find_lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// ����ˢ��
				FindMessageInfoUtil.findMessageInfos(6, "����", 0, 0,
						new FindMessageInfoListener() {

							@Override
							public void getMessageInfo(
									List<MessageInfo> messageInfos,
									BmobException ex) {
								// �ر�ˢ��
								find_lv.stopRefresh();
								// ����������������Դ
								myHomeAdapter.updateInfos(messageInfos);
							}
						});
			}

			@Override
			public void onLoadMore() {
				// ���ظ���,�����ϴμ��ص�n������
				FindMessageInfoUtil.findMessageInfos(7, "����",
						findMessageInfos.size(), 0,
						new FindMessageInfoListener() {

							@Override
							public void getMessageInfo(
									List<MessageInfo> messageInfos,
									BmobException ex) {
								if (ex == null) {
									findMessageInfos.addAll(messageInfos);
									// ����������������Դ
									myHomeAdapter.updateInfos(findMessageInfos);
								} else {
									log("���ظ���ʧ��" + ex.getMessage());
								}
								// �رռ��ظ���
								find_lv.stopLoadMore();
							}
						});
			}
		});
	}

	/**
	 * �ؼ��Ĳ���
	 */
	public void initOperat() {
		/**
		 * �������е���Ϣ
		 */
		FindMessageInfoUtil.findMessageInfos(5, "����", 0, 0,
				new FindMessageInfoListener() {

					@Override
					public void getMessageInfo(List<MessageInfo> messageInfos,
							BmobException ex) {
						if (ex == null) {
							// �Ѵӷ������õ������ݸ�ȫ����Ϣ����
							findMessageInfos = messageInfos;
							// ����������������Դ
							myHomeAdapter.updateInfos(findMessageInfos);
						}
					}
				});

	}
	@Override
	public void onStart() {
		initOperat();
		super.onStart();
	}
}
