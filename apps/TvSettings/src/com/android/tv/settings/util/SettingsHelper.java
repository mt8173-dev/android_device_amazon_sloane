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

package com.android.tv.settings.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

import com.android.tv.settings.R;

public class SettingsHelper {

    private static final String TAG = "SettingsHelper";
    private static final boolean DEBUG = false;

    private final Resources mResources;

    public SettingsHelper(Context context) {
        mResources = context.getResources();
    }

    public static void setSystemProperties(String setting, String value) {
        SystemProperties.set(setting, value);
        pokeSystemProperties();
    }

    public static void setSystemProperties(String setting, boolean value) {
        setSystemProperties(setting, Boolean.toString(value));
    }

    public static String getSystemProperties(String setting) {
        return SystemProperties.get(setting);
    }

    private static void pokeSystemProperties() {
        (new SystemPropPoker()).execute();
    }

    static class SystemPropPoker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String[] services;
            try {
                services = ServiceManager.listServices();
            } catch (RemoteException e) {
                return null;
            }
            for (String service : services) {
                IBinder obj = ServiceManager.checkService(service);
                if (obj != null) {
                    Parcel data = Parcel.obtain();
                    try {
                        obj.transact(IBinder.SYSPROPS_TRANSACTION, data, null, 0);
                    } catch (RemoteException e) {
                        if (DEBUG) {
                            Log.d(TAG, "SystemPropPoker", e);
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "Somone wrote a bad service '" + service
                                + "' that doesn't like to be poked: " + e);
                    }
                    data.recycle();
                }
            }
            return null;
        }
    }

    public String getStatusStringFromBoolean(boolean status) {
        int descResId = status ? R.string.action_on_description : R.string.action_off_description;
        return mResources.getString(descResId);
    }
}
