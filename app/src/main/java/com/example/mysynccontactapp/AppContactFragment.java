package com.example.mysynccontactapp;

import android.accounts.Account;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mysynccontactapp.adapter.ContactsCursorAdapter;
import com.example.mysynccontactapp.databinding.FragmentAppContactBinding;

public class AppContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private FragmentAppContactBinding binding;
    private ContactsCursorAdapter cursorAdapter;
    Account account;

    AppContactFragment(Account account){
        this.account = account;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_contact, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cursorAdapter = new ContactsCursorAdapter();
        binding.contactsList.setAdapter(cursorAdapter);
        binding.contactsList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        LoaderManager.getInstance(this).initLoader(0,null,this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri currentContactsUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
                .appendQueryParameter(ContactsContract.RawContacts.ACCOUNT_NAME, account.name)
                .appendQueryParameter(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type).build();
        String[] projection = new String[] { ContactsContract.RawContacts.SYNC1,ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};

        return new CursorLoader(requireActivity(),currentContactsUri,projection,null,null,ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
