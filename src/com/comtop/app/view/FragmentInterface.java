package com.comtop.app.view;

import android.support.v4.app.Fragment;

public interface FragmentInterface {

	/**
	 * 替换Fragment
	 * 
	 * @param mFragment
	 *            新的Fragment
	 * @param Tag
	 *            新的Fragment的Tag
	 */
	public void replaceFragment(Fragment mFragment, String Tag);

	/**
	 * 新增Fragment
	 * 
	 * @param mFragment
	 *            新的Fragment
	 * @param oldTag
	 *            旧的Fragment的Tag
	 * @param newTag
	 *            新的Fragment的Tag
	 */
	public void addFragment(Fragment mFragment, String oldTag, String newTag);

	/**
	 * Fragment返回
	 */
	public void backFragment();

}
