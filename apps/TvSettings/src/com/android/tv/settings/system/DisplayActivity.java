package com.android.tv.settings.system;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.tv.settings.ActionBehavior;
import com.android.tv.settings.ActionKey;
import com.android.tv.settings.BaseSettingsActivity;
import com.android.tv.settings.R;
import com.android.tv.settings.dialog.old.Action;
import com.android.tv.settings.dialog.old.ActionAdapter;

public class DisplayActivity extends BaseSettingsActivity implements ActionAdapter.Listener {

    private static final String TAG = DisplayActivity.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final String OVERSCAN_APP_PACKAGE = "com.google.android.tungsten.overscan";
    private static final ComponentName mOverscanActivity =
            new ComponentName("com.google.android.tungsten.overscan",
                    "com.google.android.tungsten.overscan.CalibratorActivity");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Object getInitialState() {
        return ActionType.DISPLAY_OVERVIEW;
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
            case DISPLAY_OVERSCAN:
                Intent intent = new Intent();
                intent.setComponent(mOverscanActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            case DISPLAY_OVERVIEW:
                setView(R.string.system_display, R.string.settings_app_name, 0,
                        R.drawable.ic_settings_display);
                break;
            case DISPLAY_OVERSCAN:
                setView(R.string.system_display_overscan, R.string.system_display, 0, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void refreshActionList() {
        mActions.clear();
        switch ((ActionType) mState) {
            case DISPLAY_OVERVIEW:
                if (DeveloperOptionsActivity.isPackageInstalled(this, OVERSCAN_APP_PACKAGE)) {
                    mActions.add(ActionType.DISPLAY_OVERSCAN.toAction(mResources));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void setProperty(boolean enable) {

    }
}

