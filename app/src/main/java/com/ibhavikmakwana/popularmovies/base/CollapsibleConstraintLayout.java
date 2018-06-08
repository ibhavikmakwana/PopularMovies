package com.ibhavikmakwana.popularmovies.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ibhavikmakwana.popularmovies.R;

/**
 * Created by Bhavik Makwana on 08-06-2018.
 */
public class CollapsibleConstraintLayout extends ConstraintLayout implements AppBarLayout.OnOffsetChangedListener {

    private float mTransitionThreshold = 0.35f;
    private int mLastPosition = 0;
    private boolean mToolBarOpen = true;
    private ConstraintSet mOpenToolBarSet = new ConstraintSet();
    private ConstraintSet mCloseToolBarSet = new ConstraintSet();

    private ImageView mBackground;
    private Animator showImageAnimator;
    private Animator hideImageAnimator;

    public CollapsibleConstraintLayout(Context context) {
        super(context);
    }

    public CollapsibleConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsibleConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() instanceof AppBarLayout) {
            AppBarLayout appBarLayout = ((AppBarLayout) getParent());
            appBarLayout.addOnOffsetChangedListener(this);
            mOpenToolBarSet.clone(getContext(), R.layout.collapsing_toolbar);
            mCloseToolBarSet.clone(getContext(), R.layout.layout_closed_toolbar);
            showImageAnimator = ObjectAnimator.ofFloat(mBackground, "alpha", 0f, 1f);
            showImageAnimator.setDuration(600);
            hideImageAnimator = ObjectAnimator.ofFloat(mBackground, "alpha", 1f, 0f);
            hideImageAnimator.setDuration(600);
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mLastPosition == verticalOffset) {
            return;
        }
        mLastPosition = verticalOffset;
        float progress = Math.abs(verticalOffset / appBarLayout.getHeight());

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) getLayoutParams();
        params.topMargin = -verticalOffset;
        setLayoutParams(params);

        if (mToolBarOpen && progress > mTransitionThreshold) {
            mCloseToolBarSet.applyTo(this);
            hideImageAnimator.start();
            mToolBarOpen = false;
        } else if (!mToolBarOpen && progress < mTransitionThreshold) {
            mOpenToolBarSet.applyTo(this);
            showImageAnimator.start();
            mToolBarOpen = true;
        }
    }
}
