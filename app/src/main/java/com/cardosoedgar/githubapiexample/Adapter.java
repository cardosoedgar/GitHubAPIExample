package com.cardosoedgar.githubapiexample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by edgarcardoso on 2/24/16.
 */
public class Adapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //Endless Scroll
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 2;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    LinearLayoutManager layoutManager;

    List<T> contributorList;

    public Adapter(List<T> contributorList, RecyclerView recyclerView) {
        this.contributorList = contributorList;
        this.layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }

    public void doneLoading() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return contributorList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == VIEW_PROG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress, parent, false);
            return new ProgressViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == ContributorViewHolder.class) {
            ContributorViewHolder viewHolder = (ContributorViewHolder) holder;
            Contributor contributor = (Contributor) contributorList.get(position);
            String description = "contributions: " + contributor.contributtions;

            viewHolder.title.setText(contributor.login);
            viewHolder.description.setText(description);
            Glide.with(holder.itemView.getContext())
                    .load(contributor.avatarUrl)
                    .placeholder(R.mipmap.icon_github)
                    .into(viewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return contributorList == null ? 0 : contributorList.size();
    }

    public class ContributorViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.title) TextView title;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.image_view) ImageView imageView;

        public ContributorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.progress_bar) ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
