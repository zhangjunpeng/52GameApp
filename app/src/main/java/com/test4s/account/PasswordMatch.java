package com.test4s.account;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/25.
 */
public class PasswordMatch {
    public static boolean passwordMatch(String password){
        String regEx_1="[^a-zA-Z0-9]";

        String regEx_abc="[^a-zA-Z]";
        String regEx_num="[^0-9]";


        boolean result= Pattern.compile(regEx_1).matcher(password).find();
        boolean result_num= Pattern.compile(regEx_abc).matcher(password).find();
        boolean result_abc= Pattern.compile(regEx_num).matcher(password).find();
        return (!result)&&result_abc&&result_num;
    }
}
