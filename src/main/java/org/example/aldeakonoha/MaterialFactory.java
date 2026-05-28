package org.example.aldeakonoha;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import java.util.HashMap;
import java.util.Map;

/**
 * FÁBRICA DE MATERIALES (MaterialFactory) - Proyecto Final de Graficación Computacional.
 * Responsabilidad: Centralizar, configurar y cachear materiales (PhongMaterial) 
 * de manera eficiente para optimizar el rendimiento de la GPU y mantener una estética visual coherente.
 * 
 * CONCEPTOS DE EXPOSICIÓN ACADÉMICA:
 * 1. Patrón Flyweight (Caché): Evita la creación de miles de instancias duplicadas de materiales,
 *    reduciendo el consumo de memoria de video y facilitando el loteado de mallas estáticas.
 * 2. Pipeline de Iluminación y Phong:
 *    - Diffuse Color (Color Difuso): Define la reflectividad base o color intrínseco bajo luz directa.
 *    - Specular Color (Color Especular): Controla el reflejo de la fuente de luz en superficies pulidas o metálicas.
 *    - Specular Power (Brillo/Shininess): Determina la concentración del reflejo especular (a mayor valor, reflejo más pequeño y nítido).
 *    - Emissive Simulation (Materiales Emisivos): Colores de alto brillo simulados para luces y sharingan, de modo que brillen en la oscuridad.
 */
public class MaterialFactory {

    private final Map<String, PhongMaterial> materials = new HashMap<>();

    public MaterialFactory() {
        initializeMaterials();
    }

