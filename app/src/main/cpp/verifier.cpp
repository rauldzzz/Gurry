#include <jni.h>
#include <malloc.h>
#include <android/log.h>
#include <stdio.h>
#include <string>

// Routes
#include "circuits/mdoc/mdoc_zk.h"
#include "circuits/mdoc/mdoc_examples.h"
#include "circuits/mdoc/mdoc_test_attributes.h"
#include "circuits/mdoc/my_keys.h"

using namespace proofs;

// Macro para usar logs de Android
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "ZkNative", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "ZkNative", __VA_ARGS__)

static uint8_t *circuit_ = nullptr;
static size_t len_ = 0;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_gurry_viewmodels_AuthViewModel_verificarPruebaZkNativa(
        JNIEnv *env, jobject thiz, jbyteArray proof_array, jbyteArray transcript_array, jstring cacheDir_jstr) {

    // ==========================================
    // 1. GESTIÓN DE LA CACHÉ DEL CIRCUITO
    // ==========================================
    const char *cache_dir_cstr = env->GetStringUTFChars(cacheDir_jstr, nullptr);
    std::string file_path = std::string(cache_dir_cstr) + "/circuito_mdoc.bin";
    env->ReleaseStringUTFChars(cacheDir_jstr, cache_dir_cstr);

    if (!circuit_) {
        FILE* f = fopen(file_path.c_str(), "rb");
        if (f) {
            LOGI("✅ VERIFIER: Cargando circuito desde caché...");
            fseek(f, 0, SEEK_END);
            len_ = ftell(f);
            fseek(f, 0, SEEK_SET);
            circuit_ = (uint8_t*) malloc(len_);
            fread(circuit_, 1, len_, f);
            fclose(f);
            LOGI("✅ VERIFIER: Circuito cargado en memoria (%zu bytes).", len_);
        } else {
            LOGI("⚠️ VERIFIER: Circuito no encontrado. Compilando...");
            generate_circuit(&kZkSpecs[0], &circuit_, &len_);

            f = fopen(file_path.c_str(), "wb");
            if (f) {
                fwrite(circuit_, 1, len_, f);
                fclose(f);
                LOGI("💾 VERIFIER: Circuito guardado en caché.");
            }
        }
    }

    // ==========================================
    // 2. EXTRAER EL BYTEARRAY ENVIADO DESDE KOTLIN
    // ==========================================
    jbyte* proof_bytes = env->GetByteArrayElements(proof_array, nullptr);
    size_t proof_len = env->GetArrayLength(proof_array);

    jbyte* transcript_bytes = env->GetByteArrayElements(transcript_array, nullptr);
    size_t transcript_len = env->GetArrayLength(transcript_array);

    // ==========================================
    // 3. EJECUTAR EL VERIFIER
    // ==========================================
    const RequestedAttribute attrs[] = { test::age_over_18 };

    const char* current_time = "2024-05-20T12:00:00Z";

    LOGI("--- INICIANDO VERIFICACIÓN DE PRUEBA RECIBIDA (%zu bytes) ---", proof_len);

    MdocVerifierErrorCode ret_verifier = run_mdoc_verifier(
            circuit_, len_,
            public_keys::POLICE_KEYS.pkx.as_pointer,
            public_keys::POLICE_KEYS.pky.as_pointer,
            (uint8_t*)transcript_bytes, transcript_len,
            attrs, 1,
            (const char*)current_time,
            (uint8_t*)proof_bytes, proof_len, // Pasamos la prueba recibida
            proofs::kMDLDocType,
            &kZkSpecs[0]
    );

    // Liberamos la memoria del ByteArray copiado
    env->ReleaseByteArrayElements(proof_array, proof_bytes, JNI_ABORT);
    env->ReleaseByteArrayElements(transcript_array, transcript_bytes, JNI_ABORT);

    // 4. RETORNAR EL RESULTADO BOOLEANO A KOTLIN
    if (ret_verifier == MDOC_VERIFIER_SUCCESS) {
        LOGI(">>> ÉXITO: ¡El Verifier confirma que la prueba es legítima!");
        return JNI_TRUE;
    } else {
        LOGE(">>> ERROR: El Verifier ha rechazado la prueba (Código: %d).", ret_verifier);
        return JNI_FALSE;
    }
}