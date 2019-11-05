# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep public class com.verygoodsecurity.vgscollect.widget.VGSTextInputLauout
-keep public class * com.verygoodsecurity.vgscollect.widget.VGSEditText {
      public <init>(android.content.Context);
      public <init>(android.content.Context, android.util.AttributeSet);
      public <init>(android.content.Context, android.util.AttributeSet, int);
      public void set*(...);
}

-keep public class com.verygoodsecurity.vgscollect.view.text.validation.card.VGSTextInputType

-keep public class com.verygoodsecurity.vgscollect.core.VGSCollect
-keep public class com.verygoodsecurity.vgscollect.core.Environment
