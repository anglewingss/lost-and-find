package com.gly.collagelf.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.CommentListViewAdapter;
import com.gly.collagelf.adapter.HomeItemGridViewAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Comment;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.myview.MyImageView;
import com.gly.collagelf.myview.MyListView;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CurrItemMessageActivity extends BaseActivity implements
		BaseInterface {

	private ImageView currBack;
	private MyImageView currHead;
	private TextView currUsername, currTime, currPhone, currAddress,
			currDistance, currTitle, currType, currGoodsType, currDesc;
	private GridView currGridView;

	private ImageLoader loader;
	private int screenWidth;

	// ����
	private EditText commentEt;
	private Button commentBtn;
	private MyListView commentLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_curritem_message);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		// ���ذ�ť
		currBack = (ImageView) findViewById(R.id.act_curritem_message_back_iv);
		// ��Ϣ������ͷ��
		currHead = (MyImageView) findViewById(R.id.act_curritem_message_head_iv);
		// ��Ϣ�������ǳƣ�����ʱ�䣬�����ص㣬���룬���⣬���ͣ�����
		currUsername = (TextView) findViewById(R.id.act_curritem_message_username_tv);
		currTime = (TextView) findViewById(R.id.act_curritem_message_time_tv);
		currPhone = (TextView) findViewById(R.id.act_curritem_message_phone_tv);
		currAddress = (TextView) findViewById(R.id.act_curritem_message_address_tv);
		currDistance = (TextView) findViewById(R.id.act_curritem_message_distance_tv);
		currTitle = (TextView) findViewById(R.id.act_curritem_message_title_tv);
		currType = (TextView) findViewById(R.id.act_curritem_message_type_tv);
		currGoodsType = (TextView) findViewById(R.id.act_curritem_message_goodstype_tv);
		currDesc = (TextView) findViewById(R.id.act_curritem_message_desc_tv);

		currGridView = (GridView) findViewById(R.id.act_curritem_message_pics_gv);

		// ����
		commentEt = (EditText) findViewById(R.id.comment_et);
		commentBtn = (Button) findViewById(R.id.comment_btn);
		commentLv = (MyListView) findViewById(R.id.comment_lv);
	}

	@Override
	public void initData() {
		// ������ذ�ť������һҳ
		currBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	@Override
	public void initOperat() {
		Intent intent = getIntent();
		int index = intent.getIntExtra("index", 1);
		toast("index==" + index);
		log("hahahahha--------" + index);
		List<MessageInfo> allInfos = (List<MessageInfo>) MyApplication.getDate(
				true, "allInfos");
		final MessageInfo infos = allInfos.get(index - 1);
		currTime.setText("����ʱ�䣺" + infos.getCreatedAt());
		currPhone.setText(infos.getMessagePhone());
		currAddress.setText(infos.getMessageAddress());
		currDistance.setText(MyApplication.getDate(true, "distance") + "km");
		currType.setText("��Ϣ���" + infos.getMessageType());
		currGoodsType.setText("��Ʒ����:" + infos.getMessageGoodsType());
		currTitle.setText(infos.getMessageName());
		currDesc.setText("��  �飺" + infos.getMessageDesc());

		FindPersonInfoUtil.findPersonInfo(infos.getMessageUserId(),
				new FindPersonInfoListener() {

					@Override
					public void getPersonInfo(List<Person> person) {
						currUsername.setText(person.get(0).getNick());
						// ʹ��ImageLoaderUtil����ͼƬ
						loader = ImageLoaderUtil
								.getInstance(CurrItemMessageActivity.this);
						DisplayImageOptions options = ImageLoaderUtil.getOpt();
						loader.displayImage(person.get(0).getHead().getFileUrl(),
								currHead, options);
					}
				});

		// ��GridView����������
		currGridView.setAdapter(new HomeItemGridViewAdapter(this, infos
				.getMessagePics(), currGridView, screenWidth));

		// ��������
		commentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String commentInfo = commentEt.getText().toString().trim();
				if (commentInfo.equals("") || commentInfo==null) {
					toast("���������ݣ�");
					return;
				}
				Comment comment = new Comment();
				comment.setCommentInfos(commentInfo);
				comment.setCommentUserNick(MyApplication.person.getNick());
				comment.setCommentMessageInfoId(infos.getObjectId());
				comment.save(new SaveListener<String>() {
					
					@Override
					public void done(String arg0, BmobException ex) {
						if (ex == null) {
							toast("���۳ɹ���");
							commentEt.setText("");
							findCommentInfos(infos);
						}
					}
				});
			}
		});
		
		findCommentInfos(infos);
	}

	public void findCommentInfos(final MessageInfo infos) {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.order("-createAt");
		query.addWhereEqualTo("commentMessageInfoId",infos.getObjectId());
		query.findObjects(new FindListener<Comment>() {
			
			@Override
			public void done(List<Comment> list, BmobException ex) {
				if (ex == null) {
					log("list�Ĵ�СΪ��"+list.size());
					commentLv.setAdapter(new CommentListViewAdapter(CurrItemMessageActivity.this,list));
				}
			}
		});
	}

}
