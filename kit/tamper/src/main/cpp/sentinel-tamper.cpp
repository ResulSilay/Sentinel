#include <jni.h>
#include <string>
#include <cstring>

#define LOG_TAG "SentinelNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean
JNICALL
Java_com_rs_kit_tamper_detector_TamperDetector_verifyIntegrity(JNIEnv *env, jobject thiz, jobject context, jstring expectedPackage, jstring expectedSignature) {

    const char *expPkg = env->GetStringUTFChars(expectedPackage, nullptr);
    const char *expSig = env->GetStringUTFChars(expectedSignature, nullptr);

    jclass contextClass = env->GetObjectClass(context);
    jmethodID getPkgMethod = env->GetMethodID(contextClass, "getPackageName", "()Ljava/lang/String;");
    jstring currentPackage = (jstring) env->CallObjectMethod(context, getPkgMethod);
    const char *curPkg = env->GetStringUTFChars(currentPackage, nullptr);

    if (strcmp(expPkg, curPkg) != 0) {
        env->ReleaseStringUTFChars(expectedPackage, expPkg);
        env->ReleaseStringUTFChars(currentPackage, curPkg);
        return JNI_FALSE;
    }

    jmethodID getPmMethod = env->GetMethodID(contextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject pm = env->CallObjectMethod(context, getPmMethod);
    jclass pmClass = env->GetObjectClass(pm);
    jmethodID getPkgInfoMethod = env->GetMethodID(pmClass, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    jobject pkgInfo = env->CallObjectMethod(pm, getPkgInfoMethod, currentPackage, 64);
    jclass pkgInfoClass = env->GetObjectClass(pkgInfo);
    jfieldID sigField = env->GetFieldID(pkgInfoClass, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray sigArray = (jobjectArray) env->GetObjectField(pkgInfo, sigField);
    jobject sigObj = env->GetObjectArrayElement(sigArray, 0);

    jclass sigClass = env->GetObjectClass(sigObj);
    jmethodID toCharsMethod = env->GetMethodID(sigClass, "toCharsString", "()Ljava/lang/String;");
    jstring currentSignature = (jstring) env->CallObjectMethod(sigObj, toCharsMethod);
    const char *curSig = env->GetStringUTFChars(currentSignature, nullptr);

    jboolean result = (strcmp(expSig, curSig) == 0) ? JNI_TRUE : JNI_FALSE;

    env->ReleaseStringUTFChars(expectedPackage, expPkg);
    env->ReleaseStringUTFChars(expectedSignature, expSig);
    env->ReleaseStringUTFChars(currentPackage, curPkg);
    env->ReleaseStringUTFChars(currentSignature, curSig);

    return result;
}
}