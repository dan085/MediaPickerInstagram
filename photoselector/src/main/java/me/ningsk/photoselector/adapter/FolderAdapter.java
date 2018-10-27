package me.ningsk.photoselector.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.photoselector.R;
import me.ningsk.photoselector.bean.MediaBean;
import me.ningsk.photoselector.bean.FolderBean;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{

    private Context mContext;
    private List<FolderBean> folders = new ArrayList<>();

    public FolderAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void bindFolderData(List<FolderBean> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }


    public List<FolderBean> getFolderData() {
        if (folders == null) {
            folders = new ArrayList<>();
        }
        return folders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.photo_item_folder, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FolderBean folder = folders.get(position);
        String name = folder.getName();
        int imageNum = folder.getImageNum();
        String imagePath = folders.get(0).getPath();
        boolean isChecked = folder.isChecked();
        int checkedNum = folder.getCheckedNum();
        holder.tvSign.setVisibility(checkedNum > 0 ? View.VISIBLE : View.INVISIBLE);
        holder.itemView.setSelected(isChecked);
        RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .sizeMultiplier(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(160, 160);
            Glide.with(holder.itemView.getContext())
                    .asBitmap()
                    .load(imagePath)
                    .apply(options)
                    .into(new BitmapImageViewTarget(holder.ivCover) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.
                                            create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(8);
                            holder.ivCover.setImageDrawable(circularBitmapDrawable);
                        }
                    });

        holder.tvNum.setText("(" + imageNum + ")");
        holder.tvFolderName.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    for (FolderBean mediaFolder : folders) {
                        mediaFolder.setChecked(false);
                    }
                    folder.setChecked(true);
                    notifyDataSetChanged();
                    onItemClickListener.onItemClick(folder.getName(), folder.getMedias());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvFolderName, tvNum, tvSign;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvFolderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
            tvSign = (TextView) itemView.findViewById(R.id.tv_sign);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String folderName, List<MediaBean> medias);
    }

}
