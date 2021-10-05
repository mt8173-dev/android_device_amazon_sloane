/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.settings.connectivity.setup;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tv.settings.R;

/**
 * Displays a UI for text input in the "wizard" style.
 * TODO: Merge with EditTextFragment
 */
public class PasswordInputWizardFragment extends Fragment {

    public interface Listener {
        /**
         * Called when text input is complete.
         *
         * @param text the text that was input.
         * @return true if the text is acceptable; false if not.
         */
        boolean onPasswordInputComplete(String text);
    }

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_DESCRIPTION = "description";
    private static final String EXTRA_PREFILL = "prefill";

    //TODO: Add a boolean parameter that controls whether the default is show or hide
    public static PasswordInputWizardFragment newInstance(
            String title, String description, String prefill) {
        PasswordInputWizardFragment fragment = new PasswordInputWizardFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TITLE, title);
        args.putString(EXTRA_DESCRIPTION, description);
        args.putString(EXTRA_PREFILL, prefill);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText mTextInput;
    private CheckBox mTextObsufactionToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        final View view = inflater.inflate(R.layout.account_content_area, container, false);

        final ViewGroup descriptionArea = (ViewGroup) view.findViewById(R.id.description);
        final View content = inflater.inflate(R.layout.wifi_content, descriptionArea, false);
        descriptionArea.addView(content);

        final ViewGroup actionArea = (ViewGroup) view.findViewById(R.id.action);
        final View action = inflater.inflate(R.layout.password_text_input, actionArea, false);
        actionArea.addView(action);

        TextView titleText = (TextView) content.findViewById(R.id.title_text);
        TextView descriptionText = (TextView) content.findViewById(R.id.description_text);
        mTextInput = (EditText) action.findViewById(R.id.text_input);
        mTextObsufactionToggle = (CheckBox) action.findViewById(R.id.text_obfuscation_toggle);

        final Bundle args = getArguments();
        final String title = args.getString(EXTRA_TITLE);
        final String description = args.getString(EXTRA_DESCRIPTION);
        final String prefill = args.getString(EXTRA_PREFILL);

        if (title != null) {
            titleText.setText(title);
            titleText.setVisibility(View.VISIBLE);
        } else {
            titleText.setVisibility(View.GONE);
        }

        if (description != null) {
            descriptionText.setText(description);
            descriptionText.setVisibility(View.VISIBLE);
        } else {
            descriptionText.setVisibility(View.GONE);
        }

        mTextObsufactionToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePasswordInputObfuscation();
            }
        });

        updatePasswordInputObfuscation();

        if (prefill != null) {
            mTextInput.setText(prefill);
            mTextInput.setSelection(mTextInput.getText().length(), mTextInput.getText().length());
        }

        mTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                    Activity a = getActivity();
                    if (a instanceof Listener) {
                        boolean inputValid =
                                ((Listener) a).onPasswordInputComplete(v.getText().toString());
                        if (inputValid) {
                            InputMethodManager imm = (InputMethodManager) getActivity()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mTextInput.getWindowToken(), 0);
                        }
                    }
                }
                return true;  // If we don't return true on ACTION_DOWN, we don't get the ACTION_UP.
            }
        });

        return view;
    }

    public void updatePasswordInputObfuscation() {
        mTextInput.setInputType(InputType.TYPE_CLASS_TEXT |
                (mTextObsufactionToggle.isChecked() ?
                        InputType.TYPE_TEXT_VARIATION_PASSWORD :
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
    }

}