    /**
     * Inicializa y pre-configura la rica paleta de materiales del proyecto.
     */
    private void initializeMaterials() {
        // --- 1. MATERIALES DE CONSTRUCCIÓN TRADICIONAL JAPONESA (MINKAS) ---
        PhongMaterial wood = new PhongMaterial();
        wood.setDiffuseColor(Color.web("#4E3629")); // Madera oscura estructural
        wood.setSpecularColor(Color.web("#1A120C"));
        wood.setSpecularPower(5);
        materials.put("wood", wood);

        PhongMaterial woodLight = new PhongMaterial();
        woodLight.setDiffuseColor(Color.web("#805A3C")); // Madera clara para barandillas y vallas
        woodLight.setSpecularColor(Color.web("#2A1E14"));
        materials.put("woodLight", woodLight);

        PhongMaterial wallCrema = new PhongMaterial();
        wallCrema.setDiffuseColor(Color.web("#EFEAD8")); // Paredes tradicionales de estuco
        wallCrema.setSpecularColor(Color.web("#111111"));
        materials.put("wall", wallCrema);

        PhongMaterial roofRed = new PhongMaterial();
        roofRed.setDiffuseColor(Color.web("#962D2D")); // Tejado tradicional rojo
        roofRed.setSpecularColor(Color.web("#501010"));
        roofRed.setSpecularPower(12);
        materials.put("roofRed", roofRed);

        PhongMaterial roofBlue = new PhongMaterial();
        roofBlue.setDiffuseColor(Color.web("#2E5077")); // Tejado azul ninja
        roofBlue.setSpecularColor(Color.web("#15253F"));
        roofBlue.setSpecularPower(12);
        materials.put("roofBlue", roofBlue);

        PhongMaterial roofBrown = new PhongMaterial();
        roofBrown.setDiffuseColor(Color.web("#5C4033")); // Tejado marrón rústico
        roofBrown.setSpecularColor(Color.BLACK);
        materials.put("roofBrown", roofBrown);

        PhongMaterial shoji = new PhongMaterial();
        shoji.setDiffuseColor(Color.web("#FAF6EE")); // Papel Shoji translúcido para ventanas y puertas
        materials.put("shoji", shoji);

        // --- 2. MATERIALES ESPECIALES DE EDIFICIOS IMPORTANTES ---
        PhongMaterial redOffice = new PhongMaterial();
        redOffice.setDiffuseColor(Color.web("#C43B3B")); // Gran Sede del Hokage (Rojo Brillante)
        redOffice.setSpecularColor(Color.web("#FF8888"));
        redOffice.setSpecularPower(20);
        materials.put("redOffice", redOffice);

        PhongMaterial roofGreen = new PhongMaterial();
        roofGreen.setDiffuseColor(Color.web("#2A6F62")); // Techos de la oficina del Hokage
        roofGreen.setSpecularColor(Color.web("#4A8F82"));
        roofGreen.setSpecularPower(15);
        materials.put("roofGreen", roofGreen);

        PhongMaterial gold = new PhongMaterial();
        gold.setDiffuseColor(Color.web("#D4AF37")); // Oro metálico para letreros y letras Ichiraku
        gold.setSpecularColor(Color.web("#FFFFFF"));
        gold.setSpecularPower(45);
        materials.put("gold", gold);

        PhongMaterial banners = new PhongMaterial();
        banners.setDiffuseColor(Color.web("#DC3545")); // Estandartes comerciales y cortinas Ichiraku
        banners.setSpecularColor(Color.web("#501010"));
        materials.put("banners", banners);

        // --- 3. MATERIALES NATURALES Y AMBIENTALES ---
        PhongMaterial groundGrass = new PhongMaterial();
        groundGrass.setDiffuseColor(Color.web("#1F3B20")); // Césped de la aldea
        groundGrass.setSpecularColor(Color.BLACK);
        materials.put("groundGrass", groundGrass);

        PhongMaterial groundHill = new PhongMaterial();
        groundHill.setDiffuseColor(Color.web("#2D5230")); // Césped de colinas
        groundHill.setSpecularColor(Color.BLACK);
        materials.put("groundHill", groundHill);

        PhongMaterial pathSand = new PhongMaterial();
        pathSand.setDiffuseColor(Color.web("#D9B48F")); // Arena de caminos principales
        pathSand.setSpecularColor(Color.web("#30251A"));
        pathSand.setSpecularPower(3);
        materials.put("pathSand", pathSand);

        PhongMaterial pathCobble = new PhongMaterial();
        pathCobble.setDiffuseColor(Color.web("#5D636B")); // Adoquines de plaza central
        pathCobble.setSpecularColor(Color.web("#9095A0"));
        pathCobble.setSpecularPower(8);
        materials.put("pathCobble", pathCobble);

        PhongMaterial water = new PhongMaterial();
        water.setDiffuseColor(Color.web("#2E6B80")); // Agua del riachuelo
        water.setSpecularColor(Color.web("#80D0FF")); // Brillo especular alto de reflejo del sol
        water.setSpecularPower(35);
        materials.put("water", water);

        PhongMaterial stoneGranite = new PhongMaterial();
        stoneGranite.setDiffuseColor(Color.web("#828891")); // Piedra granito regular
        stoneGranite.setSpecularColor(Color.web("#A0A5B0"));
        stoneGranite.setSpecularPower(10);
        materials.put("stone", stoneGranite);

        PhongMaterial stoneDark = new PhongMaterial();
        stoneDark.setDiffuseColor(Color.web("#4A4E54")); // Piedra de la montaña y murallas perimetrales
        stoneDark.setSpecularColor(Color.web("#606368"));
        stoneDark.setSpecularPower(8);
        materials.put("stoneDark", stoneDark);

        PhongMaterial leavesDark = new PhongMaterial();
        leavesDark.setDiffuseColor(Color.web("#1E3F20")); // Follaje oscuro para relieve
        leavesDark.setSpecularColor(Color.BLACK);
        materials.put("leavesDark", leavesDark);

        PhongMaterial leavesLight = new PhongMaterial();
        leavesLight.setDiffuseColor(Color.web("#3D6D41")); // Follaje claro
        leavesLight.setSpecularColor(Color.BLACK);
        materials.put("leavesLight", leavesLight);

        // --- 4. MATERIALES DE ITACHI UCHIHA ---
        PhongMaterial skin = new PhongMaterial();
        skin.setDiffuseColor(Color.web("#F6D7C8")); // Piel pálida
        skin.setSpecularColor(Color.web("#201010"));
        skin.setSpecularPower(5);
        materials.put("skin", skin);

        PhongMaterial hair = new PhongMaterial();
        hair.setDiffuseColor(Color.web("#151518")); // Cabello azabache
        hair.setSpecularColor(Color.web("#3E4249"));
        hair.setSpecularPower(18);
        materials.put("hair", hair);

        PhongMaterial cloak = new PhongMaterial();
        cloak.setDiffuseColor(Color.web("#0D0D10")); // Capa Akatsuki negra mate
        cloak.setSpecularColor(Color.web("#2B2E35"));
        cloak.setSpecularPower(5);
        materials.put("cloak", cloak);

        PhongMaterial innerCloak = new PhongMaterial();
        innerCloak.setDiffuseColor(Color.web("#800D0D")); // Forro rojo interno de la capa
        materials.put("innerCloak", innerCloak);

        PhongMaterial redCloud = new PhongMaterial();
        redCloud.setDiffuseColor(Color.web("#C81010")); // Nube Akatsuki carmesí en relieve
        redCloud.setSpecularColor(Color.RED);
        materials.put("redCloud", redCloud);

        PhongMaterial headband = new PhongMaterial();
        headband.setDiffuseColor(Color.web("#1F2432")); // Tela de bandana protectora
        materials.put("headband", headband);

        PhongMaterial metalPlate = new PhongMaterial();
        metalPlate.setDiffuseColor(Color.web("#A0A5B5")); // Placa metálica
        metalPlate.setSpecularColor(Color.WHITE);
        metalPlate.setSpecularPower(40);
        materials.put("metalPlate", metalPlate);

        PhongMaterial sharingan = new PhongMaterial();
        sharingan.setDiffuseColor(Color.web("#FF1E1E")); // Ojos Sharingan de alta emisividad
        sharingan.setSpecularColor(Color.WHITE);
        sharingan.setSpecularPower(50);
        materials.put("sharingan", sharingan);

        PhongMaterial sandals = new PhongMaterial();
        sandals.setDiffuseColor(Color.web("#12141F")); // Sandalias ninja
        materials.put("sandals", sandals);

        PhongMaterial socks = new PhongMaterial();
        socks.setDiffuseColor(Color.WHITE); // Calcetines blancos
        materials.put("socks", socks);

        PhongMaterial blackDetails = new PhongMaterial();
        blackDetails.setDiffuseColor(Color.BLACK); // Detalles negros de la pupila y el rayón
        materials.put("black", blackDetails);

        // --- 5. MATERIALES DE EFECTOS LUMINOSOS ---
        PhongMaterial glowingFire = new PhongMaterial();
        glowingFire.setDiffuseColor(Color.web("#FFB732")); // Fuego naranja/amarillo
        materials.put("glowingFire", glowingFire);

        PhongMaterial glowingAura = new PhongMaterial();
        glowingAura.setDiffuseColor(Color.web("#CC1414")); // Aura de chakra roja estándar
        materials.put("glowingAura", glowingAura);

        PhongMaterial glowingAuraActive = new PhongMaterial();
        glowingAuraActive.setDiffuseColor(Color.web("#FF5722")); // Aura de chakra activa en color naranja incandescente
        materials.put("glowingAuraActive", glowingAuraActive);

        PhongMaterial crowPlumage = new PhongMaterial();
        crowPlumage.setDiffuseColor(Color.web("#060A12")); // Plumaje negro azulado iridiscente
        crowPlumage.setSpecularColor(Color.web("#203554"));
        crowPlumage.setSpecularPower(12);
        materials.put("crow", crowPlumage);
    }

    /**
     * Obtiene un material preconfigurado desde el caché Flyweight.
     * Si la clave no existe, retorna un material gris de emergencia para no romper el renderizado.
     */
    public PhongMaterial getMaterial(String key) {
        if (materials.containsKey(key)) {
            return materials.get(key);
        }
        PhongMaterial fallback = new PhongMaterial(Color.LIGHTGRAY);
        return fallback;
    }
}
