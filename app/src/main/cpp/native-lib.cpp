#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_doc_on_call_Utilities_ObscuredSharedPreference_getPrefFile(JNIEnv *env, jobject object) {
    std::string mPrefFile = "doc.on.call";
    return env->NewStringUTF(mPrefFile.c_str());
}

extern "C" JNIEXPORT jstring

JNICALL
Java_doc_on_call_RetroFit_Request_RetrofitRequest_getAPIBaseURL(JNIEnv *env, jobject object) {
    std::string mAPIBaseURL = "https://ssd-backend-users.herokuapp.com/";
    return env->NewStringUTF(mAPIBaseURL.c_str());
}