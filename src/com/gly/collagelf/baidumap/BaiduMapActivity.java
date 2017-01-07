package com.gly.collagelf.baidumap;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;

public class BaiduMapActivity extends Activity {

	private MapView mapView;
	private EditText et;
	private Button btn;
	private BaiduMap baiduMap;
	private PoiSearch mPoiSearch;
	private LatLng xdlLocation;//�õ���λ���ĵ�ǰλ�ã�����Ϊ���ĵ�
	private String btnMessage;
	private Double longitude;//����
	private double latitude;//γ��
	private List<PoiInfo> pois;
	private PoiResult result2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidumap);
		mapView = (MapView) findViewById(R.id.bmapView);
		et = (EditText) findViewById(R.id.search_et);
		btn = (Button) findViewById(R.id.search_btn);
		
		
		//��ȡ��λ���ĵ�ǰ�ĵ�ַ�Լ����Ⱥ�γ��
		btnMessage = MyApplication.getAddress();
		longitude = MyApplication.getLongitude();
		latitude = MyApplication.getLatitude();
		while (btnMessage == null) {
			btnMessage = MyApplication.getAddress();
			longitude = MyApplication.getLongitude();
			latitude = MyApplication.getLatitude();
		}
//		Toast.makeText(this, "address:"+btnMessage+longitude+".."+latitude, 0).show();
		xdlLocation = new LatLng(latitude, longitude);
		
		// �ٶȵ�ͼ�Ŀ�����
		baiduMap = mapView.getMap();
		
		//����ͼ��������
		sheZhiShuXing();
		//��Ӹ�����
		addMaker();
		// ��һ��������POI����ʵ��
		mPoiSearch = PoiSearch.newInstance();

		// �ڶ���������POI����������,����POI���������ߣ�
		mPoiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {


					// ��ȡPOI�������
					@Override
					public void onGetPoiResult(PoiResult result) {
						if (result == null) {
							return;
						}

						result2 = result;
						pois = result.getAllPoi();
						baiduMap.clear();
						// ����PoiOverlay
						PoiOverlay overlay = new MyPoiOverlay(baiduMap);
						// ����overlay���Դ����ע����¼�
						baiduMap.setOnMarkerClickListener(overlay);
						// ����PoiOverlay����
						overlay.setData(result);
						// ���PoiOverlay����ͼ��
						overlay.addToMap();
						overlay.zoomToSpan();
						return;
					}

					@Override
					public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

					}

					// ��ȡPlace����ҳ�������
					@Override
					public void onGetPoiDetailResult(PoiDetailResult arg0) {

					}
				});

		// ���Ĳ����ͷ�POI����ʵ����
//		mPoiSearch.destroy();
	}

	private void addMaker() {
		// ����Markerͼ��
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.map_localtion);
		// ����MarkerOption�������ڵ�ͼ�����Marker
		OverlayOptions option = new MarkerOptions().position(xdlLocation).icon(
				bitmap);
		// �ڵ�ͼ�����Marker������ʾ
		baiduMap.addOverlay(option);

	}

	private void sheZhiShuXing() {
		/**
		 * ���õ�ͼ��ʾ�����ĵ�
		 * ����:����LatLng(γ��,����)
		 */
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
		.newLatLng(xdlLocation);
		// ���µ�ͼ����(���ĵ�)
		baiduMap.setMapStatus(mapStatusUpdate);
		MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.zoomBy(4);
		// ���µ�ͼ����(���ż���)
		baiduMap.setMapStatus(mapStatusUpdate1);
		
	}

	public void searchClick(View v){
		// �������������������
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city("����")
				.keyword(et.getText().toString().trim()).pageNum(0));
	}
	
	// ��һ���������Զ��� PoiOverlay �ࣻ
		private class MyPoiOverlay extends PoiOverlay {
			public MyPoiOverlay(BaiduMap baiduMap) {
				super(baiduMap);
			}

			/**
			 * ��ȡ�û�ѡ��ĵص�,�����Ե��ϸ�����
			 */
			@Override
			public boolean onPoiClick(int index) {
				super.onPoiClick(index);
				
				String mapAddress= pois.get(index).address;
				final double actionLong = pois.get(index).location.longitude;
				final double actionLa = pois.get(index).location.latitude;
				final String mapName = pois.get(index).name;
				final String mapCity = pois.get(index).city;
				//����InfoWindowչʾ��view  
				final View popView = getLayoutInflater().inflate(R.layout.baidumap_pop, null); 
				//��ȡ�������еĿؼ�,����������
				TextView name = (TextView) popView.findViewById(R.id.map_pop_name_tv);
				name.setText(mapName);
				TextView desc = (TextView) popView.findViewById(R.id.map_pop_desc_tv);
				desc.setText(mapAddress);
				Button cancleBtn = (Button) popView.findViewById(R.id.map_pop_cancle_btn);
				//ȡ��
				cancleBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						mBaiduMap.hideInfoWindow();
					}
				});
				Button selectBtn = (Button) popView.findViewById(R.id.map_pop_select_btn);
				//ȷ����������ѵ�ǰ�ĵ�ַ�����ϸ�ҳ�棬������ǰҳ��
				selectBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("name", mapName);
						intent.putExtra("city", mapCity);
						intent.putExtra("actionLong", actionLong);
						intent.putExtra("actionLa", actionLa);
						setResult(555, intent);
//						MyApplication.mLocationClient.stop();
						finish();
					}
				});
				InfoWindow mInfoWindow = new InfoWindow(popView, pois.get(index).location, -47);  
				//��ʾInfoWindow  
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onPause();
	}
}
