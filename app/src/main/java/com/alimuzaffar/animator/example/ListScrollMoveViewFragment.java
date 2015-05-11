package com.alimuzaffar.animator.example;

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

import static com.alimuzaffar.animator.example.Constants.X;
import static com.alimuzaffar.animator.example.Constants.Y;


public class ListScrollMoveViewFragment extends Fragment {

    public ListScrollMoveViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_listscrollanimator, container, false);

        final View header = inflater.inflate(R.layout.listview_header, null);
        final View headerTwo = inflater.inflate(R.layout.listview_header_two, null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.addHeaderView(header);
        listView.addHeaderView(headerTwo);
        listView.setAdapter(adapter);

        final View red = view.findViewById(R.id.red);
        final View green = view.findViewById(R.id.green);
        final View venue = headerTwo.findViewById(R.id.venue);
        final View line1 = headerTwo.findViewById(R.id.line1);
        final View line2 = headerTwo.findViewById(R.id.line2);
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.VISIBLE);
        venue.setVisibility(View.GONE);

        final TextView redTxt = (TextView) header.findViewById(R.id.red_txt);
        final TextView greenTxt = (TextView) header.findViewById(R.id.green_txt);
        final TextView team1 = (TextView) headerTwo.findViewById(R.id.team1);
        final TextView team2 = (TextView) headerTwo.findViewById(R.id.team2);
        final TextView vs = (TextView) headerTwo.findViewById(R.id.vs);

        //start positions for the animations
        final float[] redTeamXY = new float[2];
        final float[] greenTeamXY = new float[2];

        //end positions for the animation
        final float[] team1XYOrig = new float[2];
        final float[] team2XYOrig = new float[2];

        final float defaultTextSize = redTxt.getTextSize();
        final float minTextSize = spToPx(getActivity(), 15);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            float[] offsetRed = null;
            float[] offsetGreen = null;
            float[] redPPXY = new float[2];
            float[] greenPPXY = new float[2];
            int viewHeight = -1;

            float startPoint = 100;
            float endPoint = 0;
            float lastPixMoved = 0;

            private int mLastFirstVisibleItem;
            private boolean mIsScrollingUp;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int[] xy = new int[2];
                header.getLocationOnScreen(xy);

                int[] xy2 = new int[2];
                headerTwo.getLocationOnScreen(xy2);

                int size = xy[Y];
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

                if (xy[Y] <= startPoint) {
                    //start moving the textviews
                    if (offsetRed == null) {
                        offsetRed = new float[2];
                        offsetGreen = new float[2];

                        //update start point for the animation
                        redTeamXY[X] = redTxt.getX();
                        redTeamXY[Y] = redTxt.getY();
                        //redTxt.getLocationInWindow(redTeamXY);

                        greenTeamXY[X] = greenTxt.getX();
                        greenTeamXY[Y] = greenTxt.getY();
                        //greenTxt.getLocationInWindow(greenTeamXY);

                        int [] temp = new int[2];
                        //update the end points for the animation
                        team1.getLocationInWindow(temp);
                        team1XYOrig[X] = (int) team1.getX();
                        team1XYOrig[Y] = (int) team1.getY();


                        team2.getLocationInWindow(temp);
                        team2XYOrig[X] = (int) team2.getX();
                        team2XYOrig[Y] = temp[Y];


                        Log.d("HERE", String.format("TEAM1 X=%.2f, Y=%.2f", team1XYOrig[X], team1XYOrig[Y]));

                        //relative to the start point, how many pixels on X and Y is the end point.
                        offsetRed[X] = redTeamXY[X] - team1XYOrig[X];
                        offsetRed[Y] = (team1XYOrig[Y] - redTeamXY[Y])/4;

                        offsetGreen[X] = greenTeamXY[X] - team2XYOrig[X];
                        offsetGreen[Y] = (team2XYOrig[Y] - greenTeamXY[Y])/4;

                        //red per pixel xy
                        redPPXY[X] = offsetRed[X] / (startPoint - endPoint);
                        redPPXY[Y] = offsetRed[Y] / (startPoint - endPoint);

                        //green per pixel xy
                        greenPPXY[X] = offsetGreen[X] / (startPoint - endPoint);
                        greenPPXY[Y] = offsetGreen[Y] / (startPoint - endPoint);
                    }

                    //how many pixels have we moved
                    float pixMoved = startPoint - xy[Y];
                    if (pixMoved > 0) {
                        if (pixMoved != lastPixMoved) {
                            mIsScrollingUp = lastPixMoved < pixMoved;
                            lastPixMoved = pixMoved;
                        }
                        int [] redEndInWindow = new int[2];
                        team1.getLocationInWindow(redEndInWindow);
                        //X needs to be moved from the original position
                        float[] redLoc = { redTeamXY[X] - pixMoved * redPPXY[X], redTxt.getY() + redPPXY[Y] };
                        float[] greenLoc = { greenTeamXY[X] - pixMoved * greenPPXY[X], greenTxt.getY() + greenPPXY[Y] };


                        if (redLoc[X] >= team1XYOrig[X]) {
                            redTxt.setX(redLoc[X]);
                        }

                        if (greenLoc[X] >= team2XYOrig[X]) {
                            greenTxt.setX(greenLoc[X]);
                        }

                        //if (redLoc[Y] < temp[Y]) {
                           // Log.d("HERE", String.format("RED NEW Y=%.2f", team1XYOrig[Y]));
                        if (mIsScrollingUp) {
                            redTxt.setY(redTxt.getY() + 1);
                            greenTxt.setY(greenTxt.getY() + 1);
                        } else {
                            redTxt.setY(redTxt.getY() - 1);
                            greenTxt.setY(greenTxt.getY() - 1);
                        }

                    } else {
                        //do nothing, we are outside the animation area
                    }

                } else if (xy[Y] > startPoint && offsetRed != null) {
                    redTxt.setX(redTeamXY[X]);
                    greenTxt.setX(greenTeamXY[X]);
                    redTxt.setY(redTeamXY[Y]);
                    greenTxt.setY(greenTeamXY[Y]);

                } else if (xy[Y] < endPoint && offsetRed != null) {
                    redTxt.setX(team1XYOrig[X]);
                    greenTxt.setX(team2XYOrig[X]);
                    redTxt.setY(team1XYOrig[Y]);
                    greenTxt.setY(team2XYOrig[Y]);
                    //redTxt.setY(0);
                    //greenTxt.setY(0);
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
