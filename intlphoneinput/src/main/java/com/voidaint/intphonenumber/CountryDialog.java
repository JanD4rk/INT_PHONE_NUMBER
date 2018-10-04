package com.voidaint.intphonenumber;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import net.rimoto.intlphoneinput.R;

import java.util.List;

public class CountryDialog extends Dialog implements SearchView.OnQueryTextListener {
    private ConstraintLayout constraintLayout;
    private ImageView mSearchButton;
    private ImageView mCloseButton;
    private EditText editText;
    private View divider;
    private RecyclerView recyclerView;
    private CountryCodesAdapter adapter;

    public CountryDialog(Context context) {
        super(context);
        adapter = new CountryCodesAdapter();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.country_dialog);

        constraintLayout = findViewById(R.id.main_layout);
        SearchView searchView = findViewById(R.id.searchView2);
        mSearchButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        mCloseButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        divider = findViewById(R.id.divider);
        recyclerView = findViewById(R.id.country_holder);

        editText.setTextColor(Utils.getColor(context,R.color.dialog_edittext_text));
        editText.setHintTextColor(Utils.getColor(context,R.color.dialog_edittext_text));



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

//
        searchView.setOnQueryTextListener(this);

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
    }

    CountryDialog setData(List<Country> countryList) {
        adapter.setData(countryList);
    return this;
    }

    CountryDialog setOnListClickListener(final IntlPhoneInput a) {
        adapter.setListener(new OnCountrySelectionListener() {
            @Override
            public void onCountryListener(Country country) {
                a.setCurrentCountry(country);
                adapter.filterData("");
                dismiss();
            }
        });
        return this;
    }



    CountryDialog setSearchIcon(Drawable searchIcon) {
        mSearchButton.setImageDrawable(searchIcon);
        return this;
    }


    CountryDialog setCloseIcon(Drawable closeIcon) {
        mCloseButton.setImageDrawable(closeIcon);
        return this;
    }

    CountryDialog setDialogTextColor(int color) {
        editText.setTextColor(color);
        return this;
    }

    CountryDialog setDialogHintColor(int color) {
        editText.setHintTextColor(color);
        return this;
    }

    CountryDialog setDialogBackgroundDrawable(int drawId) {
        constraintLayout.setBackgroundResource(drawId);
        return this;
    }

    CountryDialog setDialogBackgroundColor(int color) {
        constraintLayout.setBackgroundColor( color);
        return this;
    }

    CountryDialog setDividerColor(int color) {
        divider.setBackgroundColor(Utils.getColor(getContext(), color));
        return this;
    }

    CountryDialog setRowDividerColor(int color) {
        adapter.setDividerColor(color);
        return this;
    }

    CountryDialog setRowDividerSize(int size) {
        adapter.setDividerSize(size);
        return this;
    }

    CountryDialog setDividerSize(int size) {
        divider.getLayoutParams().height = size;
        return this;
    }

    CountryDialog setRowTextSize(int size) {
        adapter.setTextSize(size);
        return this;
    }

    CountryDialog setDialogHeight(int size) {
        constraintLayout.getLayoutParams().height = size;
        return this;
    }

    CountryDialog setTextSize(int size) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    CountryDialog setHintText(int stringId) {
        editText.setHint(stringId);
        return this;
    }

    CountryDialog setHintText(CharSequence string) {
        editText.setHint(string);
        return this;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filterData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filterData(newText);
        return false;
    }

    public interface OnCountrySelectionListener {
        void onCountryListener(Country country);
    }


}