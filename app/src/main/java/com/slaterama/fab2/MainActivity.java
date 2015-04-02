package com.slaterama.fab2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.slaterama.fab2.widget.roundedbutton.FloatingActionButton;
import com.slaterama.fab2.widget.roundedbutton.RoundedButton;
import com.slaterama.fab2.widget.roundedbutton.RoundedImageButton;


public class MainActivity extends Activity {

	RoundedButton mRoundedButton;
	Button mButton;
	RoundedImageButton mRoundedImageButton;
	FloatingActionButton mFloatingActionButton;
	FloatingActionButton mSmallFloatingActionButton;
	CheckBox mCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRoundedButton = (RoundedButton) findViewById(R.id.roundedbutton);
		mButton = (Button) findViewById(R.id.button);
		mRoundedImageButton = (RoundedImageButton) findViewById(R.id.roundedimagebutton);
		mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingactionbutton);
		mSmallFloatingActionButton = (FloatingActionButton)
				findViewById(R.id.smallfloatingactionbutton);
		mCheckBox = (CheckBox) findViewById(R.id.checkbox);
		mCheckBox.setChecked(mRoundedButton.isEnabled());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.checkbox:
				boolean enabled = mCheckBox.isChecked();
				mRoundedButton.setEnabled(enabled);
				mButton.setEnabled(enabled);
				mRoundedImageButton.setEnabled(enabled);
				mFloatingActionButton.setEnabled(enabled);
				mSmallFloatingActionButton.setEnabled(enabled);
				break;
		}
	}
}
