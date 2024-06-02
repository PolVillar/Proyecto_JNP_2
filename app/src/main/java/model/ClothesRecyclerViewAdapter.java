package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_jnp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClothesRecyclerViewAdapter extends RecyclerView.Adapter<ClothesRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<Clothes> clothesList;
    private OnItemClickListener onItemClickListener;


    public static final SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public ClothesRecyclerViewAdapter(Context context, List<Clothes> clothesList) {
        this.context = context;
        this.clothesList=clothesList;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ClothesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycle_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageDrawable(parseDrawable(clothesList.get(position).getPicture()));
        holder.tvName.setText(clothesList.get(position).getName());
        String date= clothesList.get(position).getLastUse()!=null?
                context.getString(R.string.last_use,sdf.format(clothesList.get(position).getLastUse()))
                : context.getString(R.string.last_use_null);
        holder.tvLastUse.setText(date);
        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
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
    @NonNull
    private  Drawable parseDrawable(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
