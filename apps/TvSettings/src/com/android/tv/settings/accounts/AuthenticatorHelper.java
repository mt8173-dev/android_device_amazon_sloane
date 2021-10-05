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

package com.android.tv.settings.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthenticatorHelper {

    private static final String TAG = "AuthenticatorHelper";
    private final Map<String, AuthenticatorDescription> mTypeToAuthDescription = new HashMap<>();
    private AuthenticatorDescription[] mAuthDescs;
    private final ArrayList<String> mEnabledAccountTypes = new ArrayList<>();
    private final Map<String, Drawable> mAccTypeIconCache = new HashMap<>();

    public AuthenticatorHelper() {
    }

    public String[] getEnabledAccountTypes() {
        return mEnabledAccountTypes.toArray(new String[mEnabledAccountTypes.size()]);
    }

    /**
     * Gets an icon associated with a particular account type. If none found, return null.
     * @param accountType the type of account
     * @return a drawable for the icon or null if one cannot be found.
     */
    public Drawable getDrawableForType(Context context, final String accountType) {
        Drawable icon = null;
        if (mAccTypeIconCache.containsKey(accountType)) {
            return mAccTypeIconCache.get(accountType);
        }
        if (mTypeToAuthDescription.containsKey(accountType)) {
            try {
                AuthenticatorDescription desc = mTypeToAuthDescription.get(accountType);
                Context authContext = context.createPackageContext(desc.packageName, 0);
                icon = authContext.getResources().getDrawable(desc.iconId);
                mAccTypeIconCache.put(accountType, icon);
            } catch (PackageManager.NameNotFoundException e) {
            } catch (Resources.NotFoundException e) {
            }
        }
        if (icon == null) {
            icon = context.getPackageManager().getDefaultActivityIcon();
        }
        return icon;
    }

    /**
     * Gets the label associated with a particular account type. If none found, return null.
     * @param accountType the type of account
     * @return a CharSequence for the label or null if one cannot be found.
     */
    public CharSequence getLabelForType(Context context, final String accountType) {
        CharSequence label = null;
        if (mTypeToAuthDescription.containsKey(accountType)) {
            try {
                AuthenticatorDescription desc = mTypeToAuthDescription.get(accountType);
                Context authContext = context.createPackageContext(desc.packageName, 0);
                label = authContext.getResources().getText(desc.labelId);
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, "No label name for account type " + accountType);
            } catch (Resources.NotFoundException e) {
                Log.w(TAG, "No label icon for account type " + accountType);
            }
        }
        return label;
    }

    /**
     * Updates provider icons. Subclasses should call this in onCreate()
     * and update any UI that depends on AuthenticatorDescriptions in onAuthDescriptionsUpdated().
     */
    public void updateAuthDescriptions(Context context) {
        mAuthDescs = AccountManager.get(context).getAuthenticatorTypes();
        for (int i = 0; i < mAuthDescs.length; i++) {
            mTypeToAuthDescription.put(mAuthDescs[i].type, mAuthDescs[i]);
        }
    }

    public void onAccountsUpdated(Context context, Account[] accounts) {
        if (accounts == null) {
            accounts = AccountManager.get(context).getAccounts();
        }
        mEnabledAccountTypes.clear();
        mAccTypeIconCache.clear();
        for (Account account: accounts) {
            if (!mEnabledAccountTypes.contains(account.type)) {
                mEnabledAccountTypes.add(account.type);
            }
        }
    }

    public boolean containsAccountType(String accountType) {
        return mTypeToAuthDescription.containsKey(accountType);
    }

    public AuthenticatorDescription getAccountTypeDescription(String accountType) {
        return mTypeToAuthDescription.get(accountType);
    }

    public boolean hasAccountPreferences(final String accountType) {
        if (containsAccountType(accountType)) {
            AuthenticatorDescription desc = getAccountTypeDescription(accountType);
            if (desc != null && desc.accountPreferencesId != 0) {
                return true;
            }
        }
        return false;
    }
}
