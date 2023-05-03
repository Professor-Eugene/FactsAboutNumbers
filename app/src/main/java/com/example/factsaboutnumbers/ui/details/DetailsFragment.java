package com.example.factsaboutnumbers.ui.details;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.base.BaseFragment;
import com.example.factsaboutnumbers.model.Number;

import javax.inject.Inject;

import butterknife.BindView;

public class DetailsFragment extends BaseFragment {

    @BindView(R.id.details_number)
    TextView numberTextView;

    @BindView(R.id.details_text)
    TextView factTextView;

    @BindView(R.id.details_loading_view)
    View loadingView;

    @BindView(R.id.details_tv_error)
    TextView errorTextView;

    ActionBar actionBar;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_details;
    }

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel.class);
        observeViewModel();
    }

    void observeViewModel() {
        Observer<Number> numberObserver = new Observer<Number>() {
            @Override
            public void onChanged(Number number) {

                String stringNumber = String.valueOf(number.getNumber());
                numberTextView.setText(stringNumber);
                String rawFact = number.getFact();
                String fact = rawFact.replaceFirst(stringNumber + " ", "");
                factTextView.setText(fact);
            }
        };

        viewModel.getRequestedNumberLiveData().observe(getViewLifecycleOwner(), numberObserver);

        viewModel.getSelectedNumberLiveData().observe(getViewLifecycleOwner(), numberObserver);

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    numberTextView.setText("");
                    factTextView.setText("");
                    loadingView.setVisibility(View.VISIBLE);
                } else {
                    loadingView.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null)
                if (isError) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("An Error Occurred While Loading Data!");
                } else {
                    errorTextView.setVisibility(View.GONE);
                    errorTextView.setText(null);
                }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                actionBar.setDisplayHomeAsUpEnabled(false);
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
