package com.gly.collagelf.application;

import java.util.HashMap;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.gly.collagelf.bean.Person;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;
import android.app.Application;

/**
 * ��������
 * 
 * ȫ�����ݵĳ�ʼ��
 * 
 * @author ������
 * 
 */
public class MyApplication extends Application {

	// ��ͼ
	public static LocationClient mLocationClient = null;
	private static String MyAddress;
	private static double Longitude;
	private static double Latitude;

	public static String getAddress() {
		String str = MyAddress;
		return str;
	}

	public static double getLongitude() {
		double longitude = Longitude;
		return longitude;
	}

	public static double getLatitude() {
		double latitude = Latitude;
		return latitude;
	}

	// ά��һ����̬��Person,��������û���������
	public static Person person;

	// ά��һ��map���ϣ������������
	static Map datas = new HashMap<String, Object>();

	/**
	 * ��ȫ���д������
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Object putData(String key, Object value) {
		return datas.put(key, value);
	}

	/**
	 * ��ȡȫ������
	 * 
	 * @param idDelete
	 *            �Ƿ�ɾ��
	 * @param key
	 *            Ҫ��ѯ��key
	 * @return
	 */
	public static Object getDate(boolean idDelete, String key) {
		if (idDelete) {
			return datas.remove(key);
		}
		return datas.get(key);
	}

	//΢�ŷ���
	private static final String APP_ID = "wxfdeafa10cb4c1bf2";
	public static IWXAPI api;

	public void registerToWx() {
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		//����
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
		
		//����
		registerToWx();
		
		// ��һ��Ĭ�ϳ�ʼ��
		Bmob.initialize(this, "9021a5c111cf4365a27bac7f5d8e0d5b");
		// SMS��ʼ��
		BmobSMS.initialize(this, "9021a5c111cf4365a27bac7f5d8e0d5b");

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		// ��һ������ʼ��LocationClient��
		mLocationClient = new LocationClient(getApplicationContext()); //
		// ����LocationClient��
		// �ڶ���:ע��. ʵ��BDLocationListener�ӿ�
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				String address = location.getAddrStr();
				Longitude = location.getLongitude();
				Latitude = location.getLatitude();
				MyAddress = address;
				// Log.i("myTag", "address:" + address);
				// System.out.println("address=="+address);

			}
		});
		// ���ö�λ�Ĳ���
		initLocation();
		// ������:������λ����
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�GPS��Чʱ����1S/1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}
}
