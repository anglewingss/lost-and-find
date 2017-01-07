package com.gly.collagelf.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.gly.collagelf.R;
import com.gly.collagelf.activity.CurrItemMessageActivity;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.myview.MyImageView;
import com.gly.collagelf.utils.CountDistanceUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

/**
 * home��ListView��Ҫ��ʾ������
 * 
 * @author ������
 * 
 */
public class HomeAdapter extends BaseAdapter {
	private ImageLoader loader;
	private Context context;
	private int screenWidth;
	// ����Դ
	private List<MessageInfo> allInfos;

	public HomeAdapter(Activity act, List<MessageInfo> allInfos, int screenWidth) {
		this.context = act;
		this.allInfos = allInfos;
		this.screenWidth = screenWidth;
	}

	@Override
	public int getCount() {
		return allInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		// һ���Ż���������item��Ĵ�������
		// �����Ż�:�����˻�ȡitem�еĿؼ��Ĵ���
		// ά��һ��ViewHolder����
		final ViewHolder holder;
		if (v == null) {
			// ������ճ�Ϊnull,˵�����ص�һҳ����,û�л���ListView
			// ��item�Ĳ���
			v = LayoutInflater.from(context).inflate(R.layout.fragment_item,
					null);
			// ����ViewHolder���󣬲�ʹ��ViewHolder��ȡ�����ؼ�
			holder = new ViewHolder();
			// �ı���
			holder.messageUsernameTv = (TextView) v
					.findViewById(R.id.frag_item_username_tv);
			holder.messageTimeTv = (TextView) v
					.findViewById(R.id.frag_item_time_tv);
			holder.messageTypeTv = (TextView) v
					.findViewById(R.id.frag_item_type);
			holder.messageAddressTv = (TextView) v
					.findViewById(R.id.frag_item_address_tv);
			holder.messageDistanceTv = (TextView) v
					.findViewById(R.id.frag_item_distance_tv);
			holder.messageTitleTv = (TextView) v
					.findViewById(R.id.frag_item_title_tv);
			holder.messageDescTv = (TextView) v
					.findViewById(R.id.frag_item_desc_tv);
			// ͷ��
			holder.messageHead = (MyImageView) v
					.findViewById(R.id.frag_item_headpic_iv);
			// ͼƬ GridView
			holder.messagePicGv = (GridView) v.findViewById(R.id.frag_item_gv);
			// �ղ�
			holder.lovepicIv = (ImageView) v
					.findViewById(R.id.frag_item_lovepic_iv);
			//����
			holder.sharepicIv = (ImageView) v.findViewById(R.id.frag_item_share_iv);
			// setTag��holder�󶨵�item��Ĳ�����
			v.setTag(holder);
		} else {
			// ������ճز�Ϊ�գ���ʹ�û��ճ��д�������item��Ĳ��֣�����ͨ��getTag()��ȡholder�е�����
			holder = (ViewHolder) v.getTag();
		}
		// ��position��Ӧ���û���Ϣ��bean����
		final MessageInfo infos = allInfos.get(position);
		// �ŵ�ȫ��
		MyApplication.putData("allInfos", allInfos);

		/**
		 * �������
		 */
		// ��ǰλ�õľ���γ��
		double locationLong = MyApplication.getLongitude();// ��ǰλ�õľ���
		double locationLa = MyApplication.getLatitude();// ��ǰλ�õ�γ��
		double actionLong = infos.getMessageLocation().getLongitude();// ��ľ���
		double actionLa = infos.getMessageLocation().getLatitude();// ���γ��
		double distance = CountDistanceUtil.Distance(locationLong, locationLa,
				actionLong, actionLa);
		if (distance < 1000) {
			// ������λС��
			DecimalFormat df = new DecimalFormat("#.00");
			String distancekm = df.format(distance);
			holder.messageDistanceTv.setText("������" + distancekm + "m");
			// ���浽ȫ��
			MyApplication.putData("distance", distance);
		} else {
			// ������λС��
			DecimalFormat df = new DecimalFormat("#.00");
			String distancekm = df.format(distance / 1000.0);
			holder.messageDistanceTv.setText("������" + distancekm + "km");
			// ���浽ȫ��
			MyApplication.putData("distance", distancekm);
		}
		// ���ı�����������
		holder.messageTimeTv.setText("ʱ�䣺" + infos.getCreatedAt());
		holder.messageAddressTv.setText("�ص㣺" + infos.getMessageAddress());
		holder.messageTypeTv.setText(infos.getMessageType());
		holder.messageTitleTv.setText("���⣺" + infos.getMessageName());
		holder.messageDescTv.setText("���飺" + infos.getMessageDesc());

		// ��GridView����������
		holder.messagePicGv.setAdapter(new HomeItemGridViewAdapter(context,
				infos.getMessagePics(), holder.messagePicGv, screenWidth));

		// ͨ��infos��getMessageUserId�����ҵ����û��ĸ�����Ϣ
		FindPersonInfoUtil.findPersonInfo(infos.getMessageUserId(),
				new FindPersonInfoListener() {

					

					@Override
					public void getPersonInfo(List<Person> person) {
						holder.messageUsernameTv.setText(person.get(0).getNick());
						// ʹ��ImageLoaderUtil����ͼƬ
						loader = ImageLoaderUtil.getInstance(context);
						DisplayImageOptions options = ImageLoaderUtil.getOpt();
						loader.displayImage(person.get(0).getHead().getFileUrl(),
								holder.messageHead, options);
					}
				});
		
		//����
		holder.sharepicIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!MyApplication.api.isWXAppInstalled()) {
				      Toast.makeText(context, "����δ��װ΢�ſͻ���", 0).show();
				      return;
				    }
				loader = ImageLoaderUtil.getInstance(context);
				DisplayImageOptions options = ImageLoaderUtil.getOpt();
				loader.loadImage(infos.getMessagePics().get(0).getFileUrl(), options, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View arg1, Bitmap loadedImage) {
						File file = new File("/sdcard/image");
						OutputStream stream;
						try {
							stream = new FileOutputStream(file);
							loadedImage.compress(CompressFormat.PNG, 100, stream );
							Intent intent = new Intent();
							ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
							intent.setComponent(comp);
							intent.setAction(Intent.ACTION_SEND);
							intent.setType("image/*");
							intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
							context.startActivity(intent);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
				
			}
		});
		
		
		boolean flag = true;// �ж��ղ�,Ϊ��ʱ��ʾδ�ղ�

		// ʹ��holder���󣬿���ֱ��ʹ��ActionPraiseViewHolder������
		final MessagePraiseViewHolder messageHolder = new MessagePraiseViewHolder();
		messageHolder.infos = infos;
		messageHolder.flag = flag;

		// ��ʾ�û���ǰ����ղ�״̬
		List<String> praiseMessages = MyApplication.person.getPraiseAction();
		if (praiseMessages.contains(messageHolder.infos.getObjectId())) {
			holder.lovepicIv.setImageResource(R.drawable.love3);
			messageHolder.flag = false;
		} else {
			holder.lovepicIv.setImageResource(R.drawable.love0);
			messageHolder.flag = true;
		}

		// �ղ�
		holder.lovepicIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				messageCollect(holder.lovepicIv);
			}

			/**
			 * �ղغ�ȡ���ղز���
			 * 
			 * @param lovepicIv
			 */
			private void messageCollect(final ImageView lovepicIv) {
				if (messageHolder.flag) {// δ�ղ�״̬

					// �޸Ļ����ղ����ݣ����ղش˻���û���id������
					MessageInfo messageInfo = new MessageInfo();
					// ԭ�Ӽ�����
					messageInfo.increment("loveCount"); // ��������1
					MyApplication.putData("loveCount",
							messageInfo.getLoveCount());
					messageInfo.add("praiseUsers",
							MyApplication.person.getObjectId());
					messageInfo.update(messageHolder.infos.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException ex) {
									if (ex == null) {
										// �ı���ʾ���ղص�ͼƬ
										lovepicIv
												.setImageResource(R.drawable.love3);
									}
								}
							});
					// �޸��û����е��ղ����ݣ��ѵ�ǰ���id�浽����
					Person person = new Person();
					person.add("praiseAction",
							messageHolder.infos.getObjectId());
					person.update(MyApplication.person.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException arg0) {

								}
							});
					// �޸��ղ�״̬
					messageHolder.flag = false;
				} else {// ���ղ�״̬
					// �޸Ļ����ղ����ݣ����ղش˻���û���id�����
					MessageInfo messageInfo = new MessageInfo();
					// ԭ�Ӽ�����
					messageInfo.increment("loveCount", -1); // �����ݼ���1
					MyApplication.putData("loveCount",
							messageInfo.getLoveCount());
					ArrayList<String> removePersonId = new ArrayList<String>();
					removePersonId.add(MyApplication.person.getObjectId());
					messageInfo.removeAll("praiseUsers", removePersonId);
					messageInfo.update(messageHolder.infos.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException e) {
									if (e == null) {
										lovepicIv
												.setImageResource(R.drawable.love0);
									} else {
										Log.i("bmob",
												"�ղ�ɾ���û�Idʧ�ܣ�" + e.getMessage());
									}
								}
							});
					// �޸��û����е��ղ����ݣ��ѵ�ǰ���id�ӱ����Ƴ�
					Person rePerson = new Person();
					ArrayList<String> removeMessageIds = new ArrayList<String>();
					removeMessageIds.add(messageHolder.infos.getObjectId());
					rePerson.removeAll("praiseAction", removeMessageIds);
					rePerson.update(MyApplication.person.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException e) {
									if (e == null) {
										Log.i("bmob", "�ɹ�");
									} else {
										Log.i("bmob",
												"�ղ�ɾ���Idʧ�ܣ�" + e.getMessage());
									}
								}
							});
					// �޸��ղ�״̬
					messageHolder.flag = true;
				}
			}
		});
		// v.setOnClickListener(new MyOnClickListener(position,v));
		return v;
	}

	/*
	 * class MyOnClickListener implements OnClickListener{
	 * 
	 * private int index; private View v; MyOnClickListener(int index,View v){
	 * this.index = index; this.v = v; }
	 * 
	 * @Override public void onClick(View arg0) { Intent intent = new
	 * Intent(context, CurrItemMessageActivity.class); intent.putExtra("index",
	 * index); context.startActivity(intent); }
	 * 
	 * }
	 */

	// �ղ�����Ķ�����Ϣ
	class MessagePraiseViewHolder {
		MessageInfo infos;
		boolean flag;
	}

	// ����һ������:�������item�е�ÿһ���ؼ�
	class ViewHolder {
		MyImageView messageHead;
		TextView messageTitleTv, messageTypeTv, messageDescTv, messageTimeTv,
				messageAddressTv, messageUsernameTv, messageDistanceTv;
		ImageView messagepicIv, lovepicIv,sharepicIv;
		GridView messagePicGv;
	}

	/**
	 * ���Ⱪ¶����adapter������Դ�ķ���
	 * 
	 * @param allInfos
	 */
	public void updateInfos(List<MessageInfo> allInfos) {
		this.allInfos = allInfos;
		notifyDataSetChanged();
	}
}
