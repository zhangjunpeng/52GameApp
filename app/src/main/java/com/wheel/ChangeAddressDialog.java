package com.wheel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.view.Identification.NameVal;
import com.wheel.pickaddress.wheel.widget.adapters.AbstractWheelNameValAdapter;
import com.wheel.pickaddress.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.wheel.pickaddress.wheel.widget.views.OnWheelChangedListener;
import com.wheel.pickaddress.wheel.widget.views.OnWheelScrollListener;
import com.wheel.pickaddress.wheel.widget.views.WheelView;

/**
 * 更改封面对话框
 * 
 * @author ywl
 *
 */
public class ChangeAddressDialog extends Dialog implements View.OnClickListener {

	private WheelView wvProvince;
	private WheelView wvCitys;
	private WheelView wvCounties;
	private View lyChangeAddress;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private JSONObject mJsonObj;
	private List<NameVal> mProvinceDatas;

	private Map<String, List<NameVal>> mCitisDatasMap = new HashMap<String, List<NameVal>>();
	private Map<String, List<NameVal>> mCountryDatasMap = new HashMap<String, List<NameVal>>();

//	private ArrayList<String> arrProvinces = new ArrayList<String>();
	private ArrayList<NameVal> arrCitys = new ArrayList<NameVal>();
	private ArrayList<NameVal> arrCounties = new ArrayList<NameVal>();

	private AdressNameValAdapter provinceAdapter;
	private AdressNameValAdapter cityAdapter;
	private AdressNameValAdapter countryAdapter;

	private NameVal province_s;
	private NameVal city_s;
	private NameVal country_s;

//	private String strProvince ;
//	private String strProvinceId;
//	private String strCity ;
//	private String strCityId ;
//	private String strCountry;
//	private String strCountryId;

	private OnAddressCListener onAddressCListener;

	private int maxsize = 24;
	private int minsize = 14;

