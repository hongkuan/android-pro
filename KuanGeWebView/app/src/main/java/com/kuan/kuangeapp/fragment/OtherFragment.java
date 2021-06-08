package com.kuan.kuangeapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kuan.kuangeapp.R;
import com.kuan.kuangeapp.databinding.FragmentOtherBinding;

/**
 * Created by hongkuan on 2021-06-08 0008.
 */
public class OtherFragment extends Fragment {
    public static final String ARGS_KEY_TITLE = "title";

    private String title;

    public static OtherFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARGS_KEY_TITLE, title);
        OtherFragment fragment = new OtherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        FragmentOtherBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_other, null, false);
        binding.tvTitle.setText(title);
        return binding.getRoot();
    }

    private void initData(){
        Bundle args = getArguments();
        title = args.getString(ARGS_KEY_TITLE);
    }
}
