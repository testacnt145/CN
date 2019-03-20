# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#________________________________________ ANDROID LIBRARIES
#shanraisshan answer - https://stackoverflow.com/a/43111580
#http://stackoverflow.com/a/41901653/4754141
#http://stackoverflow.com/a/23840049/4754141
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
#________________________________________ 3RD PARTY
#JodaTime
#@link https://stackoverflow.com/questions/14025487/proguard-didnt-compile-with-joda-time-used-in-windows
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

#crashlytics
#@link https://docs.fabric.io/android/crashlytics/dex-and-proguard.html
-keepattributes SourceFile,LineNumberTable,*Annotation*
#@link http://stackoverflow.com/a/34751033
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep class com.crashlytics.android.**

#retrofit
#https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-retrofit2.pro
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepattributes Signature
-keepattributes Exceptions

#picasso
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
