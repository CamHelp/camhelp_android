package com.camhelp.common;


/**
 * Created by storm on 2017-08-09.
 */

public class FindValueForID {

    /**
     * @param type
     * @return
     * 根据类型的id得到类型的名称
     */
    public String findCategoryType(int type) {
        String sCategoryType = "";
        if (type == CommonGlobal.categoryType_experience) {
            sCategoryType = CommonGlobal.sCategoryType_experience;
        }else if (type == CommonGlobal.categoryType_problem) {
            sCategoryType = CommonGlobal.sCategoryType_problem;
        }else if (type == CommonGlobal.categoryType_lose) {
            sCategoryType = CommonGlobal.sCategoryType_lose;
        }else if (type == CommonGlobal.categoryType_pickup) {
            sCategoryType = CommonGlobal.sCategoryType_pickup;
        }
        return sCategoryType;
    }
}
