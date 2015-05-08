package com.alimuzaffar.animator.example;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
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

        final TextView redTxt = (TextView) view.findViewById(R.id.red_txt);
        final TextView greenTxt = (TextView) view.findViewById(R.id.green_txt);
        final TextView team1 = (TextView) headerTwo.findViewById(R.id.team1);
        final TextView team2 = (TextView) headerTwo.findViewById(R.id.team2);
        final TextView vs = (TextView) headerTwo.findViewById(R.id.vs);

        final float defaultTextSize = redTxt.getTextSize();
        final float minTextSize = 16.0f;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int viewHeight = -1;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int[] xy = new int[2];
                header.getLocationOnScreen(xy);

                int[] xy2 = new int[2];
                headerTwo.getLocationOnScreen(xy2);

                Log.d("HERE", String.format("X=%d, Y=%d",xy[0], xy[1]));
                int size = xy[1];
                float textSize = defaultTextSize;
                if (viewHeight < 0) {
                    viewHeight = red.getMeasuredHeight();
                    if (viewHeight == 0) {
                        viewHeight = -1;
                        return;
                    }
                }

                if (size < 0) {
                    size = 0;
                }

                if (size > viewHeight) {
                    size = viewHeight;
                    textSize = defaultTextSize;
                }

                if (size < 50) {
                    red.setVisibility(View.INVISIBLE);
                    green.setVisibility(View.INVISIBLE);
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

                    Log.d("HERE", String.format("TEAM 1 X=%d, Y=%d", team1XY[0], team1XY[1]));

                    if (venue.getVisibility() == View.VISIBLE && xy[1] <= xy2[1] + headerTwo.getMeasuredHeight()) {

                        venue.setVisibility(View.GONE);
                        line1.setVisibility(View.VISIBLE);
                        line2.setVisibility(View.VISIBLE);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(redTxt, "x", 0, team1XY[0] - redXY[0]),
                                ObjectAnimator.ofFloat(redTxt, "y", 0, team1XY[1] - redXY[1]),
                                ObjectAnimator.ofFloat(greenTxt, "x", 0, team2XY[0] - greenXY[0]),
                                ObjectAnimator.ofFloat(greenTxt, "y", 0, team2XY[1] - greenXY[1])
                        );
                        set.setDuration(300).start();

                    } else if (venue.getVisibility() == View.GONE && xy[1] > xy2[1] + headerTwo.getMeasuredHeight()) {
                        venue.setVisibility(View.VISIBLE);
                        line1.setVisibility(View.INVISIBLE);
                        line2.setVisibility(View.INVISIBLE);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(redTxt, "x", team1XY[0] - redXY[0], 0),
                                ObjectAnimator.ofFloat(redTxt, "y", team1XY[1] - redXY[1], 0),
                                ObjectAnimator.ofFloat(greenTxt, "x", team2XY[0] - greenXY[0], 0),
                                ObjectAnimator.ofFloat(greenTxt, "y", team2XY[1] - greenXY[1], 0)
                        );
                        set.setDuration(300).start();

                    }

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



}
