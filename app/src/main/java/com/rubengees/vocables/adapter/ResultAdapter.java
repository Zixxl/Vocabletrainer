package com.rubengees.vocables.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.chart.ChartTools;
import com.rubengees.vocables.core.test.TestAnswer;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.utils.Utils;

import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Adapter to show the Results of a Test.
 */
public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FAB_MARGIN = 28 + 4;  //Half FAB size + Margin
    private static final int VIEW_TYPE_SPACE = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_RESULT = 2;

    private final Context context;
    private TestResult result;

    public ResultAdapter(TestResult result, Context context) {
        this.result = result;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                View space = new View(parent.getContext());

                space.setMinimumHeight(Utils.dpToPx(parent.getContext(), FAB_MARGIN));
                return new ViewHolderSpace(space);
            case 1:
                return new ViewHolderResultHeader(inflater.inflate(R.layout.list_item_result_header, parent, false));
            case 2:
                return new ViewHolderResult(inflater.inflate(R.layout.list_item_result, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderResultHeader) {
            ViewHolderResultHeader viewHolder = (ViewHolderResultHeader) holder;

            ChartTools.generateAnswerChart(viewHolder.answers, result.getCorrect(), result.getIncorrect());
            ChartTools.generateTimeChart(viewHolder.times, result.getBestTime(), result.getAverageTime());
        } else if (holder instanceof ViewHolderResult) {
            ViewHolderResult viewHolder = (ViewHolderResult) holder;
            TestAnswer current = result.getAnswerAt(position - 2);

            if (current.isCorrect()) {
                viewHolder.icon.setImageResource(R.drawable.ic_correct);
                viewHolder.correction.setText(null);
                viewHolder.correction.setVisibility(View.GONE);
            } else {
                viewHolder.icon.setImageResource(R.drawable.ic_incorrect);
                viewHolder.correction.setText(context.getString(R.string.fragment_result_list_item_correct) + " " + current.getAnswer());
                viewHolder.correction.setVisibility(View.VISIBLE);
            }

            viewHolder.answer.setText(current.getQuestion() + " - " + (current.getGiven() == null ? context.getString(R.string.fragment_result_list_item_no_answer) : current.getGiven()));
        }
    }

    @Override
    public int getItemCount() {
        return result.getCorrect() + result.getIncorrect() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= 0) {
            return VIEW_TYPE_SPACE;
        } else if (position == 1) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_RESULT;
        }
    }

    static class ViewHolderResultHeader extends RecyclerView.ViewHolder {

        PieChartView answers;
        ColumnChartView times;

        public ViewHolderResultHeader(View itemView) {
            super(itemView);

            answers = (PieChartView) itemView.findViewById(R.id.list_item_result_answers);
            times = (ColumnChartView) itemView.findViewById(R.id.list_item_result_times);
        }
    }

    static class ViewHolderResult extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView answer;
        TextView correction;

        public ViewHolderResult(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.list_item_result_icon);
            answer = (TextView) itemView.findViewById(R.id.list_item_result_answer);
            correction = (TextView) itemView.findViewById(R.id.list_item_result_correction);
        }
    }
}
