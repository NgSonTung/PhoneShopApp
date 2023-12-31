package com.example.phoneshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneshop.databinding.OrderItemLayoutBinding;

import java.util.ArrayList;

public class OrderRVAdapter  extends  RecyclerView.Adapter<OrderRVAdapter.MyHolder>{

    OrderItemLayoutBinding binding;

    ArrayList<OrderRVItemClass> data;

    public OrderRVAdapter(ArrayList<OrderRVItemClass> data){
        this.data = data;
    }

    class  MyHolder extends RecyclerView.ViewHolder{
        TextView title, price,amount, point, createAt;

        public  MyHolder (View view){
            super(view);
            binding = OrderItemLayoutBinding.bind(view);
            title = binding.orderItemName;
            price = binding.orderItemPrice;
            amount = binding.orderItemAmount;
            point = binding.orderItemPoint;
            createAt = binding.orderItemCreateAt;
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.title.setText("Trạng thái: "+data.get(position).getTitle());
        holder.price.setText("Tổng tiền: "+data.get(position).getPrice() + " đ");
        holder.amount.setText("Số sản phẩm: "+data.get(position).getAmount() +"");
        holder.point.setText("Phương thức thanh toán: \n"+data.get(position).getPoint() + "");
        holder.createAt.setText(data.get(position).getCreateAt());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
