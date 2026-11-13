plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.google.ai.edge.gallery.stablediffusion"
    compileSdk = 36
    ndkVersion = "28.2.13676358"

    defaultConfig {
        ndk { abiFilters += "arm64-v8a" }
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags += listOf("-std=c++17")
                arguments += listOf(
                    "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON",
                    "-DCMAKE_BUILD_TYPE=Release",
                    "-DCMAKE_C_FLAGS=-O3 -march=armv9.2-a+fp16+dotprod+i8mm",
                    "-DCMAKE_CXX_FLAGS=-O3 -march=armv9.2-a+fp16+dotprod+i8mm",
                    "-DSD_BUILD_EXAMPLES=OFF",
                    "-DSD_BUILD_SHARED_LIBS=OFF",
                    "-DSD_BUILD_SHARED_GGML_LIB=OFF",
                    "-DSD_WEBP=OFF",
                    "-DSD_WEBM=OFF",
                    "-DSD_CUDA=OFF",
                    "-DSD_METAL=OFF",
                    "-DSD_VULKAN=ON",
                    "-DGGML_VULKAN=ON",
                    "-DGGML_BUILD_TESTS=OFF",
                    "-DGGML_BUILD_EXAMPLES=OFF",
                    "-DBUILD_SHARED_LIBS=OFF",
                )
                System.getenv("VULKAN_HEADERS_DIR")?.let { arguments += "-DVulkan_INCLUDE_DIR=$it" }
                System.getenv("SPIRV_HEADERS_DIR")?.let { arguments += "-DCMAKE_CXX_FLAGS=-O3 -march=armv9.2-a+fp16+dotprod+i8mm -I$it" }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
}
