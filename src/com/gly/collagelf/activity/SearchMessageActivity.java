package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;
/**
 * ��ѯҳ��
 * @author ������
 *
 */
public class SearchMessageActivity extends BaseActivity implements BaseInterface,OnClickListener{

	private ImageView seaBack,seaSearch;
	private EditText seaContent;
	private ListView seaLv;
	//ListView������Դ
	private List<MessageInfo> searchInfos;
	
	private int screenWidth;// ��Ļ���
	private HomeAdapter myHomeAdapter;// ������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_message);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		seaBack = (ImageView) findViewById(R.id.act_search_back);
		seaSearch = (ImageView) findViewById(R.id.act_search_search);
		seaContent = (EditText) findViewById(R.id.act_search_et);
		seaLv = (ListView) findViewById(R.id.act_search_lv);
	}

	@Override
	public void initData() {
		seaBack.setOnClickListener(this);
		seaSearch.setOnClickListener(this);
	}

	@Override
	public void initOperat() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//����
		case R.id.act_search_back:
			finish();
			break;

		//ͨ���ؼ��ֲ�ѯ��Ϣ
		case R.id.act_search_search:
			final String name = seaContent.getText().toString().trim();
			if (TextUtils.isEmpty(name)) {
				return;
			}
			final List<MessageInfo> datas = new ArrayList<MessageInfo>();
			FindMessageInfoUtil.findMessageInfos(1, null, 0, 0, new FindMessageInfoListener() {

				@Override
				public void getMessageInfo(List<MessageInfo> messageInfos, BmobException ex) {
					if (ex == null) {
						for (int i = 0; i < messageInfos.size(); i++) {
							if (messageInfos.get(i).getMessageName().contains(name)) {
								datas.add(messageInfos.get(i));
							}
						}
						searchInfos = datas;
						myHomeAdapter = new HomeAdapter(SearchMessageActivity.this, searchInfos, screenWidth);
						seaLv.setAdapter(myHomeAdapter);
					}
				}
			});
			break;
		}
	}
}
