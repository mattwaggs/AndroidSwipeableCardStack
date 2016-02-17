package com.wenchao.cardstack;

import android.util.Log;

public class DefaultStackEventListener implements CardStack.CardEventListener {

    private static final String TAG = "DefaultStackListener";

    @Override
    public void onSwipeLeft(int mIndex) {
        Log.d(TAG, "Card Swiped Left");
    }

    @Override
    public void onSwipeRight(int mIndex) {
        Log.d(TAG, "Card Swiped Right");
    }

    @Override
    public void onCardTapped(int mIndex) {
        Log.d(TAG, "Card Tapped");
    }
}
