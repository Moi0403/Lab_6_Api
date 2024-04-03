package com.example.and103_thanghtph31577_lab5.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.and103_thanghtph31577_lab5.R;
import com.example.and103_thanghtph31577_lab5.databinding.ItemFruitBinding;
import com.example.and103_thanghtph31577_lab5.model.Distributor;
import com.example.and103_thanghtph31577_lab5.model.Fruit;
import com.example.and103_thanghtph31577_lab5.services.ApiServices;
import com.example.and103_thanghtph31577_lab5.services.HttpRequest;
import com.example.and103_thanghtph31577_lab5.view.AddFruitActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FruitAdapter  extends RecyclerView.Adapter<FruitAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Fruit> list;
    private FruitClick fruitClick;
    private HttpRequest httpRequest;
    ApiServices apiServices;
    private String token;

    public FruitAdapter(Context context, ArrayList<Fruit> list, FruitClick fruitClick) {
        this.context = context;
        this.list = list;
        this.fruitClick = fruitClick;
    }

    public interface FruitClick {
        void delete(Fruit fruit);
        void edit(Fruit fruit);
    }

    @NonNull
    @Override
    public FruitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFruitBinding binding = ItemFruitBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Fruit fruit = list.get(position);
        holder.binding.tvName.setText(fruit.getName());
        holder.binding.tvPriceQuantity.setText("price :" +fruit.getPrice()+" - quantity:" +fruit.getQuantity());
        holder.binding.tvDes.setText(fruit.getDescription());
        String url  = fruit.getImage().get(0);
        String newUrl = url.replace("localhost", "10.0.2.2");
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context).load(R.drawable.baseline_broken_image_24))
                .into(holder.binding.img);
        Log.d("321321", "onBindViewHolder: "+list.get(position).getImage().get(0));

        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest = new HttpRequest();
                Del(fruit);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFruitBinding binding;
        public ViewHolder(ItemFruitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    Callback<com.example.and103_thanghtph31577_lab5.model.Response<Void>> responseDeleteFruit = new Callback<com.example.and103_thanghtph31577_lab5.model.Response<Void>>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(Call<com.example.and103_thanghtph31577_lab5.model.Response<Void>> call, retrofit2.Response<com.example.and103_thanghtph31577_lab5.model.Response<Void>> response) {
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    list.clear();
                    notifyDataSetChanged();
                    Void ds = response.body().getData();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle unsuccessful response
                Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<com.example.and103_thanghtph31577_lab5.model.Response<Void>> call, Throwable t) {
            // Handle failure
            Toast.makeText(context, "Đã xảy ra lỗi khi xóa", Toast.LENGTH_SHORT).show();
            Log.e("Failure", "onFailure: " + t.getMessage());
        }
    };

    private void Del(Fruit fruit){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Fruit");
        builder.setMessage("Bạn muốn xóa không ?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            httpRequest.callAPI()
                    .deleteFruit(fruit.get_id())
                    .enqueue(responseDeleteFruit);
        }).setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }


}
