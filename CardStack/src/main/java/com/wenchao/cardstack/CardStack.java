package com.wenchao.cardstack;

import android.content.res.TypedArray;
import java.util.ArrayList;
import java.util.Queue;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.database.DataSetObserver;


public class CardStack extends RelativeLayout {
    private int mColor = -1;
    private int mIndex = 0;
    private int mNumVisible = 4;
    private ArrayAdapter<?> mAdapter;
    private OnTouchListener mOnTouchListener;
    private CardAnimator mCardAnimator;

    private int mThreshold = 400;

    private CardEventListener mEventListener = new DefaultStackEventListener();
    private int mContentResource = 0;

    private ImageButton currentYesImage;
    private ImageButton currentNoImage;

    public interface CardEventListener {
        void onMoveLeft(View topCard, float mThresholdPercentage);
        void onMoveRight(View topCard, float mThresholdPercentage);
        void onMoveCanceled(View topCard);
        void onSwipedLeft(int mIndex);
        void onSwipedRight(int mIndex);
        void onCardTapped(int mIndex);
        void readyForNext();

    }

    public int getThreshold() {
        return mThreshold;
    }

    public void setThreshold(int mThreshold) {
        this.mThreshold = mThreshold;
    }

    public void discardTop(final int direction){
        mCardAnimator.discard(direction, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                mCardAnimator.initLayout();
                mIndex++;
                if(direction == 0 || direction == 2) {
                    mEventListener.onSwipedLeft(mIndex);
                }else{
                    mEventListener.onSwipedRight(mIndex);
                }

                //mIndex = mIndex%mAdapter.getCount();
                loadLast();

                viewCollection.get(0).setOnTouchListener(null);
                viewCollection.get(viewCollection.size() - 1).setOnTouchListener(mOnTouchListener);
            }
        });
    }

    public int getCurrIndex(){
        //sync?
        return mIndex;
    }

    //only necessary when I need the attrs from xml, this will be used when inflating layout
    public CardStack(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CardStack);
            mColor = array.getColor(R.styleable.CardStack_backgroundColor, mColor);
            array.recycle();
        }

        //String sMyValue = attrs.getAttributeValue( "http://schemas.android.com/apk/res/android", "padding" );
        //get attrs assign minVisiableNum
        for(int i = 0; i<mNumVisible; i++){
            addContainerViews();
        }
        setupAnimation();
    }

    private void addContainerViews(){
        FrameLayout v =  new FrameLayout(getContext());
        viewCollection.add(v);
        addView(v);
    }

    public void setContentResource(int res){
        mContentResource = res;
    }

    public void reset(boolean resetIndex){
        if(resetIndex) mIndex = 0;
        removeAllViews();
        viewCollection.clear();
        for(int i = 0; i<mNumVisible; i++){
            addContainerViews();
        }
        setupAnimation();
        loadData();
        mEventListener.readyForNext();
    }

    public void setListener(CardEventListener cel){
        mEventListener = cel;
    }

    private void setupAnimation(){
        final View cardView = viewCollection.get(viewCollection.size()-1);
        mCardAnimator = new CardAnimator(viewCollection, mColor);
        mCardAnimator.initLayout();

        final DragGestureDetector dd = new DragGestureDetector(CardStack.this.getContext(),new DragGestureDetector.DragListener(){

            @Override
            public  boolean onDragStart(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                mCardAnimator.drag(e1,e2,distanceX,distanceY);
                return true;
            }

            @Override
            public boolean onDragContinue(MotionEvent e1, MotionEvent e2,
                                       float distanceX, float distanceY) {
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                final int direction = CardUtils.direction(x1,y1,x2, y2);
                mCardAnimator.drag(e1,e2,distanceX,distanceY);

                float distance = CardUtils.distance(x1,0,x2,0);
                if(direction == 0  || direction == 2) {
                    // get the current card and color red
                    mEventListener.onMoveLeft(mCardAnimator.getTopView(), (distance / (float)mThreshold));
                }else{
                    // get the current card and color green
                    mEventListener.onMoveRight(mCardAnimator.getTopView(), (distance / (float)mThreshold));
                }

                return true;
            }

            @Override
            public  boolean onDragEnd(MotionEvent e1, MotionEvent e2) {
                //reverse(e1,e2);
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                float distance = CardUtils.distance(x1,0,x2,0); // ignore vertical distance.
                final int direction = CardUtils.direction(x1,y1,x2,y2);

                boolean discard = mThreshold < distance;

                if(discard) {

                    if(direction == 0 || direction == 2) {
                        mEventListener.onSwipedLeft(mIndex);
                    }else{
                        mEventListener.onSwipedRight(mIndex);
                    }

                    mCardAnimator.discard(direction, new AnimatorListenerAdapter(){

                        @Override
                        public void onAnimationEnd(Animator arg0) {
                            mCardAnimator.initLayout();
                            mIndex++;

                            //mIndex = mIndex%mAdapter.getCount();
                            loadLast();

                            viewCollection.get(0).setOnTouchListener(null);
                            viewCollection.get(viewCollection.size()-1)
                                    .setOnTouchListener(mOnTouchListener);
                        }

                    });
                }else{
                    mCardAnimator.reverse(e1,e2);
                    mEventListener.onMoveCanceled(mCardAnimator.getTopView());
                }

                return true;
            }

            @Override
            public boolean onTapUp() {
                mEventListener.onCardTapped(mIndex);
                return true;
            }
        }
        );

        mOnTouchListener = new OnTouchListener() {
            private static final String DEBUG_TAG = "MotionEvents";
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                dd.onTouchEvent(event);
                return true;
            }
        };
        cardView.setOnTouchListener(mOnTouchListener);
    }

    private DataSetObserver mOb = new DataSetObserver(){
        @Override
        public void onChanged(){
            reset(false);
        }
    };

    //ArrayList
    ArrayList<View> viewCollection = new ArrayList<View>();
    public CardStack(Context context) {
        super(context);
    }

    public void setAdapter(final ArrayAdapter<?> adapter){
        if(mAdapter != null){
            mAdapter.unregisterDataSetObserver(mOb);
        }
        mAdapter = adapter;
        adapter.registerDataSetObserver(mOb);

        loadData();
    }

    public ArrayAdapter getAdapter() {
        return mAdapter;
    }

    private void loadData(){
        for(int i=mNumVisible-1 ; i>=0 ; i--) {
            ViewGroup parent = (ViewGroup) viewCollection.get(i);
            int index = (mIndex + mNumVisible - 1) - i;
            if (index > mAdapter.getCount() - 1) {
                parent.setVisibility(View.GONE);
            }else{
                View child = mAdapter.getView(index, getContentView(), this);
                parent.addView(child);
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

    private View getContentView(){
        View contentView = null;
        if(mContentResource != 0) {
            LayoutInflater lf = LayoutInflater.from(getContext());
            contentView = lf.inflate(mContentResource,null);
        }
        return contentView;

    }

    private void loadLast(){
        ViewGroup parent = (ViewGroup)viewCollection.get(0);

        int lastIndex = (mNumVisible - 1)+ mIndex;
        if(lastIndex > mAdapter.getCount() -1 ){
            parent.setVisibility(View.GONE);
            return;
        }

        View child = mAdapter.getView( lastIndex, getContentView(), parent);
        parent.removeAllViews();
        parent.addView(child);
    }

    public int getStackSize() {
        return mNumVisible;
    }

    public void forceSwipeLeft() {
        mEventListener.onSwipedLeft(this.getCurrIndex());
    }
    public void forceSwipeRight() {
        mEventListener.onSwipedRight(this.getCurrIndex());
    }

    public View getTopView() {
        return mCardAnimator.getTopView();
    }

}
