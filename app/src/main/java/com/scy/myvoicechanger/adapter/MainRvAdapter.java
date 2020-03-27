package com.scy.myvoicechanger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scy.myvoicechanger.R;
import com.scy.myvoicechanger.entity.MainRvBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/3/23 09:15
 */
public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.ViewHolder> {

    private List<MainRvBean> mainRvBeans;
    private Context context;

    public MainRvAdapter(List<MainRvBean> mainRvBeans, Context context) {
        this.mainRvBeans = mainRvBeans;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rvitem_iv)
        ImageView rvitemIv;
        @BindView(R.id.rvitem_tv)
        TextView rvitemTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(int position) {
            rvitemIv.setImageResource(mainRvBeans.get(position).getImageId());
            rvitemTv.setText(mainRvBeans.get(position).getName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mainRvBeans.size();
    }
}
