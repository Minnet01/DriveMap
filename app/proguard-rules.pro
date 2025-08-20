# Retrofit / OkHttp (umum)
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }


# kotlinx-serialization
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class ** { *; }


# MapLibre (umum)
-keep class org.maplibre.** { *; }
-dontwarn org.maplibre.**