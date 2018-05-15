package com.simple.hybirdnative.loading;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.simple.hybirdnative.R;

import org.hobart.hybirdnative.loading.SLoadingView;

/**
 * Created by huzeyin on 2018/5/14.
 */

public class CustomLoadingView extends LinearLayout implements SLoadingView {

    public CustomLoadingView(Context context) {
        super(context);
        View.inflate(context, R.layout.widget_loading_view, this);
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
