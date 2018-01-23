package ch.bissigp.r3pibooks;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.api.services.books.model.Volume;

import java.util.List;

import ch.bissigp.r3pibooks.databinding.ActivityMainBinding;
import ch.bissigp.r3pibooks.list.BookListAdapter;
import ch.bissigp.r3pibooks.list.EndlessRecyclerViewScrollListener;
import ch.bissigp.r3pibooks.search.SearchAsyncTask;
import ch.bissigp.r3pibooks.search.SearchFragment;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ch.bissigp.r3pibooks.BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity implements SearchAsyncTask.SearchListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment");
        if (searchFragment != null) {
            binding.searchView.setQuery(searchFragment.getLatestQuery(), false);
        } else {
            searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(searchFragment, "searchFragment")
                    .commit();
        }


        RecyclerView recyclerView = binding.booksGrid;
        int nSpans = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, nSpans);

        BookListAdapter adapter = new BookListAdapter(nSpans, searchFragment.getVolumeList());

        gridLayoutManager.setSpanSizeLookup(adapter.getSpanLookup());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        binding.searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment");
                if (searchFragment != null) {
                    searchFragment.getVolumeList().clear();
                    searchFragment.searchBooks(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment");
                if (searchFragment != null) {
                    searchFragment.getNextBooks();
                }
            }
        });

    }

    private void updateList() {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                binding.booksGrid.getAdapter().notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onSearching() {
        updateList();
    }

    @Override
    public void onResult(List<Volume> volumes) {
        updateList();
    }

}
