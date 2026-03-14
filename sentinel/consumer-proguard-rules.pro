-keep public class sentinel.** { public *; }

-keep class kotlin.Metadata { *; }

-keepattributes *Annotation*

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}