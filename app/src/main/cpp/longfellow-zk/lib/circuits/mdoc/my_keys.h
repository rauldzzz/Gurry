#pragma once
#include <stddef.h>
#include <stdint.h>
#include "circuits/mdoc/mdoc_zk.h" 
#include "algebra/static_string.h"          
#include "circuits/mdoc/mdoc_attribute_ids.h" 


namespace public_keys {

    // El Verifier SOLO necesita conocer la Clave Pública de la autoridad emisora
    struct IssuerPublicKey {
        proofs::StaticString pkx;
        proofs::StaticString pky;
    };

    // Claves Públicas de ejemplo (La "Policía")
    const IssuerPublicKey POLICE_KEYS = {
            StaticString("0x0217e617f0b6443928278f96999e69a23a4f2c152bdf6d6cdf66e5b80282d4ed"),
            StaticString("0x194a7debcb97712d2dda3ca85aa8765a56f45fc758599652f2897c65306e5794")
    };

}} // namespace custom_mock