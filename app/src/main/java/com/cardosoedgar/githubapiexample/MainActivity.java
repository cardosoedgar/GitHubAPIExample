package com.cardosoedgar.githubapiexample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Inject Github githubService;

    int pageNumber=0;
    List<Contributor> contributorsList;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((CustomApplication) getApplication()).getNetComponent().inject(this);
        setSupportActionBar(toolbar);

        setupRecyclerView();
        getContributors();
    }

    private void setupRecyclerView() {
        contributorsList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter<Contributor>(contributorsList, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(() -> {
            if (pageNumber > 0) {
                addProgressDialog();
                getContributors();
            }
        });
    }

    public void getContributors() {
        pageNumber++;
        Observable<List<Contributor>> subscription = githubService.getContributors("square", "retrofit", pageNumber);
        subscription.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contributors -> {
                            if (!contributorsList.isEmpty()) {
                                removeProgressDialog();
                            }
                            if (contributors.isEmpty()) {
                                Snackbar.make(toolbar, "No more results", Snackbar.LENGTH_SHORT).show();
                                pageNumber = -1;
                                return;
                            }
                            addNewContributors(contributors);
                        },
                        error -> {
                            Snackbar.make(toolbar, error.getMessage(), Snackbar.LENGTH_SHORT).show();
                        });
    }

    private void addProgressDialog() {
        contributorsList.add(null);
        adapter.notifyItemInserted(getContributorsLastIndex());
    }

    private void removeProgressDialog() {
        int lastIndex = getContributorsLastIndex();
        contributorsList.remove(lastIndex);
        adapter.notifyItemRemoved(lastIndex);
    }

    private void addNewContributors(List<Contributor> contributors) {
        int size = getContributorsLastIndex() +1;
        contributorsList.addAll(contributors);
        adapter.notifyItemRangeInserted(size, contributors.size());
        adapter.doneLoading();
    }

    private int getContributorsLastIndex() {
        return contributorsList.isEmpty() ? 0 : contributorsList.size() - 1;
    }
}
