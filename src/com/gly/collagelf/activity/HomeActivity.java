package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.FragmentViewPagerAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.fragment.FindFragment;
import com.gly.collagelf.fragment.HomeFragment;
import com.gly.collagelf.fragment.LostFragment;
import com.gly.collagelf.fragment.MoreFragment;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends FragmentActivity implements BaseInterface,
		OnClickListener {

	/**
	 * �������õ��Ĳ����е���Դ
	 */
	// �й�DrawerLayout�������ݵĿؼ�
	private DrawerLayout dl;
	private LinearLayout left, center;
	private TextView title_tv;// �����е�����
	/**
	 * ����ViewPager��������ݵĿؼ�
	 */
	// ѡ�
	private ViewPager pager;
	private LinearLayout[] linears = new LinearLayout[4];
	private int linearIds[] = { R.id.home_ll, R.id.lost_ll, R.id.find_ll,R.id.more_ll };
	private ImageView imgs[] = new ImageView[4];
	private int imgIds[] = { R.id.home_iv, R.id.lost_iv, R.id.find_iv,R.id.more_iv };
	private TextView tvs[] = new TextView[4];
	private int tvIds[] = { R.id.home_tv, R.id.lost_tv, R.id.find_tv,R.id.more_tv };
	// ����Fragment����
	private List<Fragment> fragments = new ArrayList<Fragment>();
	// ����ImageView����״̬Ҫ��ʾ��ͼƬ
	int imgGuan[] = { R.drawable.home1, R.drawable.lost1, R.drawable.ling1,R.drawable.more };
	int imgKai[] = { R.drawable.home4, R.drawable.lost4, R.drawable.find4,R.drawable.more2 };

	// ����˫���˳��ж�
	private boolean backFlag = false;

	// ���غͻ���ͷ��
	private ImageLoader mImageLoader;

	// �û���Ϣ
	private ImageView leftHeadIv, centerHeadIv;
	private TextView nickTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_home);
		Log.i("MyTag", "��ҳ�е�personֵ��"+MyApplication.person.toString());
		initView();
		initData();
		initOperat();
		initDlOpers();
	}

	/**
	 * ����Ĳ�������������Ч��
	 */
	private void initDlOpers() {
		dl.setDrawerListener(new DrawerListener() {
			float fromX = 1.0f;
			float fromY = 1.0f;

			@Override
			public void onDrawerStateChanged(int arg0) {
				getPersonInfo();
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				if (arg0 == left) {
					center.setX(200 * (arg1 * 100.0f) / 100.0f);
					// title.setX(200 * (arg1 * 100.0f) / 100.0f);
					// �������Ŷ���
					float toX = 1 - (arg1 * 0.4f);// 0.6-1
					ScaleAnimation scaleAnimation = new ScaleAnimation(fromX,
							toX, fromY, toX, Animation.RELATIVE_TO_PARENT,
							0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
					scaleAnimation.setFillAfter(true);
					scaleAnimation.setDuration(2);
					fromX = toX;
					fromY = toX;
					center.startAnimation(scaleAnimation);
				}
			}

			@Override
			public void onDrawerOpened(View arg0) {
				getPersonInfo();
			}

			@Override
			public void onDrawerClosed(View arg0) {
				getPersonInfo();
			}
		});
	}

	/**
	 * ���ͷ���������
	 * 
	 * @param v
	 */
	public void MyClick(View v) {
		if (dl.isDrawerOpen(Gravity.LEFT) || dl.isDrawerOpen(Gravity.RIGHT)) {
			dl.closeDrawers();
		} else {
			dl.openDrawer(Gravity.LEFT);
		}

	}

	/**
	 * DrawerLayout�������еĿؼ��ĵ���¼�
	 * 
	 * @param v
	 */
	public void MyselfClick(View v) {
		switch (v.getId()) {
		case R.id.mylost_tv:
			startActivity(new Intent(this, MyLostActivity.class));
			break;
		case R.id.myfind_tv:
			startActivity(new Intent(this, MyFindActivity.class));
			break;
		case R.id.myselfinfo_tv:
			startActivity(new Intent(this, MySelfActivity.class));
			break;
		case R.id.mycancel_tv:
//			Toast.makeText(this, "���ȵ�¼", 0).show();
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage("ȷ��Ҫע����");
			builder.setPositiveButton("ȷ��", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					Log.i("MyTag", "ע����perosonֵ��"+MyApplication.person.toString());
					MyApplication.person = null;
					startActivity(new Intent(HomeActivity.this, LoginAct.class));
				}
			});
			builder.setNegativeButton("ȡ��", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(HomeActivity.this, "���ѷ������β�����", 0).show();
				}
			});
			builder.show();
			break;
		}
	}

	/**
	 * �ؼ��ĳ�ʼ��
	 */
	@Override
	public void initView() {
		// �û���Ϣ
		leftHeadIv = (ImageView) findViewById(R.id.act_home_lefthead_iv);
		centerHeadIv = (ImageView) findViewById(R.id.act_home_centerhead_iv);
		nickTv = (TextView) findViewById(R.id.act_home_nick);
		
		// DrawerLayout
		dl = (DrawerLayout) findViewById(R.id.dl);
		left = (LinearLayout) findViewById(R.id.left);
		center = (LinearLayout) findViewById(R.id.center);
		title_tv = (TextView) findViewById(R.id.title_tv);

		pager = (ViewPager) findViewById(R.id.pager);
		// ��������fragment
		fragments.add(new HomeFragment());
		fragments.add(new LostFragment());
		fragments.add(new FindFragment());
		fragments.add(new MoreFragment());
		for (int i = 0; i < 4; i++) {
			linears[i] = (LinearLayout) findViewById(linearIds[i]);
			imgs[i] = (ImageView) findViewById(imgIds[i]);
			tvs[i] = (TextView) findViewById(tvIds[i]);
			// ������ѡ����ü���
			linears[i].setOnClickListener(this);
		}
		// Ĭ����ʾѡ��еĵ�һ����
		imgs[0].setImageResource(imgKai[0]);
		tvs[0].setTextColor(Color.parseColor("#d81e06"));
	}

	/**
	 * ���ؼ���������
	 */
	@Override
	public void initData() {
		// ��ViewPager����������������fragment��ʾ��ViewPager��(Ҫ�̳�FragmentPagerAdapter)
		pager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
	}

	/**
	 * �ؼ��Ĳ���
	 */
	
	@Override
	public void initOperat() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			// ViewPager��item��ѡ��ʱ������ѡ��е�ͼƬ��������ɫ
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					changes(position);
					break;
				case 1:
					changes(position);
					break;
				case 2:
					changes(position);
					break;
				case 3:
					changes(position);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		getPersonInfo();
	}

	/**
	 * ���ôӷ�������ȡ���ݵĹ����࣬ע��۲��ߣ��ӷ������õ���Ҫ�����ݣ��û�ͷ��
	 */
	public void getPersonInfo() {
		// ��ȡͼƬ���غͻ���Ĳ�������
//		Log.i("MyTag", "�������õ���Ҫ��������perosonֵ��"+MyApplication.person.getObjectId());
		mImageLoader = ImageLoaderUtil.getInstance(this);
		FindPersonInfoUtil.findPersonInfo(MyApplication.person.getObjectId(),
				new FindPersonInfoListener() {

					@Override
					public void getPersonInfo(List<Person> person) {
						if (person != null) {
							nickTv.setText(person.get(0).getNick());
//							Log.i("MyTag", "nick��ֵ��"+person.get(0).getNick());
							// ��ȡͷ�����õ��ؼ���
							String uri = person.get(0).getHead().getFileUrl();
							DisplayImageOptions options = ImageLoaderUtil
									.getOpt();
							/**
							 * ����һ��ͼƬ��URI ����������ʾͼƬ�Ŀؼ� ��������ͼƬ������
							 */
							mImageLoader.displayImage(uri, leftHeadIv, options);
							mImageLoader.displayImage(uri, centerHeadIv,
									options);
						}else{
							Log.i("MyTag", "nick��ֵvxxxxxxxxxxxxxxxxxxxx");
						}
					}
				});
	}

	// ���ѡ��е�ѡ������¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_ll:
			// �ı�ViewPager��ǰ��ʾ��ҳ
			pager.setCurrentItem(0);
			changes(0);
			break;
		case R.id.lost_ll:
			pager.setCurrentItem(1);
			changes(1);
			break;
		case R.id.find_ll:
			pager.setCurrentItem(2);
			changes(2);
			break;
		case R.id.more_ll:
			pager.setCurrentItem(3);
			changes(3);
			break;
		}
	}

	// �ı�ѡ��е�ͼƬ��������ɫ
	private void changes(int position) {
		for (int i = 0; i < 4; i++) {
			if (i == position) {
				imgs[i].setImageResource(imgKai[i]);
				tvs[i].setTextColor(Color.parseColor("#d81e06"));
				title_tv.setText(tvs[position].getText());
			} else {
				imgs[i].setImageResource(imgGuan[i]);
				tvs[i].setTextColor(Color.parseColor("#707070"));
			}
		}
	}

	// ˫���˳�
	@Override
	public void onBackPressed() {
		if (backFlag) {
			// �˳�
			
			super.onBackPressed();
		} else {
			// ����һ����ʾ��Ϣ
			Toast.makeText(this, "˫���˳�", 0).show();
			backFlag = true;
			new Thread() {
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 3��֮���޸�flag��״̬
					backFlag = false;
				};
			}.start();
		}
	}

	/**
	 * ѡ��ͷ����ɣ����ص���ҳ�棬ˢ����ҳ��
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		dl.closeDrawer(left);
	}
}
