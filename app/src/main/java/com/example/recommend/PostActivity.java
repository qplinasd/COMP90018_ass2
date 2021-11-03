package com.example.recommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {

    private TextView title;
    private TextView content;
    private String TITLE_KEY = "title_key";
    private String CONTENT_KEY = "content_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new PostFragment()).commit();

        title = findViewById(R.id.text_post_title);
        content = findViewById(R.id.text_post_content);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        String title_str = title.getText().toString();
        String content_str = content.getText().toString();
        outState.putString(TITLE_KEY, title_str);
        outState.putString(CONTENT_KEY, content_str);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String title_str = savedInstanceState.getString(TITLE_KEY, "");
        String content_str = savedInstanceState.getString(CONTENT_KEY, "");
        title.setText(title_str);
        content.setText(content_str);
    }
}