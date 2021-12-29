package com.example.recycleranotherdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//创建适配器继承RecyclerView.Adapter
//步骤：
// 1.创建适配器类继承自RecyclerView.Adapter，泛型传入RecyclerView.ViewHolder类。
// 2.创建内部类即RecyclerView.ViewHolder类的子类，并初始化item的控件。
// 3.重写RecyclerView.Adapter类的相关方法。
public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
    private Context context;
    private List<String> list;
    private View inflater;
    //构造方法，传入参数
    public RecycleAdapterDome(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public RecycleAdapterDome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建Viewholder,返回每一项的布局
        inflater= LayoutInflater.from(context).inflate(R.layout.item_dome,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterDome.MyViewHolder holder, int position) {
        //将数据和控件绑定
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        //返回Item的总条数；
        return list.size();
    }
    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(View itemView){
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.text_view);
        }
    }
}
