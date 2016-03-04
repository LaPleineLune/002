package com.android.linglan.utils;

import android.content.Context;

import com.android.linglan.ui.R;
import com.roc.actionsheet.ActionSheet;

/**
 * Created by LeeMy on 2016/1/8 0008.
 */
public class ActionSheetUtil {
    private static ActionSheet actionSheet;

    public static void show(Context context, ActionSheet.MenuItemClickListener menuItemClickListener, String... titles) {
        dismiss();

        actionSheet = new ActionSheet(context);
        actionSheet.setItemClickListener(menuItemClickListener);
        actionSheet.setCancelableOnTouchMenuOutside(true);
        actionSheet.setCancelButtonTitle(R.string.cancel);
        actionSheet.addItems(titles);
        actionSheet.showMenu();
    }

    public static void dismiss() {
        if (actionSheet != null) {
            actionSheet.dismissMenu();
            actionSheet = null;
        }
    }
}
