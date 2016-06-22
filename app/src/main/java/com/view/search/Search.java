package com.view.search;

import com.test4s.net.BaseParams;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Search {
     public static void search(String keyword, String identity_cat, Callback.CommonCallback<String> callback) {
        BaseParams baseParams=new BaseParams("search/dosearch");
        baseParams.addParams("keyword",keyword);
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), callback);
    }
}
