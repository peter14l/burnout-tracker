# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Room entities
-keep class com.burnouttracker.data.local.** { *; }

# Keep domain models
-keep class com.burnouttracker.domain.model.** { *; }

# Keep Hilt classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# Keep Compose
-keep class androidx.compose.** { *; }

# General Android
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
