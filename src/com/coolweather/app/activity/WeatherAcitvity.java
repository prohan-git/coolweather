package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherAcitvity extends Activity {
	private LinearLayout weatherInfoLayout;
	/**
	 * ������ʾ������
	 */
	private TextView cityNameText;
	/**
	 * ������ʾ����ʱ��
	 */
	private TextView publishText;
	/**
	 * ������ʾ����������Ϣ
	 */
	private TextView weatherDespText;
	/**
	 * ������ʾ����1
	 */
	private TextView temp1Text;
	/**
	 * ������ʾ����2
	 */
	private TextView temp2Text;
	/**
	 * ��ʾ��ǰ����
	 */
	private TextView currentDateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// ��ʼ�����ֿؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCode)) {
			// ���ؼ����ŵ�ʱ���ȥ��ѯ����
			Log.i("countyCode", "66666666666");
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			// û���ؼ�����ʱ��ֱ����ʾ��������
			showWeather();
		}

	}

	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * 
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		Log.i("countyCode", countyCode);
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countyCode");

	}

	/**
	 * ��ѯ������������Ӧ������
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * ���ݴ���ĵ�ַ���������������ѯ�������Ż���������Ϣ
	 * 
	 * @param address
	 * @param string
	 */
	private void queryFromServer(final String address, final String type) {
		Log.i("queryFromServer address", address);
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// �ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					// ������������ص�������Ϣ
					Log.i("queryFromServer response", response);
					Utility.handleWeatherResponse(WeatherAcitvity.this,
							response);
					Log.i("queryFromServer response", "keyikeyi");
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							showWeather();

						}
					});
				}

			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
					}
				});

			}
		});

	}
	/**
	 * ��sharePreference�ļ��ж�ȡ�洢��������Ϣ����ʾ��������
	 */

	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("cityname", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);

	}

}
