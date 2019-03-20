package com.chattynotes.adapters.listForward;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chattynotes.adapters.listForward.model.InterfaceForward;

public class AdapterForwardPager extends FragmentPagerAdapter {
	
	private ArrayList<InterfaceForward> recentList;

	//only for shared message
	private Boolean isSharedMessage;
	private Bundle shared_bundle;

	public AdapterForwardPager(FragmentManager fm, ArrayList<InterfaceForward> _recentList, Boolean _isSharedMessage, Bundle _shared_bundle) {
		super(fm);
		recentList = _recentList;
		isSharedMessage = _isSharedMessage;
		shared_bundle = _shared_bundle;
	}

//________________________________________________________________________________________________________________________
	@Override
	public Fragment getItem(int index) {

		switch (index) {
			case 0:
				return new FragmentRecent(recentList, isSharedMessage, shared_bundle);
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 1;
	}
}
