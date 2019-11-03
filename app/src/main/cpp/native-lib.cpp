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