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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommend.adapter.ListAdapter;
import com.example.recommend.application.MyApplication;
import com.example.recommend.bean.ListBean;
import com.example.recommend.data.Post;
import com.example.recommend.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
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
public class HomeFragment extends Fragment  implements ChildEventListener {

    private static final String FirebaseURL = "https://comp90018-a2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private SmartRefreshLayout refreshLayout;
    private List<ListBean> listBeanList = new ArrayList<>();
    private ImageView btn_post;

    private List<Post> mList = new ArrayList<>();
    int page = 3;
    int currentIndex = 0;
    List<ListBean> list = new ArrayList<>();
    private MyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        app = (MyApplication)getActivity().getApplication();

        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        btn_post = view.findViewById(R.id.btn_post);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter(app.getUsername());
        listAdapter.bindToRecyclerView(recyclerView);
        listAdapter.setOnMyClickListener(new ListAdapter.OnMyClickListener() {
            @Override
            public void OnClickListener(View view, int position, int childrenPosition) {
//                ListBean listBean = listAdapter.getItem(position);
//                if (listBean == null) return;
//                if (childrenPosition == -1) {
//                    ArrayList<Integer> urls = new ArrayList<>();
//                    urls.add(listBean.getImgBig());
//                    ImagePagerActivity.startImagePage(getActivity(), urls, position,
//                            recyclerView.getLayoutManager().findViewByPosition(position));
//                } else {
//                    ArrayList<Integer> urls = (ArrayList<Integer>) listBean.getImages();
//                    ImagePagerActivity.startImagePage(getActivity(), urls, childrenPosition,
//                            recyclerView.getLayoutManager().findViewByPosition(position));
//                }
                if(mList!=null){
                    Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                    intent.putExtra("title", list.get(position).getTitle());
                    intent.putExtra("location", list.get(position).getLocation());
                    intent.putExtra("date", list.get(position).getCreateTime());
                    intent.putExtra("author", list.get(position).getName());
                    intent.putExtra("content", list.get(position).getContent());

                    intent.putExtra("key", list.get(position).getKey());

                    startActivity(intent);
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
                                    Toast.makeText(getActivity(), "No more posts", Toast.LENGTH_SHORT).show();
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

        return view;
    }

    private void getData() {
        int a = page;
        if (page == (listBeanList.size() - 1) || page > (listBeanList.size() - 1)) {
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

        DatabaseReference postData = FirebaseDatabase
                .getInstance(FirebaseURL)
                .getReference("posts");
        postData.orderByChild("date").addChildEventListener(this);

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Post post = snapshot.getValue(Post.class);
        mList.add(post);

        ListBean listBean = new ListBean();

        listBean.setName(post.getAuthor());
        listBean.setCreateTime(post.getDate());
        listBean.setTitle(post.getTitle());
        listBean.setImgBig(R.drawable.ic_1);
        listBean.setLocation(post.getLocation());
        listBean.setKey(snapshot.getKey());
        listBean.setContent(post.getContent());

        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("posts/"+snapshot.getKey());
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                if(listResult.getItems().size()!=0){
                    List<StorageReference> imageList = new ArrayList<>();
                    imageList.addAll(listResult.getItems());
                    listBean.setImages(imageList);
                }
                else{
                    listBean.setImages(new ArrayList<>());
                }
                listBeanList.add(listBean);

                list.add(listBeanList.get(currentIndex));
                currentIndex++;

//                for (int i = 0; i < page; i++) {
//
//                    list.add(listBeanList.get(i));
//                }
                listAdapter.setNewData(list);
            }
        });
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}