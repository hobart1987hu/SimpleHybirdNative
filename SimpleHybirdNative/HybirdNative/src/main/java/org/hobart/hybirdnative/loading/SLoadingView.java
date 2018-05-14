package org.hobart.hybirdnative.loading;

import android.view.View;

/**
 * Created by huzeyin on 2018/5/14.
 */

public interface SLoadingView {
    /**
     * 显示loading
     */
    void show();

    /**
     * 关闭loading
     */
    void hide();

    /**
     * 获取loading
     *
     * @return
     */
    View getView();
}
