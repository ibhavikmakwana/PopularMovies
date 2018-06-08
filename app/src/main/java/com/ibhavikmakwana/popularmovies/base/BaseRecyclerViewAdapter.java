package com.ibhavikmakwana.popularmovies.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Bhavik on 31-May-17.
 * Base class for loading {@link RecyclerView.Adapter} that handles page
 * complete callbacks. Use this class instead of {@link RecyclerView.Adapter} through out the application.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 * @see PageCompleteListener
 */

@SuppressWarnings("unused")
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public static final int PAGE_SIZE = 20;
    private final PageCompleteListener mListener;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Collection mCollection;
    private int mTotalRecords = -1;

    /**
     * Public constructor.
     *
     * @param collection Collection list of the data to bind.
     * @param listener   {@link PageCompleteListener} to get notified when all items bound.
     */
    public BaseRecyclerViewAdapter(@NonNull Collection collection,
                                   @Nullable PageCompleteListener listener) {
        mCollection = collection;
        mListener = listener;
    }

    /**
     * Public constructor.
     *
     * @param collection Collection list of the data to bind.
     */
    public BaseRecyclerViewAdapter(@NonNull Collection collection) {
        mCollection = collection;
        mListener = null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //notify on pag complete
        if (mListener != null
                && position == mCollection.size() - 1
                && mCollection.size() >= PAGE_SIZE)
            if (mTotalRecords < 0 ? (mCollection.size() - 1 % PAGE_SIZE == 0) : (mCollection.size() < mTotalRecords))
                mListener.onPageComplete();

        bindView(holder, position);
    }

    /**
     * Set the total number of records you will receive
     *
     * @param totalRecords Total number of records.
     */
    public void setTotalRecords(int totalRecords) {
        mTotalRecords = totalRecords;
    }

    /**
     * Here you should bind the view holder with your view and data.
     *
     * @param holder   {@link RecyclerView.ViewHolder}
     * @param position position of the row.
     */
    public abstract void bindView(VH holder, int position);

    @Override
    public int getItemCount() {
        return mCollection.size();
    }

    /**
     * Add the subscription to the {@link CompositeDisposable}.
     *
     * @param disposable {@link Disposable}
     */
    protected void addSubscription(@Nullable Disposable disposable) {
        if (disposable == null) return;
        mCompositeDisposable.add(disposable);
    }

    public void setCollection(Collection collection) {
        mCollection = collection;
        notifyDataSetChanged();
    }

    /**
     * Listener to get notify when the list ends.
     */
    public interface PageCompleteListener {
        /**
         * Callback to call when whole list is displayed.
         */
        void onPageComplete();
    }
}
