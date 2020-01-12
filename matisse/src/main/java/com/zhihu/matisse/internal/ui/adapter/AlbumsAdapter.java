package com.zhihu.matisse.internal.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhihu.matisse.R;
import com.zhihu.matisse.internal.entity.Album;
import com.zhihu.matisse.internal.entity.SelectionSpec;
import com.zhihu.matisse.internal.ui.widget.CheckView;

import java.util.List;


/**
 * author: wenjie
 * date: 2019-11-27 14:46
 * descption:
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private Context mContext;
    private List<Album> mAlbums;
    private LayoutInflater mInflater;
    private final Drawable mPlaceholder;
    private int mSelectedPosition;
    private OnFolderSelectListener mListener;

    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    public interface OnFolderSelectListener {
        void onItemSelected(Album album);
    }

    public AlbumsAdapter(Context context, List<Album> albums) {
        this.mContext = context;
        this.mAlbums = albums;
        this.mInflater = LayoutInflater.from(context);
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.album_thumbnail_placeholder});
        mPlaceholder = ta.getDrawable(0);
        ta.recycle();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.album_list_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Album album = mAlbums.get(position);

        holder.tvFolderName.setText(album.getDisplayName(mContext));
        holder.tvFolderSize.setText(album.getCount() + "张");

        holder.checkView.setVisibility(mSelectedPosition == position ? View.VISIBLE : View.GONE);

        SelectionSpec.getInstance().imageEngine.loadThumbnail(mContext, mContext.getResources().getDimensionPixelSize(R
                        .dimen.media_grid_size), mPlaceholder,
                holder.ivImage, album.getCoverUri());

        holder.itemView.setOnClickListener(v -> {
            mSelectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onItemSelected(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums == null ? 0 : mAlbums.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvFolderName;
        TextView tvFolderSize;
        CheckView checkView;

        ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.album_cover);
            tvFolderName = itemView.findViewById(R.id.album_name);
            tvFolderSize = itemView.findViewById(R.id.album_media_count);
            checkView = itemView.findViewById(R.id.check_view);
            checkView.setChecked(true);
        }
    }

}
