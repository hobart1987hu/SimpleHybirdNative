package org.hobart.hybirdnative.loading.impl;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.hobart.hybirdnative.loading.SLoadingView;

/**
 * Created by huzeyin on 2018/5/15.
 */

public class DefaultLoadingView extends LinearLayout implements SLoadingView {

    public DefaultLoadingView(Context context) {
        super(context);
        //View.inflate(context, R.layout.widget_loading_view, this);
    }

    @Override
    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        this.setVisibility(View.GONE);
    }

    @Override
    public View getView() {
        return this;
    }
}
