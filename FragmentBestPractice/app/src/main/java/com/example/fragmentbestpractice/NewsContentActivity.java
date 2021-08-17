package com.example.fragmentbestpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewsContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        String newsTitle = getIntent().getStringExtra("news_title");
        String newsContent = getIntent().getStringExtra("news_content");
        NewsContentFragment newsContentFragment =
                (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_context_fragment);
        newsContentFragment.refresh(newsTitle,newsContent);
    }
}