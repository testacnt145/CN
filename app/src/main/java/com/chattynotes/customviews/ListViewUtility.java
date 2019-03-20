package com.chattynotes.customviews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtility {
    //http://stackoverflow.com/a/35552988/4754141

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            //apply dynamic for less than 3 rows, otherwise fixed 3 rows height
            int loop = listAdapter.getCount();
            int MAX_LIST_ITEMS = 3;
            if(listAdapter.getCount()>= MAX_LIST_ITEMS)
               loop = MAX_LIST_ITEMS;

            //Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos<loop; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            //Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (loop-1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            //Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //LogUtil.e("ListViewUtility", params.height + "<--");
        }

    }
}
