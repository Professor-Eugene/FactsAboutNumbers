package com.example.factsaboutnumbers.ui.list;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factsaboutnumbers.L;
import com.example.factsaboutnumbers.R;
import com.example.factsaboutnumbers.base.BaseFragment;
import com.example.factsaboutnumbers.model.Number;
import com.example.factsaboutnumbers.ui.details.DetailsFragment;
import com.example.factsaboutnumbers.ui.details.DetailsViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ListFragment extends BaseFragment implements NumberSelectedListener {

    @BindView(R.id.recyclerView)
    RecyclerView listView;

    @BindView(R.id.loading_view)
    View loadingView;

    @BindView(R.id.tv_error)
    TextView errorTextView;

    @BindView(R.id.et_number)
    EditText editText;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ListViewModel viewModel;

    NumbersListAdapter numbersListAdapter;

    //    private LiveData<List<Number>> numbersLiveData;
    @Override
    protected int layoutRes() {
        return R.layout.fragment_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ListViewModel.class);

        numbersListAdapter = new NumbersListAdapter(getViewLifecycleOwner()
                , viewModel.getNumbersLiveData(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if (numbersListAdapter.getItemCount() != 0) {
                    listView.post(() -> listView.smoothScrollToPosition(0));
                }
            }
        };

        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(numbersListAdapter);

        editText.setSaveEnabled(false);
        editText.setOnKeyListener(onEnterKeyListener);

        observeViewModel();
    }

    void observeViewModel() {
        viewModel.getUpdateListLiveData().observe(getViewLifecycleOwner(), numbersLiveData ->
                numbersListAdapter.observe(numbersLiveData));
    }

    @Override
    public void onNumberSelected(Number number) {
        DetailsViewModel detailsViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel.class);
        detailsViewModel.getSelectedFromList(number);
        initDetailsFragment();
    }

    @OnClick(R.id.btn_get_fact)
    public void requestNewFact() {
        String et = editText.getText().toString();
        if (!TextUtils.isEmpty(et)) {
            int numberRequested = Integer.parseInt(et);
            makeAPIRequest(numberRequested);
        }
    }

    @OnClick(R.id.btn_get_random_fact)
    public void requestNewRandomFact() {
        int numberRequested = (int) (Math.random() * 501);
        makeAPIRequest(numberRequested);
    }

    void makeAPIRequest(int intNumberRequested) {
        DetailsViewModel detailsViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel.class);
        detailsViewModel.requestNewFact(intNumberRequested);
        initDetailsFragment();
    }

    private void initDetailsFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    View.OnKeyListener onEnterKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == 0) {
                String et = editText.getText().toString();
                if (!TextUtils.isEmpty(et))
                    requestNewFact();
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sort_order: {
                viewModel.toggleSortOrder();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
