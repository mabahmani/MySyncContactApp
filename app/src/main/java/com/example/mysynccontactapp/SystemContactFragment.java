package com.example.mysynccontactapp;

import android.database.Cursor;
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
import com.example.mysynccontactapp.databinding.FragmentSystemContactBinding;

public class SystemContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private FragmentSystemContactBinding binding;
    private ContactsCursorAdapter cursorAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_system_contact, container, false);
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
        final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        };
        return new CursorLoader(requireActivity(),ContactsContract.CommonDataKinds.Phone.CONTENT_URI,CONTACTS_SUMMARY_PROJECTION,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
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
