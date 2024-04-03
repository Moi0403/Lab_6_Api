package com.example.and103_thanghtph31577_lab5.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.and103_thanghtph31577_lab5.R;
import com.example.and103_thanghtph31577_lab5.adapter.FruitAdapter;
import com.example.and103_thanghtph31577_lab5.databinding.ActivityHomeBinding;
import com.example.and103_thanghtph31577_lab5.model.Fruit;
import com.example.and103_thanghtph31577_lab5.model.Response;
import com.example.and103_thanghtph31577_lab5.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeActivity extends AppCompatActivity implements FruitAdapter.FruitClick {
    ActivityHomeBinding binding;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String token;
    private FruitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();
        sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);

        token = sharedPreferences.getString("token","");
        httpRequest.callAPI().getListFruit("Bearer " + token).enqueue(getListFruitResponse);
        userListener();
    }
    private void userListener () {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this , AddFruitActivity.class));
            }
        });
    }



    Callback<Response<ArrayList<Fruit>>> getListFruitResponse = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() ==200) {
                    ArrayList<Fruit> ds = response.body().getData();
                    getData(ds);
//                    Toast.makeText(HomeActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

        }
    };
    private void getData (ArrayList<Fruit> ds) {
        adapter = new FruitAdapter(this, ds,this );
        binding.rcvFruit.setAdapter(adapter);
    }

    @Override
    public void delete(Fruit fruit) {

    }

    @Override
    public void edit(Fruit fruit) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.callAPI().getListFruit("Bearer "+token).enqueue(getListFruitResponse);
    }
}