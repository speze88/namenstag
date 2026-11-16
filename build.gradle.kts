// Patch vulnerable transitive libraries used by the Android build plugins.
// Keep these constraints until upstream plugins depend on patched versions.
buildscript {
    dependencies {
        constraints {
            classpath("com.google.guava:guava:33.8.0-jre")
            classpath("org.apache.commons:commons-lang3:3.22.0")
            classpath("org.bitbucket.b_c:jose4j:0.9.7")
            classpath("org.bouncycastle:bcpkix-jdk18on:1.87")
            classpath("org.bouncycastle:bcprov-jdk18on:1.87")
            classpath("org.bouncycastle:bcutil-jdk18on:1.87")
            classpath("org.jdom:jdom2:2.0.6.1")
        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
