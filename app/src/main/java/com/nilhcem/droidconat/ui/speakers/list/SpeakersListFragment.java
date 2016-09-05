package com.nilhcem.droidconat.ui.speakers.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nilhcem.droidconat.DroidconApp;
import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.DataProvider;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.nilhcem.droidconat.ui.BaseFragment;
import com.nilhcem.droidconat.ui.core.recyclerview.MarginDecoration;
import com.nilhcem.droidconat.ui.speakers.details.SpeakerDetailsDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SpeakersListFragment extends BaseFragment<SpeakersListPresenter> implements SpeakersListMvp.View {

    @Inject Picasso picasso;
    @Inject DataProvider dataProvider;

    @BindView(R.id.speakers_list_loading) ProgressBar loading;
    @BindView(R.id.speakers_list_recyclerview) RecyclerView recyclerView;

    private Snackbar errorSnackbar;
    private SpeakersListAdapter adapter;

    @Override
    protected SpeakersListPresenter newPresenter() {
        return new SpeakersListPresenter(this, dataProvider);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DroidconApp.get(getContext()).component().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.speakers_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SpeakersListAdapter(picasso, this);
        recyclerView.addItemDecoration(new MarginDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        if (errorSnackbar != null) {
            errorSnackbar.dismiss();
        }
        super.onDestroyView();
    }

    @Override
    public void displaySpeakers(List<Speaker> speakers) {
        adapter.setSpeakers(speakers);
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayLoadingError() {
        loading.setVisibility(View.GONE);
        errorSnackbar = Snackbar.make(loading, R.string.connection_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.connection_error_retry, v -> presenter.reloadData());
        errorSnackbar.show();
    }

    @Override
    public void showSpeakerDetails(Speaker speaker) {
        SpeakerDetailsDialogFragment.show(speaker, getFragmentManager());
    }
}
