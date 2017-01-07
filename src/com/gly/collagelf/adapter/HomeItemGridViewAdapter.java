package com.gly.collagelf.adapter;

import java.io.File;
import java.util.List;

import com.gly.collagelf.R;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * GridView����������������ʾ�ͼƬ
 * @author ������
 *
 */
public class HomeItemGridViewAdapter extends BaseAdapter {

	private Context context;//������
	private List<BmobFile> messagePics;//�ͼƬ�ļ���
	private GridView messagepicGv;//Ҫ��ʾ�Ŀؼ�
	private int screenWidth;//��Ļ���
	
	public HomeItemGridViewAdapter(Context context,List<BmobFile> messagePics,GridView messagepicGv,int screenWidth) {
		this.messagePics = messagePics;
		this.context = context;
		this.messagepicGv = messagepicGv;
		this.screenWidth = screenWidth;
	}

	@Override
	public int getCount() {
		return messagePics.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		final MyHolder holder;
		if (v == null) {
			v = LinearLayout.inflate(context, R.layout.frag_home_item_actionpic_gridview, null);
			holder = new MyHolder();
			holder.picIv = (ImageView) v.findViewById(R.id.item_iv);
			v.setTag(holder);
		}else {
			holder = (MyHolder) v.getTag();
		}
		
		//�����3��֮�ڵ���Ƭ����GridView�ĸ߶�����Ϊ90������3�ţ��߶�����Ϊ180
		if (messagePics.size()<=3) {
			LinearLayout.LayoutParams params = new LayoutParams(screenWidth-20 , (screenWidth-20)/4);
			messagepicGv.setLayoutParams(params);
		}else {
			LinearLayout.LayoutParams params = new LayoutParams(screenWidth-20, (screenWidth-20)/2);
			messagepicGv.setLayoutParams(params);
		}
		//����ͼƬ��GridView��

		//ʹ��ImageLoaderUtil����ͼƬ
		ImageLoader loader = ImageLoaderUtil.getInstance(context);
		DisplayImageOptions options = ImageLoaderUtil.getOpt();
		loader.displayImage(messagePics.get(position).getFileUrl(), holder.picIv, options);
		return v;
	}

	//�����Ż�
	class MyHolder{
		ImageView picIv;
	}
}
