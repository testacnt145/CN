package com.chattynotes.util.androidos;

import android.support.v4.content.FileProvider;

//Android Nougat File Provider error
//https://stackoverflow.com/a/38858040
//https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
public final class GenericFileProvider extends FileProvider {

    //[package_name_: requirement]
    public final static String PATH = ".util.androidos.GenericFileProvider";
}
