package com.example.mysynccontactapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mysynccontactapp.databinding.FragmentLoginBinding;
import com.example.mysynccontactapp.retrofit.RetrofitConfig;
import com.example.mysynccontactapp.retrofit.req.RegisterReqBody;
import com.example.mysynccontactapp.retrofit.res.RegisterUserResBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
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
        final ActivityResultLauncher<String> requestPermissionLauncher =
                requireActivity().registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {

                        } else {
                            Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_CONTACTS);
        }

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
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
                        RegisterReqBody registerReqBody = new RegisterReqBody(binding.phone.getEditText().getText().toString());
                        performLoginRequest(registerReqBody,v);

                    }
                } else {
                    requestPermissionLauncher.launch(
                            Manifest.permission.READ_CONTACTS);
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

    private void performLoginRequest(RegisterReqBody registerReqBody, final View view) {

        Call<RegisterUserResBody> call = RetrofitConfig.getService().registerUser(registerReqBody);

        call.enqueue(new Callback<RegisterUserResBody>() {
            @Override
            public void onResponse(Call<RegisterUserResBody> call, Response<RegisterUserResBody> response) {
                Log.d(TAG, "onResponse: " + response);
                Log.d(TAG, "onResponse: " + call.request());

                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: " + response.body());
                    binding.phone.setError("");
                    Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
