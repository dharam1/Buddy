package com.example.dharmendra.buddy1;

import android.animation.Animator;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

/**
 * Created by karthik on 1/10/17.
 */

/**
 * This class is used to implement scroll listener for {@code Chat}. It defines {@code onScroll()} and {@code onScrollStateChanged()}
 * It animates the date textview to be displayed on Chat screen on scroll up, and hides it after 1000ms.
 */
public class ChatOnScrollListener implements AbsListView.OnScrollListener {

    private TextView dateView;
    private MessageAdapter adapter;
    private int distance;

    ChatOnScrollListener(TextView dateView, MessageAdapter adapter){
        this.dateView = dateView;
        this.adapter = adapter;
        this.distance = dateView.getTop();
    }

    /**
     * private method to animate the date textview. Animation lasts 500 ms.
     */
    private void showDate(){
        dateView.animate().translationY(dateView.getBottom()).alpha(1f).setDuration(100).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                //dateView.setVisibility(View.VISIBLE);
                Log.v("anim", "starting scroll");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                control = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //dateView.setVisibility(View.VISIBLE);
                control = true;
                Log.v("anim", "starting scroll");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //dateView.setVisibility(View.VISIBLE);

            }
        }).start();
    }

    /**
     * Overriden method from OnScrollListener interface. if scroll state is idle, it starts a fade out animation, with a start delay of 500 ms.
     *
     * @param absListView the ListView on which the listener is implemented
     * @param i the scroll state: Has 3 states, SCROLL_STATE_IDLE, SCROLL_STATE_FLING, SCROLL_STATE_TOUCH_SCREEN.
     */
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        Log.v("anim", " scroll state changed to "+i );
        Log.v("anim", "animRunning =" +control);
        if(i == SCROLL_STATE_IDLE && control){
            dateView.animate().alpha((0f)).translationY(distance).setDuration(500).setStartDelay(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //dateView.setVisibility(View.INVISIBLE);
                    control = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    control = false;
                    //dateView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    }
    private int last = 0;
    private boolean control = false;

    /**
     * Overriden method from OnScrollListener interface,it is called whenever ListView is scrolled.
     * Calls showDate() whenever control is not given to fade. I.E, calls it whenever dateTextView is INVISIBLE and listview is scrolled.
     * @param absListView the ListView on which the listener is implemented on.
     * @param topIndex The index of the first item in the screen.
     * @param totalInView Number of total items in view.
     * @param totalCount Count of all items in the ListView.
     */
    @Override
    public void onScroll(AbsListView absListView, int topIndex, int totalInView, int totalCount) {
        if (totalCount > 0) {
            dateView.setText(DateOperations.toString(adapter.getItem(topIndex).getMessageTime()));
            if (topIndex < last && !control) {
                showDate();
            }
            last = topIndex;
        }
    }
}