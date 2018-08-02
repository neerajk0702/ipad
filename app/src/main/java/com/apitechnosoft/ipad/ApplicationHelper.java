package com.apitechnosoft.ipad;

import com.google.gson.internal.Primitives;

/**
 * @author AST Inc.
 */
public class ApplicationHelper {

    private static ApplicationClass applicationObj;

    public static ApplicationClass application() {
        return applicationObj;
    }

    public static <T> T application(Class<T> entity) {
        return Primitives.wrap(entity).cast(application());
    }

    public static void setApplicationObj(ApplicationClass applicationObj) {
        ApplicationHelper.applicationObj = applicationObj;
    }

}
