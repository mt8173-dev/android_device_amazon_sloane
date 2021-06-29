package com.android.tv.settings.system;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.tv.settings.ActionBehavior;
import com.android.tv.settings.ActionKey;
import com.android.tv.settings.BaseSettingsActivity;
import com.android.tv.settings.R;
import com.android.tv.settings.dialog.old.Action;
import com.android.tv.settings.dialog.old.ActionAdapter;

public class HomeActivity extends BaseSettingsActivity implements ActionAdapter.Listener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final boolean DEBUG = false;

    private PackageManager mPm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPm = getPackageManager();
    }

    protected Object getInitialState() {
        return ActionType.HOME_OVERVIEW;
    }

    @Override
    public void onActionClicked(Action action) {
        /*
         * For regular states
         */
        ActionKey<ActionType, ActionBehavior> actionKey = new ActionKey<ActionType, ActionBehavior>(
                ActionType.class, ActionBehavior.class, action.getKey());
        final ActionType type = actionKey.getType();
        switch (type) {
            case HOME_RESET_DEFAULT:
                warnLauncherReset();
                return;
            case HOME_SETTINGS:
                Intent intent = new Intent("com.google.android.leanbacklauncher.SETTINGS");
                startActivity(intent);
                return;
            default:
                break;
        }
    }

    @Override
    protected void updateView() {
        refreshActionList();
        switch ((ActionType) mState) {
            case HOME_OVERVIEW:
                setView(R.string.system_home, R.string.settings_app_name, 0, R.drawable.ic_settings_home);
                break;
            case HOME_RESET_DEFAULT:
                setView(R.string.system_reset_default_home, R.string.system_home, 0, 0);
                break;
            case HOME_SETTINGS:
                setView(R.string.system_home_settings, R.string.system_home, 0, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void refreshActionList() {
        mActions.clear();
        switch ((ActionType) mState) {
            case HOME_OVERVIEW:
                mActions.add(ActionType.HOME_RESET_DEFAULT.toAction(mResources));
                mActions.add(ActionType.HOME_SETTINGS.toAction(mResources));
                break;
            default:
                break;
        }
    }

    @Override
    protected void setProperty(boolean enable) {

    }

    private void warnLauncherReset() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.reset_launcher_title)
                .setMessage(R.string.reset_launcher_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User wants to reset the launcher
                        mPm.clearPackagePreferredActivities("com.google.android.leanbacklauncher");
                        Toast.makeText(HomeActivity.this, R.string.reset_launcher_tip, Toast.LENGTH_LONG).show();
                        goBack();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}

