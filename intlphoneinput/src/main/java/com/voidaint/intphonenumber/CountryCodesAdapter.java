package com.voidaint.intphonenumber;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 8/1/2017.
 */

public class CountryCodesAdapter extends RecyclerView.Adapter<CountryCodesAdapter.ViewHolder> {

    private List<Country> countries;
    private List<Country> childList;
    private CountryDialog.OnCountrySelectionListener listener;
    @ColorInt
    private int textColor = 0;
    @ColorInt
    private int dividerColor = 0;
    private int textSize=0;
    private int dividerSize=0;


    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setDividerSize(int dividerSize) {
        this.dividerSize = dividerSize;
    }

    public void setListener(CountryDialog.OnCountrySelectionListener listener) {
        this.listener = listener;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    public void setDividerColor(@ColorInt int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public void setData(List<Country> list) {
        this.countries = list;
        childList = new ArrayList<>();
        childList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.intp_item_country, parent, false);
        return new ViewHolder(convertView, textColor,dividerColor,dividerSize,textSize);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Country country = childList.get(position);
        viewHolder.mImageView.setImageResource(Utils.getFlagResource(country, viewHolder.itemView.getContext()));
        viewHolder.mNameView.setText(country.getName());
        viewHolder.mDialCode.setText(String.format("+%s", country.getDialCode()));

        if (listener != null)
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCountryListener(country);
                }
            });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (childList == null)
            return 0;
        return childList.size();
    }


    public void filterData(String query) {
        query = query.toLowerCase();
        childList.clear();

        if (query.isEmpty()) {
            childList.addAll(countries);
        } else {

            for (Country country : countries) {

                if (country.getName().toLowerCase().contains(query))
                    childList.add(country);


            }
        }

        notifyDataSetChanged();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mNameView;
        TextView mDialCode;
        TextView divider;

        ViewHolder(View convertView, @ColorInt int textColor, @ColorInt int dividerColor,int dividerSize,int textSize) {
            super(convertView);
            mImageView = convertView.findViewById(R.id.intp_row_country_iv);
            mNameView = convertView.findViewById(R.id.intp_row_country_name);
            mDialCode = convertView.findViewById(R.id.intp_row_country_dialcode);
            divider = convertView.findViewById(R.id.intp_row_country_divider);

            if (textColor != 0) {
                mNameView.setTextColor(textColor);
                mDialCode.setTextColor(textColor);
            }
            if (dividerColor != 0)
                divider.setBackgroundColor(dividerColor);

            if (dividerSize!=0)
                divider.getLayoutParams().height=dividerSize;


            if (textSize!=0){
                mNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                mDialCode.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            }


        }
    }
}
