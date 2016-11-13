package com.studypact.studypact.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Arvind on 11/12/2016.
 */

public class SquareCardView extends LinearLayout {

    public SquareCardView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int columns = 3;//edit this if you need different grid
        final int rows = 3;

        int children = getChildCount();
        if (children != columns * rows)
            throw new IllegalStateException("GridLayout must have " + columns * rows + " children");

        int width = getWidth();
        int height = getHeight();


        int viewWidth = width / columns;
        int viewHeight = height / rows;

        int rowIndex = 0;
        int columnIndex = 0;

        for (int i = 0; i < children; i++) {
            getChildAt(i).layout(viewWidth * columnIndex, viewHeight * rowIndex, viewWidth * columnIndex + viewWidth, viewHeight * rowIndex + viewHeight);
            columnIndex++;
            if (columnIndex == columns) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }
}
