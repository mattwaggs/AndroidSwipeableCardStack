package com.wenchao.cardstack;

import android.util.Log;
import android.view.View;

public class DefaultStackEventListener implements CardStack.CardEventListener {

    private static final String TAG = "DefaultStackListener";

    @Override
    public void onMoveLeft(View topCard, float mThresholdPercentage) {
        Log.d(TAG, "Card moving left, threshold: " + mThresholdPercentage);
    }

    @Override
    public void onMoveRight(View topCard, float mThresholdPercentage) {
        Log.d(TAG, "Card moving right, threshold: " + mThresholdPercentage);
    }

    @Override
    public void onMoveCanceled(View topCard) {
        Log.d(TAG, "Card move canceled");
    }

    @Override
    public void onSwipedLeft(int mIndex) {
        Log.d(TAG, "Card Swiped Left");
    }

    @Override
    public void onSwipedRight(int mIndex) {
        Log.d(TAG, "Card Swiped Right");
    }

    @Override
    public void onCardTapped(int mIndex) {
        Log.d(TAG, "Card Tapped");
    }

    @Override
    public void readyForNext() {

    }
}
