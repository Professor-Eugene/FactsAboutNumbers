package com.example.factsaboutnumbers.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.util.TempDataHolder;
import com.example.factsaboutnumbers.model.Number;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NumbersListAdapter extends RecyclerView.Adapter<NumbersListAdapter.NumberViewHolder> {

    private NumberSelectedListener numberSelectedListener;
    private List<Number> data = new ArrayList<>();

    LifecycleOwner lifecycleOwner;

    ViewModel viewModel;

    LiveData<List<Number>> numbersLiveData;

    NumbersListAdapter(LifecycleOwner lifecycleOwner, LiveData<List<Number>> numbersLiveData, NumberSelectedListener numberSelectedListener) {
        this.numberSelectedListener = numberSelectedListener;
        this.lifecycleOwner = lifecycleOwner;
        this.numbersLiveData = numbersLiveData;
        setHasStableIds(true);
        observe(numbersLiveData);
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.numbers_list_item, parent, false);
        return new NumberViewHolder(view, numberSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    void observe(LiveData<List<Number>> numbersLiveData) {
        this.numbersLiveData.removeObservers(lifecycleOwner);
        this.numbersLiveData = numbersLiveData;
        numbersLiveData.observe(lifecycleOwner, numbers -> {

            //  TempDataHolder link check is to avoid getting old list before updated by Room.
            if (TempDataHolder.list != numbers) {
//                L.og(numbers.toString());
                TempDataHolder.list = numbers;
            }
            data = TempDataHolder.list;
            notifyDataSetChanged();
        });
    }

    static final class NumberViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_number)
        TextView numberTextView;

        @BindView(R.id.tv_fact)
        TextView factTextView;

        private Number number;

        NumberViewHolder(View itemView, NumberSelectedListener numberSelectedListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (number != null) {
                    numberSelectedListener.onNumberSelected(number);
                }
            });
        }

        void bind(Number numberObject) {
            this.number = numberObject;
            String stringNumberValue = String.valueOf(numberObject.getNumber());
            numberTextView.setText(stringNumberValue);
            String rawFact = numberObject.getFact();
            String fact = rawFact.replaceFirst(stringNumberValue + " ", "");
            factTextView.setText(fact);
        }
    }
}