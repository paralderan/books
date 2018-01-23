package ch.bissigp.r3pibooks.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchAsyncTask.SearchListener {

    public static final long nToFetch = 31;

    private SearchAsyncTask searchTask;
    private SearchAsyncTask.SearchListener searchListener;

    private String latestQuery;
    private List<Volume> mVolumes = new ArrayList<>();

    private long page;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        searchListener = (SearchAsyncTask.SearchListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void getNextBooks() {
        page += nToFetch;
        if (mVolumes.get(mVolumes.size() - 1) != null)
            mVolumes.add(null);

        searchTask = new SearchAsyncTask();
        searchTask.setSearchListener(this, page);
        searchTask.execute(latestQuery);
    }

    public void searchBooks(String query) {
        page = 0L;
        mVolumes.clear();
        mVolumes.add(null);
        if (query.equalsIgnoreCase(latestQuery)) {
            return;
        }
        if (searchTask != null) {
            searchTask.cancel(true);
        }
        latestQuery = query;
        searchTask = new SearchAsyncTask();
        searchTask.setSearchListener(this, page);

        searchTask.execute(query);
    }

    @Override
    public void onSearching() {
        searchListener.onSearching();
    }

    @Override
    public void onResult(List<Volume> volumes) {
        if (mVolumes.size() > 0 && mVolumes.get(mVolumes.size() - 1) == null) {
            mVolumes.remove(mVolumes.size() - 1);
        }
        mVolumes.addAll(volumes);

        searchListener.onResult(volumes);
    }

    public String getLatestQuery() {
        return latestQuery;
    }

    public List<Volume> getVolumeList() {
        return mVolumes;
    }

}