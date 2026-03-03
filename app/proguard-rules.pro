# Add project specific ProGuard rules here.

# ---- kotlinx.serialization ----
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.speze88.namenstag.**$$serializer { *; }
-keepclassmembers class com.speze88.namenstag.** {
    *** Companion;
}
-keepclasseswithmembers class com.speze88.namenstag.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ---- Hilt / Dagger ----
-dontwarn com.google.errorprone.annotations.**

# ---- WorkManager ----
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}

# ---- Coroutines ----
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ---- Keep data models used in JSON seed ----
-keep class com.speze88.namenstag.data.seed.** { *; }
