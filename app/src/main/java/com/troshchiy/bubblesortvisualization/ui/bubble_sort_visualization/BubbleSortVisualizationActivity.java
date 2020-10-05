package com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.troshchiy.bubblesortvisualization.R;

import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.ARGS_ARRAY_LENGTH;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.ARGS_ONE_STEP_SWAP_DRAW;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.ARGS_SWAP_DURATION;

public class BubbleSortVisualizationActivity extends AppCompatActivity {

    private final String TAG = BubbleSortVisualizationActivity.class.getSimpleName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_sort_visualization);

        Intent intent = getIntent();
        int length = intent.getIntExtra(ARGS_ARRAY_LENGTH, 30);
        int swapDuration = intent.getIntExtra(ARGS_SWAP_DURATION, 30);
        boolean oneStepSwapDraw = intent.getBooleanExtra(ARGS_ONE_STEP_SWAP_DRAW, true);

        if (savedInstanceState == null) { // Avoid call Fragment.onCreate() twice after rotate
            BubbleSortVisualizationFragment fragment = BubbleSortVisualizationFragment.newInstance(length, swapDuration, oneStepSwapDraw);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

}