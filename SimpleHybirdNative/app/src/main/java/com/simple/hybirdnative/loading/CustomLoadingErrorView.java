package com.simple.hybirdnative.loading;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.simple.hybirdnative.R;

import org.hobart.hybirdnative.loading.SLoadingErrorView;

/**
 * Created by huzeyin on 2018/5/14.
 */

public class CustomLoadingErrorView extends LinearLayout implements SLoadingErrorView {

    public CustomLoadingErrorView(Context context) {
        super(context);
        View.inflate(context, R.layout.widget_loading_error_view, this);
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
