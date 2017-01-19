package net.nashihara.naroureader.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.nashihara.naroureader.R;
import net.nashihara.naroureader.databinding.FragmentSimpleRecycerViewBinding;
import net.nashihara.naroureader.models.entities.Novel4Realm;
import net.nashihara.naroureader.models.entities.NovelItem;
import net.nashihara.naroureader.listeners.FragmentTransactionListener;
import net.nashihara.naroureader.utils.RealmUtils;
import net.nashihara.naroureader.adapters.SimpleRecyclerViewAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import narou4j.entities.Novel;

public class DownloadedRecyclerViewFragment extends Fragment {

    private SimpleRecyclerViewAdapter adapter;

    private FragmentTransactionListener listener;

    private Context context;

    private FragmentSimpleRecycerViewBinding binding;

    private ArrayList<Novel4Realm> novels = new ArrayList<>();

    public DownloadedRecyclerViewFragment() {}

    public static DownloadedRecyclerViewFragment newInstance() {
        DownloadedRecyclerViewFragment fragment = new DownloadedRecyclerViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }

        Realm realm = RealmUtils.getRealm(context);
        RealmResults<Novel4Realm> results = realm.where(Novel4Realm.class).equalTo("isDownload", true).findAll();

        for (Novel4Realm novel4Realm : results) {
            novels.add(novel4Realm);
        }

        adapter = new SimpleRecyclerViewAdapter(context);
        adapter.setOnItemClickListener((view, position) -> replaceFragment(position));

        adapter.clearData();
        adapter.addDataOf(novels);
    }

    private void replaceFragment(int position) {
        final Novel4Realm novel = novels.get(position);

        Novel novelDetail = new Novel();
        novelDetail.setNcode(novel.getNcode());
        novelDetail.setTitle(novel.getTitle());
        novelDetail.setStory(novel.getStory());
        novelDetail.setWriter(novel.getWriter());
        novelDetail.setAllNumberOfNovel(novel.getTotalPage());

        NovelItem item = new NovelItem();
        item.setNovelDetail(novelDetail);
        listener.replaceFragment(NovelTableRecyclerViewFragment.newInstance(novel.getNcode()), novel.getTitle(), item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple_recycer_view, container, false);

        binding.recycler.setLayoutManager(new LinearLayoutManager(context));
        binding.recycler.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.VISIBLE);

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FragmentTransactionListener) {
            listener = (FragmentTransactionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement context instanceof OnFragmentReplaceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
