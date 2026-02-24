#include <jni.h>
#include <string>
#include <fstream>
#include <unistd.h>
#include <android/log.h>

#include <jni.h>
#include <string>
#include <fstream>
#include <unistd.h>
#include <android/log.h>

#define LOG_TAG "SentinelNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean
JNICALL
Java_com_rs_kit_hook_detector_HookDetector_isFridaDetectedNative(JNIEnv *env, jobject thiz) {
    char line[512];
    FILE *fp = fopen("/proc/self/maps", "r");
    if (fp == nullptr) return JNI_FALSE;

    bool found = false;
    while (fgets(line, sizeof(line), fp)) {
        if (strstr(line, "frida") || strstr(line, "gum-js") || strstr(line, "linjector")) {
            found = true;
            break;
        }
    }
    fclose(fp);
    return found ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean
JNICALL
Java_com_rs_kit_hook_detector_HookDetector_checkFridaThreads(JNIEnv *env, jobject thiz) {
    return JNI_FALSE;
}
}