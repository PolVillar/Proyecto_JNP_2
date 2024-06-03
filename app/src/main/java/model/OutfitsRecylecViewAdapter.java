package model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_jnp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OutfitsRecylecViewAdapter extends RecyclerView.Adapter<OutfitsRecylecViewAdapter.ViewHolder> {

    private final Context context;
    private final List<Outfit> outfits;
    private final SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);

    public OutfitsRecylecViewAdapter(Context context, List<Outfit> outfits) {
        this.context = context;
        this.outfits=outfits;
    }


    @NonNull
    @Override
    public OutfitsRecylecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.outfits_list,parent,false);
        return new OutfitsRecylecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitsRecylecViewAdapter.ViewHolder holder, int position) {
        holder.tvOutfitName.setText(outfits.get(position).getName());
        holder.tvCreationDate.setText(context.getString(R.string.created_date,
                    sdf.format(outfits.get(position).getCreationDate())));
        String date= outfits.get(position).getModificationDate()!=null?
                    context.getString(R.string.created_date,sdf.format(outfits.get(position).getModificationDate()))
                    : context.getString(R.string.modified_date);
        holder.tvModificationDate.setText(date);            
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOutfitName;
        TextView tvCreationDate;
        TextView tvModificationDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutfitName= itemView.findViewById(R.id.tvOutfitName);
            tvCreationDate= itemView.findViewById(R.id.tvCreationDate);
            tvModificationDate= itemView.findViewById(R.id.tvModificationDate);
        }
    }
}

