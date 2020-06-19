package com.example.mysynccontactapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mysynccontactapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phone.getEditText().getText().toString();

                if (phone.isEmpty()) {
                    binding.phone.setError("شماره موبایل خالی است.");
                } else if (phone.length() > 11) {
                    binding.phone.setError("شماره موبایل نمی توانید بیشتر از 11 رقم باشد");
                } else if (phone.length() < 11) {
                    binding.phone.setError("شماره موبایل نمی توانید کمتر از 11 رقم باشد");
                } else if (!phone.startsWith("09")) {
                    binding.phone.setError("شماره موبایل باید با 09 شروع شود");
                } else {
                    binding.phone.setError("");
                    Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
                }
            }
        });

        binding.phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.phone.setError("");
                } else {
                    if (s.toString().startsWith("09")) {
                        binding.phone.setError("");
                    } else if (!s.toString().startsWith("0")) {
                        binding.phone.setError("شماره موبایل باید با 09 شروع شود.");
                    } else if (s.length() > 1 && s.charAt(0) == '0' && s.charAt(1) != '9') {
                        binding.phone.setError("شماره موبایل باید با 09 شروع شود.");
                    } else if (s.length() == 11 && s.toString().startsWith("09")) {
                        binding.phone.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
