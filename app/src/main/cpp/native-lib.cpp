#include <jni.h>
#include <string>

/**
 * Function to fetch Shared Preference File
 */
extern "C" JNIEXPORT jstring

JNICALL
Java_doc_on_call_Utilities_ObscuredSharedPreference_getPrefFile(JNIEnv *env, jobject object) {
    std::string mPrefFile = "doc.on.call";
    return env->NewStringUTF(mPrefFile.c_str());
}

/**
 * Function to fetch Safety Net Recaptcha API Key
 */
extern "C" JNIEXPORT jstring

JNICALL
Java_doc_on_call_SignInActivity_getSiteAPIKey(JNIEnv *env, jclass object) {
    std::string mSiteAPIKey = "6Ld9xcAUAAAAABngekazozbyu2X-5GsQCBzb2dQ7";
    return env->NewStringUTF(mSiteAPIKey.c_str());
}

/**
 * Function to fetch REST API URL
 */
extern "C" JNIEXPORT jstring

JNICALL
Java_doc_on_call_RetroFit_Request_RetrofitRequest_getAPIBaseURL(JNIEnv *env, jobject object) {
    std::string mAPIBaseURL = "https://doconcall.tk/";
    return env->NewStringUTF(mAPIBaseURL.c_str());
}

/**
 * Function to fetch Checker Non Writable Path
 */
extern "C" JNIEXPORT jobjectArray

JNICALL
Java_doc_on_call_Utilities_Checker_getNonWritablePath(JNIEnv *env, jobject object) {
    jobjectArray ret;
    int i;

    char *data[7]= {"/system",
                    "/system/bin",
                    "/system/sbin",
                    "/system/xbin",
                    "/vendor/bin",
                    "/sbin",
                    "/etc"};

    ret= (jobjectArray)env->NewObjectArray(5,env->FindClass("java/lang/String"),env->NewStringUTF(""));

    for(i=0;i<5;i++) env->SetObjectArrayElement(ret,i,env->NewStringUTF(data[i]));

    return(ret);
}

/**
 * Function to fetch Checker Binary Path
 */
extern "C" JNIEXPORT jobjectArray

JNICALL
Java_doc_on_call_Utilities_Checker_getBinaryPath(JNIEnv *env, jobject object) {
    jobjectArray ret;
    int i;

    char *data[14]= {"/data/local/",
                    "/data/local/bin/",
                    "/data/local/xbin/",
                    "/sbin/",
                    "/su/bin/",
                    "/system/bin/",
                    "/system/bin/.ext/",
                    "/system/bin/failsafe/",
                    "/system/sd/xbin/",
                    "/system/usr/we-need-root/",
                    "/system/xbin/",
                    "/cache",
                    "/data",
                    "/dev"};

    ret= (jobjectArray)env->NewObjectArray(5,env->FindClass("java/lang/String"),env->NewStringUTF(""));

    for(i=0;i<5;i++) env->SetObjectArrayElement(ret,i,env->NewStringUTF(data[i]));

    return(ret);
}

/**
 * Function to fetch Checker Root Related Package
 */
extern "C" JNIEXPORT jobjectArray

JNICALL
Java_doc_on_call_Utilities_Checker_getRootRelatedPackages(JNIEnv *env, jobject object) {
    jobjectArray ret;
    int i;

    char *data[24]= {"com.noshufou.android.su",
                    "com.noshufou.android.su.elite",
                    "eu.chainfire.supersu",
                    "com.koushikdutta.superuser",
                    "com.thirdparty.superuser",
                    "com.yellowes.su",
                    "com.topjohnwu.magisk",
                    "com.koushikdutta.rommanager",
                    "com.koushikdutta.rommanager.license",
                    "com.dimonvideo.luckypatcher",
                    "com.chelpus.lackypatch",
                    "com.ramdroid.appquarantine",
                    "com.ramdroid.appquarantinepro",
                    "com.android.vending.billing.InAppBillingService.COIN",
                    "com.chelpus.luckypatcher",
                    "com.devadvance.rootcloak",
                    "com.devadvance.rootcloakplus",
                    "de.robv.android.xposed.installer",
                    "com.saurik.substrate",
                    "com.zachspong.temprootremovejb",
                    "com.amphoras.hidemyroot",
                    "com.amphoras.hidemyrootadfree",
                    "com.formyhm.hiderootPremium",
                    "com.formyhm.hideroot"};

    ret= (jobjectArray)env->NewObjectArray(5,env->FindClass("java/lang/String"),env->NewStringUTF(""));

    for(i=0;i<5;i++) env->SetObjectArrayElement(ret,i,env->NewStringUTF(data[i]));

    return(ret);
}