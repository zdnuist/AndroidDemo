package me.zdnuist.android.demo8;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;

/**
 * retrofit demo
 * 
 * @author zdnuist
 * 
 */
public class Demo8Activity extends BaseActivity {

	Button click;
	TextView tv;
	EditText edit_user;
	ProgressBar pbar;
	String API = "https://api.github.com";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_demo8);
		click = (Button) findViewById(R.id.button);
		tv = (TextView) findViewById(R.id.tv);
		edit_user = (EditText) findViewById(R.id.edit);
		pbar = (ProgressBar) findViewById(R.id.pb);
		pbar.setVisibility(View.INVISIBLE);
		click.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String user = edit_user.getText().toString();
				pbar.setVisibility(View.VISIBLE);
				RestAdapter restAdapter = new RestAdapter.Builder()
						.setEndpoint(API).build();
				gitapi git = restAdapter.create(gitapi.class);

				git.getFeed(user, new Callback<gitmodel>() {
					@Override
					public void success(gitmodel gitmodel, Response response) {
						tv.setText("Github Name :" + gitmodel.getName()
								+ "\nWebsite :" + gitmodel.getBlog()
								+ "\nCompany Name :" + gitmodel.getCompany());
						pbar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void failure(RetrofitError error) {
						tv.setText(error.getMessage());
						pbar.setVisibility(View.INVISIBLE);
					}

				});
			}
		});
	}

}
