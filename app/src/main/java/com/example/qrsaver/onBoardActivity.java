package com.example.qrsaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.qrsaver.databinding.ActivityOnBoardBinding;

public class onBoardActivity extends AppCompatActivity {

    private ActivityOnBoardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOnBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btngetStarted.setOnClickListener(v->{
            startActivity(new Intent(onBoardActivity.this,loginActivity.class));
        });

    }
}