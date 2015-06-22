package com.wojtek.wstatus;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class WStatusFragment extends Fragment implements OnClickListener{
	
	private final static String TAG = WStatusFragment.class.getSimpleName();
	private EditText etStatus;
	private TextView tvCounter;
	private Button bTweet;
	private int defaultTextColor;
	private String status = "";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_wstatus, container, false);
		
		etStatus = (EditText) view.findViewById(R.id.etStatus);
		tvCounter = (TextView) view.findViewById(R.id.tvCounter);
		bTweet = (Button) view.findViewById(R.id.buttonTweet); 
		bTweet.setOnClickListener(this);
		defaultTextColor = tvCounter.getTextColors().getDefaultColor();
		
		etStatus.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.length() >= 141) {

					AlertDialog.Builder alert = new AlertDialog.Builder(
							WStatusFragment.this.getActivity());

					alert.setMessage(R.string.counter_tooLong);
					alert.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							});

					alert.show();

					String text = etStatus.getText().toString();
					String sub = text.substring(0, text.length() - 1);
					etStatus.setText(sub);
					etStatus.setSelection(etStatus.getText().length());

				}

				int count = 140 - etStatus.length();
				tvCounter.setText("" + count);

				if (count <= 50 && count > 10) {
					tvCounter.setTextColor(Color.BLUE);
				} else if (count <= 10) {
					tvCounter.setTextColor(Color.RED);
				} else {
					tvCounter.setTextColor(defaultTextColor);
				}

			}
		});
		
		return view;
	}


	@Override
	public void onClick(View v) {
		
		status = etStatus.getText().toString();

		new PostTask().execute(status);
		
	}
	
	private final class PostTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			final String url = prefs.getString("URL", "");
			
			if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
				getActivity().startActivity(new Intent(getActivity(), WSettingsActivity.class));
				return "Please update Your username and password";
			}
			
			YambaClient cloud = new YambaClient(username, password, url);
			try {
				cloud.postStatus(params[0]);
				return "successfully posted";
			} catch (YambaClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed to post";
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(WStatusFragment.this.getActivity(), result, Toast.LENGTH_LONG)
					.show();
		}

	}


	
}
