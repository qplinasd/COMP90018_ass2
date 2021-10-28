package com.example.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recommend.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FragmentHomeBinding binding;
    private ListView listView;
    private SimpleAdapter simpleAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view =inflater.inflate(R.layout.fragment_home,container,false);
        listView =view.findViewById(R.id.list_article);

        simpleAdapter = new SimpleAdapter(getActivity(),getData(),R.layout.menu,new String[]{"title","image"},new int[]{R.id.myMenu_name,R.id.myMenu_image});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(this);
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button  = (Button)  view.findViewById(R.id.jump_publish);


    }

    private List<Map<String,Object>> getData() {


        List<Map<String,Object>> list= new ArrayList<>();
        for(int i=0;i<20;i++){
            //后续改成读取数据，文章标题、作者、封面、文章内容
            Map  map = new HashMap();
            map.put("title","null");
            map.put("image",null);
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

}