package com.jchou.imagereview.adapter;


import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jchou.imagereview.ui.ImageDetailFragment;
import com.jchou.imagereview.widget.DragViewPager;

import java.util.ArrayList;
import java.util.List;


public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private DragViewPager mPager;
    private ArrayList<Fragment> mFragmentList;

    public ImagePagerAdapter(FragmentManager fm, List<Integer> datas, DragViewPager pager) {
        super(fm);
        mPager=pager;
        mPager.setAdapter(this);
        updateData(datas);
    }

    public void updateData(List<Integer> dataList) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            final ImageDetailFragment fragment = ImageDetailFragment.newInstance(dataList.get(i));
            fragment.setOnImageListener(new ImageDetailFragment.OnImageListener() {
                @Override
                public void onInit() {
                    View view = fragment.getView();
                    mPager.setCurrentShowView(view);
                }
            });
            fragments.add(fragment);
        }
        setViewList(fragments);
    }

    private void setViewList(ArrayList<Fragment> fragmentList) {
        if (mFragmentList != null) {
            mFragmentList.clear();
        }
        mFragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragmentList==null?0:mFragmentList.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


}