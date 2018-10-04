package com.voidaint.intphonenumber;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import net.rimoto.intlphoneinput.R;

import java.util.Locale;

public class IntlPhoneInput extends RelativeLayout implements Callbacks {
    private static final String TAG = "IntlPhoneInput";
    private final String DEFAULT_COUNTRY = Locale.getDefault().getCountry();

    // UI Views
    private ImageView mCountryImageView;
    private EditText mPhoneEdit;

    //Adapters
    private PhoneNumberWatcher mPhoneNumberWatcher = new PhoneNumberWatcher(DEFAULT_COUNTRY);


    //Util
    private PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();

    // Fields
    private Country mSelectedCountry;
    private String defaultHint = null;
    private Utils.CountryList mCountries;
    private IntlPhoneInputListener mIntlPhoneInputListener;
    private Callbacks callbacks = this;
    private CountryDialog countryDialog;

    /**
     * Constructor
     *
     * @param context Context
     */
    public IntlPhoneInput(Context context) {
        super(context);
        init(null);
    }

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public IntlPhoneInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Init after constructor
     */
    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.intl_phone_input, this);

        /**
         * Country spinner
         */
        mCountryImageView = findViewById(R.id.intl_phone_edit__country);
        mCountries = Utils.getCountries(getContext());
        mPhoneEdit = findViewById(R.id.intl_phone_edit__phone);
        mPhoneEdit.addTextChangedListener(mPhoneNumberWatcher);

        initDialog(attrs);

        mCountryImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.showDialog();
            }
        });


        setFlagDefaults(attrs);
        setEditTextDefaults(attrs);
    }


    private void initDialog(AttributeSet attributeSet) {
        countryDialog = new CountryDialog(getContext())
                .setOnListClickListener(this)
                .setData(mCountries);


        TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.IntlPhoneInput);
        if (ta != null) {
            Drawable drawable;
            drawable = ta.getDrawable(R.styleable.IntlPhoneInput_closeIcon);
            if (drawable != null)
                countryDialog.setCloseIcon(drawable);

            drawable = ta.getDrawable(R.styleable.IntlPhoneInput_searchIcon);

            if (drawable != null)
                countryDialog.setSearchIcon(drawable);

            int resId;

            resId = ta.getColor(R.styleable.IntlPhoneInput_dialogTextcolor, -1);
            if (resId != -1)
                countryDialog.setDialogTextColor(resId);

            resId = ta.getColor(R.styleable.IntlPhoneInput_dialogHintTextcolor, -1);
            if (resId != -1)
                countryDialog.setDialogHintColor(resId);


            TypedValue textValue = new TypedValue();
            ta.getValue(R.styleable.IntlPhoneInput_dialogBackground, textValue);

            if (textValue.type == TypedValue.TYPE_STRING)
                countryDialog.setDialogBackgroundDrawable(textValue.resourceId);
            else if (textValue.type != TypedValue.TYPE_NULL)
                countryDialog.setDialogBackgroundColor(textValue.data);

            countryDialog.setRowDividerColor(ta.getColor(R.styleable.IntlPhoneInput_dialogRowDividerColor, 0));


            countryDialog.setRowDividerSize(
                    ta.getDimensionPixelSize(R.styleable.IntlPhoneInput_dialogRowDividerSize,
                            getContext().getResources().getDimensionPixelSize(R.dimen.dialog_row_default_divider)));

            resId = ta.getColor(R.styleable.IntlPhoneInput_dialogDividerColor, 0);
            if (resId != 0)
                countryDialog.setDividerColor(resId);

            resId = ta.getDimensionPixelSize(R.styleable.IntlPhoneInput_dialogDividerSize,
                    getContext().getResources().getDimensionPixelSize(R.dimen.dialog_default_divider));

            countryDialog.setDividerSize(resId);


            countryDialog.setRowTextSize(ta.getDimensionPixelSize(R.styleable.IntlPhoneInput_dialogRowTextSize, 0));

            resId = ta.getDimensionPixelSize(R.styleable.IntlPhoneInput_dialogTextSize, 0);
            if (resId != 0)
                countryDialog.setTextSize(resId);

            resId = ta.getDimensionPixelSize(R.styleable.IntlPhoneInput_dialogHeight, 0);
            if (resId != 0)
                countryDialog.setDialogHeight(resId);


            ta.getValue(R.styleable.IntlPhoneInput_dialogHintText, textValue);
            if (textValue.type == TypedValue.TYPE_STRING)
                countryDialog.setHintText(textValue.string);
            else if (textValue.type != TypedValue.TYPE_NULL)
                countryDialog.setHintText(textValue.resourceId);


            ta.recycle();
        }

    }

    @Override
    public void showDialog() {
        countryDialog.show();
    }

    @Override
    public void onCountrySelected(Country country) {

    }


    private void setFlagDefaults(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IntlPhoneInput);

        if (a == null)
            return;

        int paddingEnd = a.getDimensionPixelSize(R.styleable.IntlPhoneInput_flagPaddingEnd, getResources().getDimensionPixelSize(R.dimen.flag_default_padding_right));
        int paddingStart = a.getDimensionPixelSize(R.styleable.IntlPhoneInput_flagPaddingStart, getResources().getDimensionPixelSize(R.dimen.flag_default_padding));
        int paddingTop = a.getDimensionPixelSize(R.styleable.IntlPhoneInput_flagPaddingTop, getResources().getDimensionPixelSize(R.dimen.flag_default_padding));
        int paddingBottom = a.getDimensionPixelSize(R.styleable.IntlPhoneInput_flagPaddingBottom, getResources().getDimensionPixelSize(R.dimen.flag_default_padding));


        mCountryImageView.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom);
        a.recycle();
    }


    private void setEditTextDefaults(AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IntlPhoneInput);

        if (a == null)
            return;

        int textSize = a.getDimensionPixelSize(R.styleable.IntlPhoneInput_textSize, getResources().getDimensionPixelSize(R.dimen.text_size_default));
        mPhoneEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int color = a.getColor(R.styleable.IntlPhoneInput_textColor, -1);
        if (color != -1)
            mPhoneEdit.setTextColor(color);


        int hintColor = a.getColor(R.styleable.IntlPhoneInput_textColorHint, -1);
        if (hintColor != -1)
            mPhoneEdit.setHintTextColor(color);

        int bgColor = a.getColor(R.styleable.IntlPhoneInput_editTextBg, -1);
        if (bgColor != -1)
            mPhoneEdit.setBackgroundColor(bgColor);

        boolean cursorVisible = a.getBoolean(R.styleable.IntlPhoneInput_editTextCursorVisible, true);
        mPhoneEdit.setCursorVisible(cursorVisible);


        TypedValue textValue = new TypedValue();


        a.getValue(R.styleable.IntlPhoneInput_defaultHint, textValue);
        if (textValue.type == TypedValue.TYPE_STRING)
            defaultHint = textValue.string.toString();
        else if (textValue.type != TypedValue.TYPE_NULL)
            defaultHint = getContext().getString(textValue.resourceId);


        a.getValue(R.styleable.IntlPhoneInput_defaultIso, textValue);
        if (textValue.type == TypedValue.TYPE_STRING)
            setEmptyDefault(textValue.string.toString());
        else if (textValue.type != TypedValue.TYPE_NULL)
            setEmptyDefault(getContext().getString(textValue.resourceId).toUpperCase());
        else
            setEmptyDefault(null);


        a.recycle();
    }

    /**
     * Hide keyboard from phoneEdit field
     */
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mPhoneEdit.getWindowToken(), 0);
    }

    /**
     * Set default value
     * Will try to retrieve phone number from device
     */


    /**
     * Set default value with
     *
     * @param iso ISO2 of country
     */
    public void setEmptyDefault(String iso) {
        if (iso == null || iso.isEmpty()) {
            iso = DEFAULT_COUNTRY;
        }
        int defaultIdx = mCountries.indexOfIso(iso);
        setCurrentCountry(mCountries.get(defaultIdx));
    }

    /**
     * Alias for setting empty string of default settings from the device (using locale)
     */


    /**
     * Set hint number for country
     */
    private void setHint() {
        if (defaultHint != null) {
            mPhoneEdit.setHint(defaultHint);
            return;
        }

        if (mPhoneEdit != null && mSelectedCountry != null && mSelectedCountry.getIso2() != null) {
            Phonenumber.PhoneNumber phoneNumber = mPhoneUtil.getExampleNumberForType(mSelectedCountry.getIso2(), PhoneNumberUtil.PhoneNumberType.MOBILE);
            if (phoneNumber != null) {
                mPhoneEdit.setHint(mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
            }
        }
    }

    /**
     * Spinner listener
     */


    public void setCurrentCountry(Country country) {
        mSelectedCountry = country;
        //Make sure that the watcher is added into the listeners of the edittext
        //after updating the country selected...
        mPhoneEdit.removeTextChangedListener(mPhoneNumberWatcher);
        mPhoneNumberWatcher = new PhoneNumberWatcher(mSelectedCountry.getIso2());
        mPhoneEdit.addTextChangedListener(mPhoneNumberWatcher);
        mCountryImageView.setImageResource(Utils.getFlagResource(mSelectedCountry, getContext()));

        callbacks.onCountrySelected(country);

        setHint();
    }

    /**
     * Get number
     *
     * @return Phone number in E.164 format | null on error
     */
    @SuppressWarnings("unused")
    public String getNumber() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();

        if (phoneNumber == null) {
            return null;
        }

        return mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    /**
     * Set Number
     *
     * @param number E.164 format or national format
     */
    public void setNumber(String number) {
        try {
            String iso = null;
            if (mSelectedCountry != null)
                iso = mSelectedCountry.getIso2();

            Phonenumber.PhoneNumber phoneNumber = mPhoneUtil.parse(number, iso);

            int countryIdx = mCountries.indexOfIso(mPhoneUtil.getRegionCodeForNumber(phoneNumber));
            mSelectedCountry = mCountries.get(countryIdx);
            mCountryImageView.setImageResource(Utils.getFlagResource(mSelectedCountry, getContext()));


            mPhoneEdit.setText(mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException ignored) {
        }
    }

    public String getText() {
        return getNumber();
    }

    /**
     * Get PhoneNumber object
     *
     * @return PhonenUmber | null on error
     */
    @SuppressWarnings("unused")
    public Phonenumber.PhoneNumber getPhoneNumber() {
        try {
            String iso = null;
            if (mSelectedCountry != null) {
                iso = mSelectedCountry.getIso2();
            }
            return mPhoneUtil.parse(mPhoneEdit.getText().toString(), iso);
        } catch (NumberParseException ignored) {
            return null;
        }
    }

    /**
     * Get selected country
     *
     * @return Country
     */
    @SuppressWarnings("unused")
    public Country getSelectedCountry() {
        return mSelectedCountry;
    }


    /**
     * Get list of Countries
     *
     * @return CountryList
     */
    public Utils.CountryList getCountryList() {
        return mCountries;
    }

    /**
     * Check if number is valid
     *
     * @return boolean
     */
    @SuppressWarnings("unused")
    public boolean isValid() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        return phoneNumber != null && mPhoneUtil.isValidNumber(phoneNumber);
    }

    /**
     * Add validation listener
     *
     * @param listener IntlPhoneInputListener
     */
    public void setOnValidityChange(IntlPhoneInputListener listener) {
        mIntlPhoneInputListener = listener;
    }

    /**
     * Returns the error message that was set to be displayed with
     * {@link #setError}, or <code>null</code> if no error was set
     * or if it the error was cleared by the widget after user input.
     *
     * @return error message if known, null otherwise
     */
    @SuppressWarnings("unused")
    public CharSequence getError() {
        return mPhoneEdit.getError();
    }

    /**
     * Sets an error message that will be displayed in a popup when the EditText has focus.
     *
     * @param error error message to show
     */
    @SuppressWarnings("unused")
    public void setError(CharSequence error) {
        mPhoneEdit.setError(error);
    }

    /**
     * Sets an error message that will be displayed in a popup when the EditText has focus along
     * with an icon displayed at the right-hand side.
     *
     * @param error error message to show
     */
    @SuppressWarnings("unused")
    public void setError(CharSequence error, Drawable icon) {
        mPhoneEdit.setError(error, icon);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mPhoneEdit.setEnabled(enabled);
        mCountryImageView.setEnabled(enabled);
    }

    /**
     * Set keyboard done listener to detect when the user click "DONE" on his keyboard
     *
     * @param listener IntlPhoneInputListener
     */
    public void setOnKeyboardDone(final IntlPhoneInputListener listener) {
        mPhoneEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    listener.done(IntlPhoneInput.this, isValid());

                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Simple validation listener
     */
    public interface IntlPhoneInputListener {
        void done(View view, boolean isValid);
    }

    /**
     * Phone number watcher
     */
    private class PhoneNumberWatcher extends PhoneNumberFormattingTextWatcher {
        private boolean lastValidity;

        @SuppressWarnings("unused")
        public PhoneNumberWatcher() {
            super();
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public PhoneNumberWatcher(String countryCode) {
            super(countryCode);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
            Log.e(TAG, "onTextChanged: ");
            try {
                String iso = null;
                if (mSelectedCountry != null) {
                    iso = mSelectedCountry.getIso2();
                }
                Phonenumber.PhoneNumber phoneNumber = mPhoneUtil.parse(s.toString(), iso);
                iso = mPhoneUtil.getRegionCodeForNumber(phoneNumber);
                if (iso != null){
                    mCountryImageView.setImageResource(Utils.getFlagResource(mCountries.get(mCountries.indexOfIso(iso)), getContext()));
                    mSelectedCountry = mCountries.get(mCountries.indexOfIso(iso));
                }


            } catch (NumberParseException ignored) {
            }

            if (mIntlPhoneInputListener != null) {
                boolean validity = isValid();
                if (validity != lastValidity) {
                    mIntlPhoneInputListener.done(IntlPhoneInput.this, validity);
                }
                lastValidity = validity;
            }
        }
    }
}
