package com.chattynotes.customviews.photoview.scrollerproxy;

import android.annotation.TargetApi;
import android.content.Context;

@TargetApi(14)
class IcsScroller extends GingerScroller {

    IcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}
