package me.zdnuist.android.demo9.fragment;

import java.util.Set;

import me.zdnuist.android.R;
import me.zdnuist.android.demo9.Demo9Activity;
import me.zdnuist.android.demo9.db.CourseBean;
import me.zdnuist.android.demo9.util.CourseConstant;
import me.zdnuist.android.demo9.util.MediaUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class CourseTipFragment extends DialogFragment {

	public static final String TAG = "CourseTipFragment";

	private int cid;
	private DbUtils db;
	private int status;
	private int repeatStatus = 0;
	private String ringUrl;

	public CourseTipFragment(int cid, DbUtils db, int status, int repeatStatus,
			String ringUrl) {
		this.cid = cid;
		this.db = db;
		this.status = status;
		this.repeatStatus = repeatStatus;
		this.ringUrl = ringUrl;
	}

	private TimePicker timePicker;

	private CheckBox checkBox;

	private RadioGroup radioGroup;

	private RadioButton once, repeat;

	private Spinner urlSelect;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Log.d(TAG, "onCreateDialog");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.fragment_course_tip, null);
		TextView title = (TextView) v.findViewById(R.id.tv_title);
		title.setText("课程提醒");
		timePicker = (TimePicker) v.findViewById(R.id.timePicker1);
		// 是否使用24小时制
		timePicker.setIs24HourView(true);

		checkBox = (CheckBox) v.findViewById(R.id.checkbox);

		if (status == CourseConstant.TIP_OFF) {
			checkBox.setChecked(false);
		} else {
			checkBox.setChecked(true);
		}

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (checkBox.isChecked()) {
					status = CourseConstant.TIP_ON;
				} else {
					status = CourseConstant.TIP_OFF;
				}
			}
		});

		radioGroup = (RadioGroup) v.findViewById(R.id.rg_repeat);

		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						int id = group.getCheckedRadioButtonId();

						RadioButton rb = (RadioButton) v.findViewById(id);
						Log.d(TAG, rb.getText() + "" + rb.getTag());
						repeatStatus = Integer.valueOf(rb.getTag().toString());
					}

				});

		once = (RadioButton) v.findViewById(R.id.rb_once);
		repeat = (RadioButton) v.findViewById(R.id.rb_repeat);

		if (repeatStatus == CourseConstant.REPEAT_ON) {
			repeat.setChecked(true);
		} else if (repeatStatus == CourseConstant.REPEAT_OFF) {
			once.setChecked(true);
		}

		urlSelect = (Spinner) v.findViewById(R.id.spn_url);
		// 建立数据源
		if (MediaUtils.mediaList.size() <= 1) {
			MediaUtils.getAllMediaList(getActivity(), null);
		}

		if (MediaUtils.mediaList.size() > 0) {
			Set<String> keys = MediaUtils.mediaList.keySet();
			String[] mItems = new String[] {};
			mItems = keys.toArray(mItems);
			// 建立Adapter并且绑定数据源
			ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(
					getActivity(),
					android.R.layout.simple_spinner_dropdown_item, mItems);
			// 绑定 Adapter到控件
			urlSelect.setAdapter(_Adapter);
			if (ringUrl != null && !"null".equals(ringUrl)) {
				Log.d(TAG, "begin map value");
				int i = 0;
				for (String key : keys) {
					if (MediaUtils.mediaList.get(key)!=null&&MediaUtils.mediaList.get(key).equals(ringUrl)) {
						urlSelect.setSelection(i);
						break;
					}
					i++;
				}
			}
			urlSelect.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					String key = parent.getItemAtPosition(position).toString();
					String value = MediaUtils.mediaList.get(key);
					Log.d(TAG, "media path:" + value);
					ringUrl = value;
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}

			});
		} else {
			ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(
					getActivity(),
					android.R.layout.simple_spinner_dropdown_item,
					new String[] { "NONE" });
			// 绑定 Adapter到控件
			urlSelect.setAdapter(_Adapter);
		}

		builder.setView(v)
		// Add action buttons
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						int h = timePicker.getCurrentHour();
						int m = timePicker.getCurrentMinute();
						StringBuilder hour = new StringBuilder();
						StringBuilder min = new StringBuilder();
						if (h < 10) {
							hour.append("0").append(h);
						} else {
							hour.append(h);
						}

						if (m < 10) {
							min.append("0").append(m);
						} else {
							min.append(m);
						}

						Log.d(TAG, "status:" + status);
						CourseBean bean = new CourseBean();
						bean.setId(cid);
						bean.setTipTime(hour.append(min).append("00")
								.toString());
						bean.setStatus(status);
						bean.setRepeat(repeatStatus);
						bean.setRingUrl(ringUrl);
						try {
							db.update(bean, "tip_time", "status", "repeat",
									"ring_url");
						} catch (DbException e) {
							e.printStackTrace();
						}

						((Demo9Activity) getActivity()).onComplete();
					}
				}).setNegativeButton("取消", null);
		return builder.create();
	}

	public interface TipListener {
		public void onComplete();
	}

}
