package com.chattynotes.constant.keyboard;

public enum Keyboard {

    //[release_: (1) check whether the right server(im)]
    baseUrl("http://www.shanraisshan.com/"),
    //baseUrl_api(baseUrl.getValue() + "/apps/cn/api/"),
    baseUrl_release_notes   (baseUrl.getValue() + "apps/cn/html/rn.html"),
    baseUrl_privacy_policy  (baseUrl.getValue() + "apps/cn/html/privacy.html"),
    baseUrl_how_to_use      (baseUrl.getValue() + "apps/cn/html/howtouse.html"),

    //[email_: chattynotes@gmail.com]
    FOURSQUARE_CLIENT_ID("BEMF355RBN5U3R5XALWQ54JT2PC4IBQOPZ0H3URZ2CQRKZBA"),
    FOURSQUARE_CLIENT_SECRET("MHVSXHT45U1BZP4SBNYNTFY0JRARHCMDOQU0YV00M5VE3EVP")
    ;

    private String value;
    Keyboard(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
