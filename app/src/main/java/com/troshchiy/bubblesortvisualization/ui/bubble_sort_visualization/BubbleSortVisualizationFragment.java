package com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.troshchiy.bubblesortvisualization.R;
import com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.widget.BubbleSortGraphWidget;
import com.troshchiy.bubblesortvisualization.util.ArrayUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.BubbleSortTask.ARGS_I;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.BubbleSortTask.ARGS_IS_BIGGER;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.BubbleSortTask.ARGS_J;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.BubbleSortTask.ARGS_LAST_INDEX_FOR_INNER_LOOP;

public class BubbleSortVisualizationFragment extends Fragment {

    private final String TAG = BubbleSortVisualizationFragment.class.getSimpleName();

    public static final String ARGS_ARRAY_LENGTH       = "args_array_length";
    public static final String ARGS_SWAP_DURATION      = "args_swap_duration";
    public static final String ARGS_ONE_STEP_SWAP_DRAW = "args_one_step_swap_draw";

    private static final String ARGS_ARRAY = "args_array";

    private static final String ARGS_STATE = "args_state";

    private static final int MAX_VALUE_IN_ARRAY = 999;

    @Bind(R.id.bubbleSortGraph) BubbleSortGraphWidget bubbleSortGraph;

    private static int[] array;

    private int     length;
    private int     swapDuration;
    private boolean oneStepSwapDraw;

    private final Handler handler = new Handler(Looper.getMainLooper());

    class BubbleSortTask implements Runnable {

        public static final String ARGS_I                         = "args_i";
        public static final String ARGS_J                         = "args_j";
        public static final String ARGS_LAST_INDEX_FOR_INNER_LOOP = "args_last_index_for_inner_loop";
        public static final String ARGS_IS_BIGGER                 = "args_is_bigger";

        private static final int NOT_SET = -1;

        private int i;
        private int j;
        private int lastIndexForInnerLoop = NOT_SET;

        private boolean drawBeforeSwap = true;
        private boolean wasSwap;
        private boolean isBigger;

        private boolean firstRun = true;

        @Override public void run() {
            if (firstRun) { // If we start sorting update status
                bubbleSortGraph.setState(BubbleSortGraphWidget.STATE.RUN);
                firstRun = false;
            }

            if (oneStepSwapDraw) {
                beforeSwapPart();
                afterSwapPartDraw();
            } else {
                if (drawBeforeSwap) {
                    beforeSwapPart();
                    bubbleSortGraph.postInvalidate();
                    handler.postDelayed(this, swapDuration); // Draw two elements that will be compared
                } else {
                    afterSwapPartDraw();
                }
            }
        }

        private void beforeSwapPart() {
            isBigger = array[j] > array[j + 1];
            bubbleSortGraph.setIsBigger(isBigger);
            bubbleSortGraph.setCurrentSortIndex(j);
            bubbleSortGraph.setDrawBeforeSwap(true);

            lastIndexForInnerLoop = calculateLastIndexForInnerLoop();
            bubbleSortGraph.setLastIndex(lastIndexForInnerLoop);

            drawBeforeSwap = false;
        }

        private void afterSwapPartDraw() {
            if (isBigger) {
                ArrayUtils.swap(array, j, j + 1);
                wasSwap = true;

                int[] clone = array.clone();
                bubbleSortGraph.setArray(clone);
            }

            bubbleSortGraph.setDrawBeforeSwap(false);
            bubbleSortGraph.postInvalidate();

            j++;
            drawBeforeSwap = true;

            if (j < lastIndexForInnerLoop) { // Inner loop
                handler.postDelayed(this, swapDuration);
            } else if (i < length - 1 && wasSwap) { // Outer loop
                wasSwap = false;
                i++;
                j = 0;
                handler.postDelayed(this, swapDuration);
            } else {
                finalGraphDraw();
            }
        }

        public int calculateLastIndexForInnerLoop() {
            return length - i - 1;
        }

        private void finalGraphDraw() {
            bubbleSortGraph.setState(BubbleSortGraphWidget.STATE.FINISH);
            bubbleSortGraph.postInvalidate();
        }
    }

    private final BubbleSortTask bubbleSortTask = new BubbleSortTask();

    public static BubbleSortVisualizationFragment newInstance(int length, int swapDuration, boolean oneStepSwapDraw) {
        Bundle args = new Bundle();
        args.putInt(ARGS_ARRAY_LENGTH, length);
        args.putInt(ARGS_SWAP_DURATION, swapDuration);
        args.putBoolean(ARGS_ONE_STEP_SWAP_DRAW, oneStepSwapDraw);

        BubbleSortVisualizationFragment fragment = new BubbleSortVisualizationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bubble_sort_visualization, null);
        ButterKnife.bind(this, v);

        Bundle args = getArguments();
        length = args.getInt(ARGS_ARRAY_LENGTH);
        swapDuration = args.getInt(ARGS_SWAP_DURATION);
        oneStepSwapDraw = args.getBoolean(ARGS_ONE_STEP_SWAP_DRAW);

        if (savedInstanceState != null) {
            bubbleSortTask.i = savedInstanceState.getInt(ARGS_I, 0);
            int j = savedInstanceState.getInt(ARGS_J, 0);
            bubbleSortTask.j = j;

            bubbleSortGraph.setCurrentSortIndex(j);
            bubbleSortGraph.setLastIndex(savedInstanceState.getInt(ARGS_LAST_INDEX_FOR_INNER_LOOP, 0));
            bubbleSortGraph.setState(
                    BubbleSortGraphWidget.STATE.valueOf(savedInstanceState.getString(ARGS_STATE))
            );
            bubbleSortGraph.setIsBigger(savedInstanceState.getBoolean(ARGS_IS_BIGGER));

            array = savedInstanceState.getIntArray(ARGS_ARRAY);
        } else {
            array = ArrayUtils.generateUnsortedArray(length, MAX_VALUE_IN_ARRAY);
        }

        initBubbleSortGraph();

        return v;
    }

    private void initBubbleSortGraph() {
        bubbleSortGraph.setOneStepSwapDraw(oneStepSwapDraw);
        bubbleSortGraph.setArray(array);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sortintg_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restart:
                restart();
                return true;
            default:
                return false;
        }
    }

    private void restart() {
        bubbleSortTask.i = 0;
        bubbleSortTask.j = 0;

        array = ArrayUtils.generateUnsortedArray(length, MAX_VALUE_IN_ARRAY);

        bubbleSortGraph.setState(BubbleSortGraphWidget.STATE.RUN);
        bubbleSortGraph.setArray(array);

        handler.removeCallbacks(bubbleSortTask);
        handler.postDelayed(bubbleSortTask, 1000); // Time to view unsorted array before start sorting.
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bubbleSortGraph.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                Log.i(TAG, "onGlobalLayout");
                                bubbleSortGraph.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                bubbleSortGraph.generateXandYratio();
                                handler.postDelayed(bubbleSortTask, 1000); // Time to view unsorted array before start sorting.
                            }
                        }
                );
    }

    @Override public void onStop() {
        super.onStop();
        handler.removeCallbacks(bubbleSortTask);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARGS_I, bubbleSortTask.i);
        outState.putInt(ARGS_J, bubbleSortTask.j);
        outState.putInt(ARGS_LAST_INDEX_FOR_INNER_LOOP, bubbleSortTask.lastIndexForInnerLoop);
        outState.putBoolean(ARGS_IS_BIGGER, bubbleSortTask.isBigger);

        outState.putString(ARGS_STATE, bubbleSortGraph.getState().toString());

        outState.putIntArray(ARGS_ARRAY, array);
    }

}