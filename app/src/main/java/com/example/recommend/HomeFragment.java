package com.example.recommend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recommend.databinding.AttractionFragmentBinding;
import com.example.recommend.databinding.FragmentHomeBinding;
import com.example.recommend.databinding.FragmentMyBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    private FragmentHomeBinding binding;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private Button button;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view =inflater.inflate(R.layout.fragment_home,container,false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        listView =view.findViewById(R.id.article);

        simpleAdapter = new SimpleAdapter(getActivity(),getData(),R.layout.menu,new String[]{"title","image"},new int[]{R.id.myMenu_name,R.id.myMenu_image});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DetailActivity.class));
            }
        });
        button  = (Button) view.findViewById(R.id.jump_publish);
        button.setOnClickListener(new View.OnClickListener() {
            //@SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private List<Map<String,Object>> getData() {


        List<Map<String,Object>> list= new ArrayList<>();
        for(int i=0;i<20;i++){
            //后续改成读取数据，文章标题、作者、封面、文章内容
            Map  map = new HashMap();
            map.put("title","Title"+i);
            map.put("image",R.drawable.country_test_background2);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String text = listView.getAdapter().getItem(position).toString();

    }

    @Override
    public void onClick(View v) {

    }
}