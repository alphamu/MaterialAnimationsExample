package com.alimuzaffar.animator.example;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        String [] hundred = new String[100];
        for (int i= 0 ; i < 100; i++) {
            hundred[i] = "List Item " + i;
        }

        final View header = inflater.inflate(R.layout.listview_header, null);
        final View headerTwo = inflater.inflate(R.layout.listview_header_two, null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, hundred);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.addHeaderView(header);
        listView.addHeaderView(headerTwo);
        listView.setAdapter(adapter);

        final View red = view.findViewById(R.id.red);
        final View green = view.findViewById(R.id.green);
        final View venue = headerTwo.findViewById(R.id.venue);
        final View line1 = headerTwo.findViewById(R.id.line1);
        final View line2 = headerTwo.findViewById(R.id.line2);

        final TextView redTxt = (TextView) header.findViewById(R.id.red_txt);
        final TextView greenTxt = (TextView) header.findViewById(R.id.green_txt);
        final TextView team1 = (TextView) headerTwo.findViewById(R.id.team1);
        final TextView team2 = (TextView) headerTwo.findViewById(R.id.team2);
        final TextView vs = (TextView) headerTwo.findViewById(R.id.vs);

        //animate text to new header
        int[] team1XYOrig = new int[2];
        team1.getLocationOnScreen(team1XYOrig);

        int[] team2XYOrig = new int[2];
        team2.getLocationOnScreen(team2XYOrig);


        final float defaultTextSize = redTxt.getTextSize();
        final float minTextSize = spToPx(getActivity(), 15);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            int[] redXYOrig = null;
            int[] greenXYOrig = null;
            int[] venueXYOrig = null;

            int viewHeight = -1;
            boolean postAnimatedState = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int[] xy = new int[2];
                header.getLocationOnScreen(xy);

                int[] xy2 = new int[2];
                headerTwo.getLocationOnScreen(xy2);

                int size = xy[1];
                float textSize = defaultTextSize;
                if (viewHeight < 0) {
                    viewHeight = red.getMeasuredHeight();
                    if (viewHeight == 0) {
                        viewHeight = -1;
                        return;
                    }
                }

                if (size <= 0) {
                    size = 0;
                }

                if (size > viewHeight) {
                    size = viewHeight;
                    textSize = defaultTextSize;
                }

                if (size < 50) {
                    textSize = minTextSize;
                } else {
                    red.setVisibility(View.VISIBLE);
                    green.setVisibility(View.VISIBLE);
                    float multi = ((float) viewHeight) / ((float) size);
                    textSize = defaultTextSize - (multi * 5);

                    if (textSize < minTextSize) {
                        textSize = minTextSize;
                    }

                    if (textSize > defaultTextSize) {
                        textSize = defaultTextSize;
                    }

                }

                if (size == 0) {
                    //animate text to new header
                    int[] team1XY = new int[2];
                    team1.getLocationOnScreen(team1XY);

                    int[] team2XY = new int[2];
                    team2.getLocationOnScreen(team2XY);

                    int[] redXY = new int[2];
                    redTxt.getLocationOnScreen(redXY);

                    int[] greenXY = new int[2];
                    greenTxt.getLocationOnScreen(greenXY);

                    if (redXYOrig == null) {
                        redXYOrig = new int[2];
                        greenXYOrig = new int[2];
                        venueXYOrig = new int[2];
                        redTxt.getLocationOnScreen(redXYOrig);
                        greenTxt.getLocationOnScreen(greenXYOrig);
                        venue.getLocationOnScreen(venueXYOrig);
                    }

                    Log.d("HERE", String.format("XY2 X=%d, Y=%d, VIEW %d", xy2[0], xy2[1], redXY[1] + red.getMeasuredHeight()));

                    if (!postAnimatedState && xy[1] < size) {
                        postAnimatedState = true;
                        AnimatorSet set = new AnimatorSet();
                        set.play(ObjectAnimator.ofFloat(redTxt, "x", 0))
                                .with(ObjectAnimator.ofFloat(redTxt, "y", team1XY[1]));

                        set.play(ObjectAnimator.ofFloat(greenTxt, "x", team2XY[0] - dpToPx(getActivity(), 16)))
                                .with(ObjectAnimator.ofFloat(greenTxt, "y", team2XY[1]));

                        set.play(ObjectAnimator.ofFloat(venue, "y", line2.getY()));

                        set.setDuration(200).start();
                        set.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                venue.setAlpha(0);
                                line1.setVisibility(View.VISIBLE);
                                line2.setVisibility(View.VISIBLE);
                                team1.setVisibility(View.VISIBLE);
                                team2.setVisibility(View.VISIBLE);
                                redTxt.setAlpha(0);
                                greenTxt.setAlpha(0);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                    }

                } else if (postAnimatedState && xy[1] >= size) {
                    postAnimatedState = false;
                    Log.d("HERE", String.format("ORIG X=%d, Y=%d", redXYOrig[0], redXYOrig[1]));

                    venue.setAlpha(1);
                    line1.setVisibility(View.INVISIBLE);
                    line2.setVisibility(View.INVISIBLE);
                    redTxt.setVisibility(View.VISIBLE);
                    greenTxt.setVisibility(View.VISIBLE);
                    AnimatorSet set = new AnimatorSet();
                    set.play(ObjectAnimator.ofFloat(redTxt, "x", redXYOrig[0]))
                            .with(ObjectAnimator.ofFloat(redTxt, "y", redXYOrig[1]))
                            .with(ObjectAnimator.ofFloat(redTxt, "alpha", 1));

                    set.play(ObjectAnimator.ofFloat(greenTxt, "x", greenXYOrig[0]))
                            .with(ObjectAnimator.ofFloat(greenTxt, "y", greenXYOrig[1]))
                            .with(ObjectAnimator.ofFloat(greenTxt, "alpha", 1));

                    set.play(ObjectAnimator.ofFloat(venue, "y", line1.getY()));

                    set.setDuration(200).start();

                }


                ViewGroup.LayoutParams params = red.getLayoutParams();
                params.height = size;
                params.width = size;
                red.setLayoutParams(params);

                ViewGroup.LayoutParams params1 = green.getLayoutParams();
                params1.height = size;
                params1.width = size;
                green.setLayoutParams(params1);

                greenTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                redTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);



            }
        });



        return view;
    }


    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public static int spToPx(Context context, int sp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
        return (int) px;
    }


}