	public ChangeAddressDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changeaddress);

		wvProvince = (WheelView) findViewById(R.id.wv_address_province);
		wvCitys = (WheelView) findViewById(R.id.wv_address_city);
		wvCounties= (WheelView) findViewById(R.id.wv_address_country);
		lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
		lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeAddress.setOnClickListener(this);
		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initJsonData();
		initDatas();
		initProvinces();
		provinceAdapter = new AdressNameValAdapter(context, mProvinceDatas, getProvinceItem(province_s), maxsize, minsize);
		wvProvince.setVisibleItems(5);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(getProvinceItem(province_s));

		initCitys(mCitisDatasMap.get(province_s.getVal()));
		cityAdapter = new AdressNameValAdapter(context, arrCitys, getCityItem(city_s), maxsize, minsize);
		wvCitys.setVisibleItems(5);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(getCityItem(city_s));

		MyLog.i("初始化wvcountry");
		MyLog.i("country data size=="+mCountryDatasMap.size());
		initCountries(mCountryDatasMap.get(city_s.getVal()));
		MyLog.i("arrcounties size=="+arrCounties.size());
		countryAdapter=new AdressNameValAdapter(context,arrCounties,getCountryItem(country_s),maxsize,minsize);
		wvCounties.setVisibleItems(5);
		wvCounties.setViewAdapter(countryAdapter);
		wvCounties.setCurrentItem(getCountryItem(country_s));
		MyLog.i("初始化wvcountry2");



		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				NameVal currentText = provinceAdapter.getItemdata(wheel.getCurrentItem());
				province_s = currentText;
				setTextviewSize(currentText, provinceAdapter);
				List<NameVal> citys = mCitisDatasMap.get(currentText.getVal());
				initCitys(citys);
				cityAdapter = new AdressNameValAdapter(context, arrCitys, 0, maxsize, minsize);
				wvCitys.setVisibleItems(5);
				wvCitys.setViewAdapter(cityAdapter);
				wvCitys.setCurrentItem(0);

				initCountries(mCountryDatasMap.get(citys.get(0).getVal()));
				countryAdapter=new AdressNameValAdapter(context,arrCounties,0,maxsize,minsize);
				wvCounties.setVisibleItems(5);
				wvCounties.setViewAdapter(countryAdapter);
				wvCounties.setCurrentItem(0);
			}
		});

		wvProvince.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				NameVal currentText =  provinceAdapter.getItemdata(wheel.getCurrentItem());
				setTextviewSize(currentText, provinceAdapter);
			}
		});

		wvCitys.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				NameVal currentText =  cityAdapter.getItemdata(wheel.getCurrentItem());
				city_s = currentText;
				setTextviewSize(currentText, cityAdapter);

				List<NameVal> coutries = mCountryDatasMap.get(currentText.getVal());
				initCountries(coutries);
				countryAdapter=new AdressNameValAdapter(context,arrCounties,0,maxsize,minsize);
				wvCounties.setVisibleItems(5);
				wvCounties.setViewAdapter(countryAdapter);
				wvCounties.setCurrentItem(0);
			}
		});

		wvCitys.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				NameVal currentText =  cityAdapter.getItemdata(wheel.getCurrentItem());
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvCounties.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				NameVal currentText = countryAdapter.getItemdata(wheel.getCurrentItem());
				country_s=currentText;
				setTextviewSize(currentText,countryAdapter);
			}
		});
		wvCounties.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				NameVal currentText = countryAdapter.getItemdata(wheel.getCurrentItem());
				setTextviewSize(currentText, countryAdapter);
			}
		});
	}

	private class AdressNameValAdapter extends AbstractWheelNameValAdapter{
		List<NameVal> list;

		protected AdressNameValAdapter(Context context, List<NameVal> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		protected NameVal getItemdata(int index) {
			return list.get(index);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
			return view;
		}
	}

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		List<NameVal> list;

		protected AddressTextAdapter(Context context, List<NameVal> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index).getVal() + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(NameVal curriteItemText, AdressNameValAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.getVal().equals(currentText)) {
				textvew.setTextSize(24);
			} else {
				textvew.setTextSize(14);
			}
		}
	}

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(province_s,city_s,country_s);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeAddressChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnAddressCListener {
		public void onClick(NameVal province, NameVal city,NameVal country);
	}

	/**
	 * 从文件中读取地址数据
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = context.getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "utf-8"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析数据
	 */
	private void initDatas() {
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray("data");
			mProvinceDatas = new ArrayList<>();
			for (int i = 0; i < jsonArray.length(); i++) {
				NameVal nameVal=new NameVal();
				JSONObject jsonP = jsonArray.getJSONObject(i);
				String province = jsonP.getString("name");

				nameVal.setId(jsonP.getString("id"));
				nameVal.setVal(jsonP.getString("name"));
				mProvinceDatas.add(nameVal);
				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("citys");
				} catch (Exception e1) {
					continue;
				}
//				String[] mCitiesDatas = new String[jsonCs.length()];

				List<NameVal> mCitiesDatas=new ArrayList<>();
				for (int j = 0; j < jsonCs.length(); j++) {
					NameVal nameVal1=new NameVal();
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("name");
//					mCitiesDatas[j] = city;
					nameVal1.setId(jsonCity.getString("id"));
					nameVal1.setVal(city);
					mCitiesDatas.add(nameVal1);
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("county");
					} catch (Exception e) {
						continue;
					}
					List<NameVal> mCountryDatas=new ArrayList<>();

//					String[] mAreasDatas = new String[jsonAreas.length()];
					for (int k = 0; k < jsonAreas.length(); k++) {
//						String area = jsonAreas.getJSONObject(k).getString("s");
//						mAreasDatas[k] = area;
						NameVal nameVal2=new NameVal();
						JSONObject countryObj=jsonAreas.getJSONObject(k);
						nameVal2.setId(countryObj.getString("id"));
						nameVal2.setVal(countryObj.getString("name"));
						mCountryDatas.add(nameVal2);

					}
					mCountryDatasMap.put(city,mCountryDatas);
				}
				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e) {
			MyLog.i("解析数据出现问题：："+e.toString());
		}
		mJsonObj = null;
	}

	/**
	 * 初始化省会
	 */
	public void initProvinces() {
//		int length = mProvinceDatas.size();
//		for (int i = 0; i < length; i++) {
//			arrProvinces.add(mProvinceDatas[i]);
//		}
//		strProvince=mProvinceDatas.get(0).getVal();
//		strProvinceId=mProvinceDatas.get(0).getId();
//
//		strCity=mCitisDatasMap.get(strProvince).get(0).getVal();
//		strCityId=mCitisDatasMap.get(strProvince).get(0).getId();
//
//		strCountry=mCountryDatasMap.get(strCity).get(0).getVal();
//		strCountryId=mCountryDatasMap.get(strCity).get(0).getId();
		if (province_s==null){
			province_s=new NameVal();
			province_s.setId("1");
			province_s.setVal("北京市");

			city_s=new NameVal();
			city_s.setVal("东城区");
			city_s.setId("37");

			country_s=new NameVal();
			country_s.setId("567");
			country_s.setVal("东华门街道");
		}


	}

	/**
	 * 根据省会，生成该省会的所有城市
	 * 
	 * @param citys
	 */
	public void initCitys(List<NameVal> citys) {
		if (citys != null) {
			arrCitys.clear();
			int length = citys.size();
			for (int i = 0; i < length; i++) {
				arrCitys.add(citys.get(i));
			}
		} else {
			List<NameVal> city = mCitisDatasMap.get("北京市");
			arrCitys.clear();
			int length = city.size();
			for (int i = 0; i < length; i++) {
				arrCitys.add(city.get(i));
			}
		}
		if (arrCitys != null && arrCitys.size() > 0
				&& !arrCitys.contains(city_s)) {
			city_s = arrCitys.get(0);
		}
	}

	/**
	 * 根据城市，生成该城市的所有地区
	 *
	 * @param countries
	 */
	public void initCountries(List<NameVal> countries) {
		if (countries != null) {
			MyLog.i("countries not null");
			arrCounties.clear();
			int length = countries.size();
			MyLog.i("countries size=="+length);
			for (int i = 0; i < length; i++) {
				arrCounties.add(countries.get(i));
			}
		} else {
			MyLog.i("countries null");

			List<NameVal> country = mCountryDatasMap.get("东城区");
			arrCounties.clear();
			int length = country.size();
			for (int i = 0; i < length; i++) {
				arrCounties.add(country.get(i));
			}
		}
		if (arrCounties != null && arrCounties.size() > 0
				&& !arrCounties.contains(country_s)) {
			country_s = arrCounties.get(0);
		}
	}

	/**
	 * 初始化地点
	 * 
	 * @param province
	 * @param city
	 */
	public void setAddress(NameVal province, NameVal city,NameVal country) {
		if (province!=null){
			province_s=province;
		}
		if (city!=null){
			city_s=city;
		}
		if (country!=null){
			country_s=country;
		}
	}

	/**
	 * 返回省会索引，没有就返回默认“四川”
	 * 
	 * @param province
	 * @return
	 */
	public int getProvinceItem(NameVal province) {
		int size = mProvinceDatas.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.getVal().equals(mProvinceDatas.get(i).getVal())) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		if (noprovince) {
			province_s.setId("1");
			province_s.setVal("北京市");
//			strProvince="北京市";
//			strProvinceId="1";
			return 0;
		}
		return provinceIndex;
	}

	/**
	 * 得到城市索引，没有返回默认“成都”
	 *
	 * @param city
	 * @return
	 */
	public int getCityItem(NameVal city) {
		int size = arrCitys.size();
		int cityIndex = 0;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrCitys.get(i));
			if (city.getVal().equals(arrCitys.get(i))) {
				nocity = false;
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			city_s.setVal("东城区");
			city_s.setId("37");
//			strCity = "东城区";
//			strCityId="37";
			return 0;
		}
		return cityIndex;
	}
	/**
	 * 得到地区索引，没有返回默认“成都”
	 *
	 * @param country
	 * @return
	 */
	public int getCountryItem(NameVal country) {
		int size = arrCounties.size();
		int cityIndex = 0;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrCounties.get(i));
			if (country.getVal().equals(arrCounties.get(i))) {
				nocity = false;
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			country_s.setId("567");
			country_s.setVal("东华门街道");
//			strCountry = "东华门街道";
//			strCountryId="567";
			return 0;
		}
		return cityIndex;
	}

}