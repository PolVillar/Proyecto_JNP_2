package com.example.proyecto_jnp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<Drawable> drawables;
    private final List<String> names;
    private final List<Date> dates;
    private final SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);

    public RecyclerViewAdapter(Context context, List<Drawable> drawables, List<String> names, List<Date> dates) {
        this.context = context;
        this.drawables = drawables;
        this.names = names;
        this.dates = dates;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycle_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageDrawable(drawables.get(position));
        holder.tvName.setText(names.get(position));
        Date date= dates.get(position);
        holder.tvLastUse.setText(date==null? context.getString(R.string.last_use_null): sdf.format(date));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvLastUse;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.ivClothe);
            tvName= itemView.findViewById(R.id.tvName);
            tvLastUse= itemView.findViewById(R.id.tvLastUse);
        }
    }
}
