package ch.bissigp.r3pibooks.list;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;

import java.util.List;


public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Volume> mVolumes;

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;

    private GridLayoutManager.SpanSizeLookup mSpanLookup;
    private int spanCount;

    public BookListAdapter(int spancount, List<Volume> vols) {

        this.spanCount = spancount;


        mSpanLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                switch (getItemViewType(position)) {
                    case VIEW_ITEM:
                        return 1;

                    case VIEW_PROG:
                        return spanCount;

                    default:
                        return 1;
                }
            }
        };

        mVolumes = vols;
    }

    public GridLayoutManager.SpanSizeLookup getSpanLookup() {
        return mSpanLookup;
    }

    public List<Volume> getVolumes(List<Volume> vols) {
        return mVolumes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case 1:
                return new BookViewHolder(parent);
            default:
                return new ProgressViewHolder(parent);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BookViewHolder) {
            ((BookViewHolder) holder).setSpanCount(spanCount);
            ((BookViewHolder) holder).setVolume(mVolumes.get(position));
        }
    }

    @Override
    public long getItemId(int position) {
        return mVolumes.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return mVolumes.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mVolumes.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

}
