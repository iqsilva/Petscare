package br.com.android.queiros.igor.petscare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.android.queiros.igor.petscare.Bean.ImageUploadInfo;

/**
 * Created by igorf on 03/11/2017.
 */

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{
    Context context;

    public ImageView imageView;
    public TextView imageNameTextView;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(View view) {
        super(view);

        context = view.getContext();

        imageView = (ImageView) view.findViewById(R.id.ShowImageView);

        imageNameTextView = (TextView) view.findViewById(R.id.ImageNameTextView);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        context = view.getContext();
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View view) {
        context = view.getContext();
        itemClickListener.onClick(view,getAdapterPosition(),true);
        return true;
    }
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    public RecyclerAdapter(Context context, List<ImageUploadInfo> mainImageUploadInfoList) {
        this.context = context;
        MainImageUploadInfoList = mainImageUploadInfoList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_items, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getName());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {


                if(isLongClick)
                    Toast.makeText(context, "Clique sem segurar",Toast.LENGTH_SHORT).show();
                else
                    if (position != RecyclerView.NO_POSITION)
                    {
                        Intent intent = new Intent(context, PerfilAnimalActivity.class);
                        intent.putExtra("animalid", MainImageUploadInfoList.get(position).getAnimalId());
                        intent.putExtra("animalname", MainImageUploadInfoList.get(position).getName());
                        intent.putExtra("animalbreed", MainImageUploadInfoList.get(position).getBreed());
                        intent.putExtra("animalcolor", MainImageUploadInfoList.get(position).getColor());
                        intent.putExtra("animalcoat", MainImageUploadInfoList.get(position).getCoat());
                        intent.putExtra("animalgenre", MainImageUploadInfoList.get(position).getGenre());
                        intent.putExtra("animalspecie", MainImageUploadInfoList.get(position).getSpecie());
                        intent.putExtra("animalimageurl", MainImageUploadInfoList.get(position).getImageURL());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

}
