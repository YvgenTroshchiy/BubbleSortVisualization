package com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.troshchiy.bubblesortvisualization.R;
import com.troshchiy.bubblesortvisualization.util.ArrayUtils;
import com.troshchiy.bubblesortvisualization.util.UiUtils;

//TODO: change to Drawable ?
public class BubbleSortGraphWidget extends View {

    private final String TAG = BubbleSortGraphWidget.class.getSimpleName();

    private int[] array;

    private static final int MARGIN;
    private static final int COLUMN_STROKE_WIDTH;
    private static final int HALF_COLUMN_STROKE_WIDTH;

    private double width;
    private double height;

    private float columnWidth;
    private float yRatio;

    private final RectF rect = new RectF();
    private Paint strokePaint;
    private Paint unsortedFillPaint;
    private Paint activeSwappedFillPaint;
    private Paint activeSmallerFillPaint;
    private Paint activeBiggerFillPaint;
    private Paint sortedFillPaint;

    static {
        MARGIN = UiUtils.convertDpToPx(16);
        COLUMN_STROKE_WIDTH = UiUtils.convertDpToPx(1);
        HALF_COLUMN_STROKE_WIDTH = COLUMN_STROKE_WIDTH / 2;
    }

    private boolean oneStepSwapDraw;

    private int currentSortIndex;
    private int lastIndex;
    private boolean drawBeforeSwap;
    private boolean isBigger;

    public enum STATE {PENDING, RUN, FINISH}

    private STATE state = STATE.PENDING;

    public BubbleSortGraphWidget(Context context) {
        super(context);
        init();
    }

    public BubbleSortGraphWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleSortGraphWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.i(TAG, "init");

        if (isInEditMode()) {
            return;
        }

        initStrokePaint();
        initUnsortedFillPaint();
        initActiveSmallerFillPaint();
        initActiveBiggerFillPaint();
        initActiveSwapperFillPaint();
        initSortedFillPaint();
    }

    private void initStrokePaint() {
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(COLUMN_STROKE_WIDTH);
        strokePaint.setColor(getResources().getColor(android.R.color.black));
    }

    private void initUnsortedFillPaint() {
        unsortedFillPaint = new Paint();
        unsortedFillPaint.setStyle(Paint.Style.FILL);
        unsortedFillPaint.setColor(getResources().getColor(R.color.grey_400));
    }

    private void initActiveSmallerFillPaint() {
        activeSmallerFillPaint = new Paint();
        activeSmallerFillPaint.setStyle(Paint.Style.FILL);
        activeSmallerFillPaint.setColor(getResources().getColor(R.color.light_blue_500));
    }

    private void initActiveBiggerFillPaint() {
        activeBiggerFillPaint = new Paint();
        activeBiggerFillPaint.setStyle(Paint.Style.FILL);
        activeBiggerFillPaint.setColor(getResources().getColor(R.color.deep_orange_500));
    }

    private void initActiveSwapperFillPaint() {
        activeSwappedFillPaint = new Paint();
        activeSwappedFillPaint.setStyle(Paint.Style.FILL);
        activeSwappedFillPaint.setColor(getResources().getColor(R.color.grey_600));
    }

    private void initSortedFillPaint() {
        sortedFillPaint = new Paint();
        sortedFillPaint.setStyle(Paint.Style.FILL);
        sortedFillPaint.setColor(getResources().getColor(R.color.green_500));
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //TODO: Sometimes get value excluding action bar height.
        Log.i(TAG, "onMeasure" + " w: " + width + ", h: " + height);
    }

    @Override protected void onDraw(Canvas canvas) {
        if (array == null) {
            return;
        }

        float left;
        float right;
        float top;
        float bottom = (float) height - MARGIN;

        for (int i = 0; i < array.length; i++) {
            left = MARGIN + columnWidth * i;
            right = left + columnWidth;
            top = bottom - (array[i] * yRatio);
            rect.set(left, top, right, bottom);

            switch (state) {
                case PENDING:
                    canvas.drawRect(rect, unsortedFillPaint); // Unsorted
                    break;
                case RUN:
                    if (i == currentSortIndex || i == currentSortIndex + 1) {
                        if (drawBeforeSwap || oneStepSwapDraw) {
                            canvas.drawRect(rect, isBigger ? activeBiggerFillPaint : activeSmallerFillPaint); // Active
                        } else {
                            canvas.drawRect(rect, activeSwappedFillPaint); // Swapped
                        }
                    } else if (i > lastIndex) {
                        canvas.drawRect(rect, sortedFillPaint); // Sorted
                    } else {
                        canvas.drawRect(rect, unsortedFillPaint); // Unsorted
                    }
                    break;
                case FINISH:
                    canvas.drawRect(rect, sortedFillPaint); // Sorted
                    break;
            }

            // Offset from current rect for inner stroke type
            rect.set(
                    left + HALF_COLUMN_STROKE_WIDTH,
                    top - HALF_COLUMN_STROKE_WIDTH,
                    right + HALF_COLUMN_STROKE_WIDTH,
                    bottom - HALF_COLUMN_STROKE_WIDTH
            );
            canvas.drawRect(rect, strokePaint);
        }
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public void setOneStepSwapDraw(boolean oneStepSwapDraw) {
        this.oneStepSwapDraw = oneStepSwapDraw;
    }

    public void setCurrentSortIndex(int currentSortIndex) {
        this.currentSortIndex = currentSortIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public void setDrawBeforeSwap(boolean drawBeforeSwap) {
        this.drawBeforeSwap = drawBeforeSwap;
    }

    public void setIsBigger(boolean isBigger) {
        this.isBigger = isBigger;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public STATE getState() {
        return state;
    }

    //TODO: Rename or split
    public void generateXAndYRatio() {
        columnWidth = getColumnWidth();
        yRatio = getYRatio();
    }

    private float getColumnWidth() {
        return (float) ((width - (MARGIN * 2)) / array.length);
    }

    private float getYRatio() {
        return (float) ((height - (MARGIN * 2)) / ArrayUtils.getArrayMaxValue(array));
    }

}