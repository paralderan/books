package ch.bissigp.r3pibooks.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ch.bissigp.r3pibooks.R;

public class ProgressViewHolder extends RecyclerView.ViewHolder {

    public ProgressViewHolder(ViewGroup viewGroup) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_progress, viewGroup, false));

    }
}
