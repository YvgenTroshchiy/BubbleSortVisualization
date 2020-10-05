package com.troshchiy.bubblesortvisualization.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.troshchiy.bubblesortvisualization.R;
import com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.ARGS_ARRAY_LENGTH;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.ARGS_ONE_STEP_SWAP_DRAW;
import static com.troshchiy.bubblesortvisualization.ui.bubble_sort_visualization.BubbleSortVisualizationFragment.ARGS_SWAP_DURATION;

public class MainFragment extends Fragment {

    private final String TAG = MainFragment.class.getSimpleName();

    private static final int NOT_SET = -1;

    private static final int MIN_ARRAY_LENGTH = 2;
    private static final int MAX_ARRAY_LENGTH = 200;

    @Bind(R.id.edt_array_length) EditText edt_array_length;
    @Bind(R.id.edt_swap_duration) EditText edt_swap_duration;
    @Bind(R.id.checkbox_isOneStepSwapDraw) CheckBox checkbox_isOneStepSwapDraw;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_visualize_sorting) void onBtnVisualizeSortingClick() {
        if (validateFields()) {
            Intent intent = new Intent(getActivity(), BubbleSortVisualizationActivity.class);
            intent.putExtra(ARGS_ARRAY_LENGTH, Integer.parseInt(edt_array_length.getText().toString()));
            intent.putExtra(ARGS_SWAP_DURATION, Integer.parseInt(edt_swap_duration.getText().toString()));
            intent.putExtra(ARGS_ONE_STEP_SWAP_DRAW, checkbox_isOneStepSwapDraw.isChecked());
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_isOneStepSwapDraw) void isOneStepSwapDraClick() {
        checkbox_isOneStepSwapDraw.performClick();
    }

    @OnClick(R.id.btn_visualize_sorting_default_value) void btnVisualizeSortingDefaultValueClick() {
        Intent intent = new Intent(getActivity(), BubbleSortVisualizationActivity.class);
        intent.putExtra(ARGS_ARRAY_LENGTH, 30);
        intent.putExtra(ARGS_SWAP_DURATION, 30);
        intent.putExtra(ARGS_ONE_STEP_SWAP_DRAW, true);
        startActivity(intent);
    }

    private boolean validateFields() {
        //FIXME: Add correct validation. Check count of elements that can be fits on screen
        int length = NOT_SET;
        if (!TextUtils.isEmpty(edt_array_length.getText())) {
            length = Integer.parseInt(edt_array_length.getText().toString());
        }

        if (length == NOT_SET || length < MIN_ARRAY_LENGTH || length > MAX_ARRAY_LENGTH) {
            edt_array_length.setError(String.format(getString(R.string.error_length), MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH));
            return false;
        }

        //FIXME: Add correct validation
        if (edt_swap_duration.getText().toString().isEmpty()) {
            edt_swap_duration.setError(getString(R.string.error_enter_non_empty_value));
            return false;
        }

        return true;
    }

}