package com.cdevtech.fragmentlayout;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


public class MainActivity extends Activity {

    Fragment attachedFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

    // Override of the OnBackPressed so that when a previous Fragment is restored, the ListView
    // is updated. Also, make sure that there is no blank Fragment displayed.
	@Override
	public void onBackPressed() {
		if (attachedFragment != null) {
            if(attachedFragment instanceof OnBackPressedListener){
                ((OnBackPressedListener) attachedFragment).onBackPressed();
            }
        }

	}
    // Store the Titles Fragment for when the back key is pressed
    public void setAttachedFragment(Fragment fragment) {
        attachedFragment = fragment;
    }
}