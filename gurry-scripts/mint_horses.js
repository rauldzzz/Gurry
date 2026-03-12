const admin = require("firebase-admin");
const bs58 = require("bs58");
const crypto = require("crypto");

// 1. CONFIGURACIÓN DE FIREBASE ADMIN
// Necesitas descargar tu clave privada desde:
// Consola Firebase -> Configuración del proyecto -> Cuentas de servicio -> Generar nueva clave privada
// Guarda el archivo como "serviceAccountKey.json" en esta misma carpeta.
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// 2. CONFIGURACIÓN DE GENÉTICA
const COLORS = ["Black", "White", "Chestnut", "Bay", "Gray", "Palomino", "Roan"];
const HORSE_NAMES = [
    "Thunder Bolt", "Cyber Spirit", "Shadow Dancer", "Moon Rocket", 
    "Solar Flare", "Golden Hoof", "Pixel Runner", "Crypto King", 
    "Ether Wind", "Satoshi's Pride", "Lightning Strike", "Velvet Storm", 
    "Iron Will", "Nebula Chaser", "Quantum Leap", "Block Gallop",
    "Midnight Star", "Phantom Ace", "Royal Ledger", "Infinite Speed"
];

// Función auxiliar para normalizar un valor Hexadecimal a un float entre 10 y 100
// Usamos 4 caracteres hex (0x0000 a 0xFFFF = 0 a 65535)
function hexToStat(hexStr, min = 10, max = 100) {
    const decimal = parseInt(hexStr, 16);
    const normalized = decimal / 65535; // 0.0 a 1.0
    const val = min + (normalized * (max - min)); 
    return parseFloat(val.toFixed(2)); // Redondear a 2 decimales
}

async function mintHorse() {
    // A. GENERAR EL METADATA_ID (Base58)
    // Generamos 16 bytes aleatorios y los codificamos en Base58
    const randomBytes = crypto.randomBytes(32); 
    const metadata_id = bs58.encode(randomBytes);

    // B. HASHEAR EL ID PARA OBTENER EL "ADN"
    // Esto simula que las stats vienen de la blockchain.
    // Si tienes el metadata_id, siempre tendrás las mismas stats.
    const hash = crypto.createHash('sha256').update(metadata_id).digest('hex');
    // El hash es un string largo: "a3f5e2..."

    // C. PARSEAR EL HASH PARA SACAR STATS (Genetic Slicing)
    // Cogemos trozos del hash para cada atributo
    const gallop = hexToStat(hash.substring(0, 4));       // Bytes 0-1
    const snatch = hexToStat(hash.substring(4, 8));       // Bytes 2-3
    const endurance = hexToStat(hash.substring(8, 12));   // Bytes 4-5
    const acceleration = hexToStat(hash.substring(12, 16));// Bytes 6-7
    
    // D. DETERMINAR COLOR (Usamos otro trozo del hash y módulo)
    const colorHex = parseInt(hash.substring(16, 18), 16); // 0-255
    const color = COLORS[colorHex % COLORS.length];

    // E. CALCULAR SCORE
    const score = parseFloat(((gallop + snatch + endurance + acceleration) / 4).toFixed(2));

    //Precio
    const price = parseFloat((Math.random() * 49 + 1).toFixed(2));

    //Names
    const randomName = HORSE_NAMES[Math.floor(Math.random() * HORSE_NAMES.length)];

    // F. OBJETO DEL CABALLO
    const horseData = {
        metadata_id: metadata_id, // El ID tipo Solana/Bitcoin
        name: randomName,
        horse_img: ``, // Ejemplo de ruta
        display_3d: ``, // Ejemplo de ruta
        id_owner: "0", // 0 = Pertenece al Mercado/Sistema
        color: color,
        gallop: gallop,
        snatch: snatch,
        endurance: endurance,
        acceleration: acceleration,
        score: score,
        price: price,
        is_racing: false,
        created_at: admin.firestore.FieldValue.serverTimestamp()
    };

    // G. GUARDAR EN FIRESTORE
    // Usamos el metadata_id como el ID del documento para búsquedas rápidas
    try {
        await db.collection("crypto_horses").doc(metadata_id).set(horseData);
        console.log(`✅ Caballo generado: ${metadata_id} | Color: ${color} | Score: ${score}`);
    } catch (error) {
        console.error("Error guardando el caballo:", error);
    }
}

// EJECUCIÓN: Generar 10 caballos de golpe
async function main() {
    console.log("🐎 Iniciando proceso de 'Minado' de caballos...");
    for (let i = 0; i < 10; i++) {
        await mintHorse();
    }
    console.log("Terminado.");
}

main();