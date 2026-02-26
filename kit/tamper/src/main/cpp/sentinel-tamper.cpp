#include <jni.h>
#include <android/log.h>
#include <string>
#include <cstring>

#define LOG_TAG "SentinelNative"
#if defined(DEBUG) || defined(_DEBUG)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
#define LOGI(...) ((void)0)
#define LOGE(...) ((void)0)
#endif

extern "C" {

std::string bytesToHex(JNIEnv *env, jbyteArray array) {
    if (!array) return "";

    jsize len = env->GetArrayLength(array);
    jbyte *data = env->GetByteArrayElements(array, nullptr);
    if (!data) return "";

    static const char hexChars[] = "0123456789ABCDEF";
    std::string result;
    result.reserve(len * 2);

    for (int i = 0; i < len; i++) {
        unsigned char b = static_cast<unsigned char>(data[i]);
        result.push_back(hexChars[b >> 4]);
        result.push_back(hexChars[b & 0x0F]);
    }

    env->ReleaseByteArrayElements(array, data, JNI_ABORT);
    return result;
}

std::string hexToString(const std::string &hex) {
    std::string result;
    result.reserve(hex.length() / 2);

    for (size_t i = 0; i < hex.length(); i += 2) {
        std::string byteStr = hex.substr(i, 2);
        char byte = static_cast<char>(std::stoi(byteStr, nullptr, 16));
        result.push_back(byte);
    }

    return result;
}

std::string sha256HexJNI(JNIEnv *env, jbyteArray dataArray) {
    if (!dataArray) return "";

    jclass mdClass = env->FindClass("java/security/MessageDigest");
    jmethodID getInstance = env->GetStaticMethodID(mdClass, "getInstance",
            "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jstring sha256Str = env->NewStringUTF("SHA-256");
    jobject mdObj = env->CallStaticObjectMethod(mdClass, getInstance, sha256Str);
    env->DeleteLocalRef(sha256Str);

    jmethodID digestMethod = env->GetMethodID(mdClass, "digest", "([B)[B");
    jbyteArray hashArray = (jbyteArray) env->CallObjectMethod(mdObj, digestMethod, dataArray);

    std::string result = bytesToHex(env, hashArray);

    env->DeleteLocalRef(hashArray);
    env->DeleteLocalRef(mdObj);
    env->DeleteLocalRef(mdClass);

    return result;
}

JNIEXPORT jboolean
JNICALL
Java_com_rs_kit_tamper_detector_TamperDetector_verifyIntegrity(
        JNIEnv *env,
        jobject thiz,
        jobject context,
        jbyteArray expectedPackage,
        jbyteArray expectedSignature
) {
    std::string expPkgStr = hexToString(bytesToHex(env, expectedPackage));
    std::string expSigStr = hexToString(bytesToHex(env, expectedSignature));

    jclass contextClass = env->GetObjectClass(context);
    jmethodID getPkgMethod = env->GetMethodID(contextClass, "getPackageName", "()Ljava/lang/String;");
    jstring currentPackage = (jstring) env->CallObjectMethod(context, getPkgMethod);

    std::string curPkgStr;
    if (currentPackage) {
        const char *tmp = env->GetStringUTFChars(currentPackage, nullptr);
        curPkgStr = tmp ? tmp : "";
        env->ReleaseStringUTFChars(currentPackage, tmp);
        env->DeleteLocalRef(currentPackage);
    }

    LOGI("Expected package : %s", expPkgStr.c_str());
    LOGI("Current package  : %s", curPkgStr.c_str());

    if (expPkgStr != curPkgStr) return JNI_FALSE;

    jmethodID getPmMethod = env->GetMethodID(contextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject pm = env->CallObjectMethod(context, getPmMethod);

    jclass pmClass = env->GetObjectClass(pm);
    jmethodID getPkgInfoMethod = env->GetMethodID(pmClass, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    jstring pkgNameJStr = env->NewStringUTF(curPkgStr.c_str());
    jobject pkgInfo = env->CallObjectMethod(pm, getPkgInfoMethod, pkgNameJStr, 64);
    env->DeleteLocalRef(pkgNameJStr);

    jclass pkgInfoClass = env->GetObjectClass(pkgInfo);
    jfieldID sigField = env->GetFieldID(pkgInfoClass, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray sigArray = (jobjectArray) env->GetObjectField(pkgInfo, sigField);

    jobject sigObj = env->GetObjectArrayElement(sigArray, 0);
    jclass sigClass = env->GetObjectClass(sigObj);
    jmethodID toByteArrayMethod = env->GetMethodID(sigClass, "toByteArray", "()[B");
    jbyteArray curSigArray = (jbyteArray) env->CallObjectMethod(sigObj, toByteArrayMethod);
    std::string curSigStr = sha256HexJNI(env, curSigArray);
    env->DeleteLocalRef(curSigArray);

    LOGI("Expected signature : %s", expSigStr.c_str());
    LOGI("Current signature  : %s", curSigStr.c_str());

    env->DeleteLocalRef(sigObj);
    env->DeleteLocalRef(sigArray);
    env->DeleteLocalRef(sigClass);
    env->DeleteLocalRef(pkgInfo);
    env->DeleteLocalRef(pkgInfoClass);
    env->DeleteLocalRef(pm);
    env->DeleteLocalRef(pmClass);

    jboolean result = (expSigStr == curSigStr) ? JNI_TRUE : JNI_FALSE;

    LOGE("Result: %hhu", result);

    return result;
}
}