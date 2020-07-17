package com.example.mysynccontactapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mysynccontactapp.databinding.FragmentMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    public static final String AUTHORITY = ContactsContract.AUTHORITY;
    public static final String ACCOUNT_TYPE = "com.example.mysynccontactapp";
    public static final String ACCOUNT = "MySyncContactApp";


    Account mAccount;

    private FragmentMainBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabFont();
        mAccount = getOrCreateAccount(getContext());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, new SystemContactFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainer, new SystemContactFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    case 1:
                        FragmentTransaction transaction1 = getChildFragmentManager().beginTransaction();
                        transaction1.replace(R.id.fragmentContainer, new AppContactFragment(mAccount));
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private static Account getOrCreateAccount(Context context) {

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        Account account;
        if (accounts.length == 0) {
            account = new Account(context.getString(R.string.app_name), ACCOUNT_TYPE);

            if (accountManager.addAccountExplicitly(account, null, null)){
                Log.d(TAG, "Created new account...");
                ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
            }

            else {
                Log.d(TAG, "Failed to create account!");
            }
        }

        else{
            Log.d(TAG, "Account (" + accounts[0] + ") Already Exist! ");
            account = accounts[0];
        }

        if (account != null && !ContentResolver.getSyncAutomatically(account, ContactsContract.AUTHORITY)) {
            Log.d(TAG, "ContentResolver:setSyncAutomatically");
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        }

        return account;
    }


    private void setTabFont() {
        Typeface fontTypeFace = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/IRANSansMobile.ttf");

        ViewGroup vg = (ViewGroup) binding.tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(fontTypeFace, Typeface.NORMAL);
                }
            }
        }
    }
}
