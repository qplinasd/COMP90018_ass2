package com.example.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommend.adapter.ListAdapter;
import com.example.recommend.bean.ListBean;
import com.example.recommend.databinding.FragmentHomeBinding;
import com.jchou.imagereview.ui.ImagePagerActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haoran Lin on 2021/10/26.
 *  * stuId:1019019
 */
public class HomeFragment extends Fragment{

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private SmartRefreshLayout refreshLayout;
    private List<ListBean> listBeanList = new ArrayList<>();
    int page = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter();
        listAdapter.bindToRecyclerView(recyclerView);
        listAdapter.setOnMyClickListener(new ListAdapter.OnMyClickListener() {
            @Override
            public void OnClickListener(View view, int position, int childrenPosition) {
                ListBean listBean = listAdapter.getItem(position);
                if (listBean == null) return;
                if (childrenPosition == -1) {
                    ArrayList<Integer> urls = new ArrayList<>();
                    urls.add(listBean.getImgBig());
                    ImagePagerActivity.startImagePage(getActivity(), urls, position,
                            recyclerView.getLayoutManager().findViewByPosition(position));
                } else {
                    ArrayList<Integer> urls = (ArrayList<Integer>) listBean.getImages();
                    ImagePagerActivity.startImagePage(getActivity(), urls, childrenPosition,
                            recyclerView.getLayoutManager().findViewByPosition(position));
                }
            }
        });
        setData();
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout1) {
                getData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        Log.e("TAG", "run: " + listAdapter.getData().size() + "   " + listBeanList.size());
                        if ((listAdapter.getData().size() + 1) == listBeanList.size()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "已加载全部数据", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, 1000);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

            }
        });
        List<ListBean> list = new ArrayList<>();
        for (int i = 0; i < page; i++) {
            list.add(listBeanList.get(i));
        }
        listAdapter.setNewData(list);

        return view;
    }

    private void getData() {
        int a = page;
        if (page == (listBeanList.size() - 1) || page > (listBeanList.size() - 1)) {
            //Toast.makeText(this, "已加载全部数据", Toast.LENGTH_SHORT).show();
            return;
        }
        if (page < (listBeanList.size() - 1)) {
            page = page + 2;
        }
        if (page < listBeanList.size()) {
            listAdapter.addData(listBeanList.get(a + 1));
            listAdapter.addData(listBeanList.get(a + 2));
            //recyclerView.smoothScrollToPosition(listAdapter.getData().size());
        }


    }

    private void setData() {

        ListBean listBean = new ListBean();
        listBean.setName("画个圈圈诅咒你");
        listBean.setCreateTime("10分钟前推荐");
        listBean.setContent("今天要开心呀！");
        listBean.setLike(10);
        listBean.setComment(2);
        listBean.setImgBig(R.drawable.ic_1);
        listBean.setHeadImg(R.drawable.head_1);


        ListBean listBean1 = new ListBean();
        listBean1.setName("花开富贵");
        listBean1.setCreateTime("2021-11-01");
        listBean1.setContent("命里有时终须有，命里无时莫强求，谋事在人，成事在天。很多事情是人力不能勉强的，随缘就好。忍得一时之气，免得百日之忧。　　在情绪冲动的情况下，做出的决策伤人伤己，总是让人追悔莫及。　　能忍住自己的脾气，是一种修养，更是一种能力。");
        listBean1.setLike(3);
        listBean1.setComment(12);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.ic_2);
        imageList.add(R.drawable.ic_4);
        imageList.add(R.drawable.ic_5);
        imageList.add(R.drawable.ic_6);
        imageList.add(R.drawable.ic_7);
        imageList.add(R.drawable.ic_12);
        listBean1.setImages(imageList);
        listBean1.setHeadImg(R.drawable.head_2);


        ListBean listBean2 = new ListBean();
        listBean2.setName("泰勒——十万伏特");
        listBean2.setCreateTime("2021-11-01");
        listBean2.setContent("好喜欢这样的感觉，像是失去神经，我感到不能呼吸！");
        listBean2.setLike(3);
        listBean2.setComment(12);
        List<Integer> imageList2 = new ArrayList<>();
        imageList2.add(R.drawable.ic_8);
        imageList2.add(R.drawable.ic_9);
        imageList2.add(R.drawable.ic_10);
        imageList2.add(R.drawable.ic_11);
        listBean2.setImages(imageList2);
        listBean2.setHeadImg(R.drawable.head_3);


        ListBean listBean3 = new ListBean();
        listBean3.setName("那个男人");
        listBean3.setCreateTime("2021-10-31");
        listBean3.setContent("杭州有什么地方好玩啊\n明天休息不想宅在家\n想出去玩求推荐");
        listBean3.setLike(1);
        listBean3.setComment(0);
      /*  List<Integer> imageList3 = new ArrayList<>();
        imageList3.add(R.drawable.ic_8);
        imageList3.add(R.drawable.ic_9);
        imageList3.add(R.drawable.ic_10);
        imageList3.add(R.drawable.ic_11);
        listBean2.setImages(imageList2);*/
        listBean3.setHeadImg(R.drawable.head_4);


        ListBean listBean4 = new ListBean();
        listBean4.setName("LoveStory");
        listBean4.setCreateTime("2021-11-01");
        listBean4.setContent("EDG挺进总决赛了！\nEDG必胜\n77777777777");
        listBean4.setLike(110);
        listBean4.setComment(57);
      /*  List<Integer> imageList3 = new ArrayList<>();
        imageList3.add(R.drawable.ic_8);
        imageList3.add(R.drawable.ic_9);
        imageList3.add(R.drawable.ic_10);
        imageList3.add(R.drawable.ic_11);
        listBean2.setImages(imageList2);*/
        listBean4.setHeadImg(R.drawable.head_1);


        ListBean listBean5 = new ListBean();
        listBean5.setName("Hola、AKA");
        listBean5.setCreateTime("2021-10-29");
        listBean5.setContent("想撸猫了!!!");
        listBean5.setLike(23);
        listBean5.setComment(4);
      /*  List<Integer> imageList3 = new ArrayList<>();
        imageList3.add(R.drawable.ic_8);
        imageList3.add(R.drawable.ic_9);
        imageList3.add(R.drawable.ic_10);
        imageList3.add(R.drawable.ic_11);
        listBean2.setImages(imageList2);*/
        listBean5.setImgBig(R.drawable.ic_3);
        listBean5.setHeadImg(R.drawable.head_5);


        ListBean listBean6 = new ListBean();
        listBean6.setName("苏格拉没有底");
        listBean6.setCreateTime("2021-10-28");
        listBean6.setContent("awsl!");
        listBean6.setLike(12);
        listBean6.setComment(5);
      /*  List<Integer> imageList3 = new ArrayList<>();
        imageList3.add(R.drawable.ic_8);
        imageList3.add(R.drawable.ic_9);
        imageList3.add(R.drawable.ic_10);
        imageList3.add(R.drawable.ic_11);
        listBean2.setImages(imageList2);*/
        listBean6.setImgBig(R.drawable.ic_13);

        listBean6.setHeadImg(R.drawable.head_1);


        ListBean listBean7 = new ListBean();
        listBean7.setName("爷傲奈我何");
        listBean7.setCreateTime("2021-11-02");
        listBean7.setContent("jiejie的状态太差了，野区栓条狗都能赢的局。\n #EDG战胜GEN.G");
        listBean7.setLike(220);
        listBean7.setComment(168);
      /*  List<Integer> imageList3 = new ArrayList<>();
        imageList3.add(R.drawable.ic_8);
        imageList3.add(R.drawable.ic_9);
        imageList3.add(R.drawable.ic_10);
        imageList3.add(R.drawable.ic_11);
        listBean2.setImages(imageList2);*/
        listBean7.setHeadImg(R.drawable.head_2);


        listBeanList.add(listBean);
        listBeanList.add(listBean1);
        listBeanList.add(listBean2);
        listBeanList.add(listBean3);
        listBeanList.add(listBean4);
        listBeanList.add(listBean5);
        listBeanList.add(listBean6);
        listBeanList.add(listBean7);

   /*     for (int i = 0; i < new Random().nextInt(10+1); i++) {


        }*/


    }
}