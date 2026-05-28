package org.example.aldeakonoha;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import java.util.ArrayList;
import java.util.List;

/**
 * MODELO 3D AVANZADO DE ITACHI UCHIHA (ItachiModel) - Proyecto Final de Graficación Computacional.
 * Responsabilidad: Modelar al legendario shinobi Itachi Uchiha mediante primitivas complejas,
 * integrando a su vez un sistema de partículas dinámico (Chakra Aura) y cuervos en órbita matemática.
 * 
 * CONCEPTOS DE EXPOSICIÓN ACADÉMICA:
 * 1. Modelado por Primitivas Compuestas: Construcción de un personaje humanoide altamente reconocible
 *    ensamblando cajas, cilindros y esferas en lugar de mallas poligonales externas.
 * 2. Simulación Cinética y Orbitación Tangencial: En cada frame, se calcula el vector de posición circular
 *    de los cuervos y se orienta su guiñada (Yaw) a lo largo de la tangente orbital, emulando vuelo real.
 * 3. Sistema de Partículas (Chakra System): Bucle físico de actualización en Y que altera dinámicamente
 *    la traslación, trayectoria espiral trigonométrica y opacidad individual (Alpha blending) de mallas 3D.
 * 4. Materiales Emisivos de Alta Opacidad: Sharingan brillante e interacción por eventos que duplica
 *    la escala cromática del aura física a sus pies.
 */
public class ItachiModel extends Group {

    private final MaterialFactory materialFactory;

    // Componentes interactivos de Aura a los pies
    private Cylinder auraBase;
    private Cylinder auraOuter;
    private PhongMaterial auraBaseMat;
    private PhongMaterial auraOuterMat;

    // Grupos para partículas y cuervos
    private final Group particlesGroup = new Group();
    private final List<Sphere> particleNodes = new ArrayList<>();
    private final List<ParticleData> particleDataList = new ArrayList<>();
    private final List<Group> crowNodes = new ArrayList<>();

    // Estado del Chakra Flare
    private boolean isFlareActive = false;

    // Estructura de datos interna para el movimiento físico de cada partícula de chakra
    private static class ParticleData {
        double angleOffset;
        double speedY;
        double baseRadius;
        double currentY;
    }

    public ItachiModel(MaterialFactory materialFactory) {
        this.materialFactory = materialFactory;
        
        // Ensamblar anatomía de Itachi
        buildItachiCharacter();
        
        // Agregar la base rúnica de aura
        addChakraAuraRing();
        
        // Poblar el sistema de partículas
        setupParticles();
        
        // Instanciar y agregar cuervos en órbita
        spawnAkatsukiCrows();
        
        // Agregar grupos de efectos al Grafo de Escena
        getChildren().addAll(particlesGroup);
    }

    /**
     * Ensambla la detallada anatomía 3D y los pliegues de la capa Akatsuki.
     */
    private void buildItachiCharacter() {
        // --- 1. CAPA DE AKATSUKI (ESTRUCTURA CILÍNDRICA CÓNICA) ---
        Cylinder cloakLower = new Cylinder(25, 45, 16);
        cloakLower.setMaterial(materialFactory.getMaterial("cloak"));
        cloakLower.setTranslateY(20);
        getChildren().add(cloakLower);

        Cylinder cloakMiddle = new Cylinder(20, 35, 16);
        cloakMiddle.setMaterial(materialFactory.getMaterial("cloak"));
        cloakMiddle.setTranslateY(-15);
        getChildren().add(cloakMiddle);

        Cylinder cloakUpper = new Cylinder(16, 25, 16);
        cloakUpper.setMaterial(materialFactory.getMaterial("cloak"));
        cloakUpper.setTranslateY(-40);
        getChildren().add(cloakUpper);

        // Cuello alto rígido de Akatsuki
        Cylinder collarOuter = new Cylinder(13.5, 18, 16);
        collarOuter.setMaterial(materialFactory.getMaterial("cloak"));
        collarOuter.setTranslateY(-56);
        getChildren().add(collarOuter);

        Cylinder collarInner = new Cylinder(12.5, 18, 16);
        collarInner.setMaterial(materialFactory.getMaterial("innerCloak"));
        collarInner.setTranslateY(-56);
        getChildren().add(collarInner);

        // --- 2. NUBES ROJAS DE AKATSUKI EN RELIEVE 3D ---
        // Nube Frontal (Pecho)
        Group frontCloud = create3DCloud();
        frontCloud.setTranslateZ(16.5);
        frontCloud.setTranslateY(-36);
        frontCloud.setTranslateX(4);
        frontCloud.setScaleX(0.75);
        frontCloud.setScaleY(0.75);
        getChildren().add(frontCloud);

        // Nube Trasera (Espalda)
        Group backCloud = create3DCloud();
        backCloud.setTranslateZ(-18.5);
        backCloud.setTranslateY(-26);
        backCloud.setTranslateX(-5);
        backCloud.setScaleX(1.15);
        backCloud.setScaleY(1.15);
        backCloud.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        getChildren().add(backCloud);

        // Nube en el costado inferior
        Group sideCloud = create3DCloud();
        sideCloud.setTranslateX(22);
        sideCloud.setTranslateY(15);
        sideCloud.setTranslateZ(-3);
        sideCloud.setScaleX(0.85);
        sideCloud.setScaleY(0.85);
        sideCloud.getTransforms().add(new Rotate(-90, Rotate.Y_AXIS));
        getChildren().add(sideCloud);

        // --- 3. EXTREMIDADES (BRAZOS CRUZADOS Y PIES) ---
        // Brazos cruzados ocultos solemnemente bajo la capa
        Box arms = new Box(32, 14, 18);
        arms.setMaterial(materialFactory.getMaterial("cloak"));
        arms.setTranslateY(-25);
        arms.setTranslateZ(10);
        arms.getTransforms().add(new Rotate(8, Rotate.X_AXIS));
        getChildren().add(arms);

        // Pies con sandalias ninja tradicionales y medias blancas
        Box sockL = new Box(6, 10, 10);
        sockL.setMaterial(materialFactory.getMaterial("socks"));
        sockL.setTranslateX(-8);
        sockL.setTranslateY(43);
        sockL.setTranslateZ(5);
        
        Box sandalL = new Box(7, 4, 14);
        sandalL.setMaterial(materialFactory.getMaterial("sandals"));
        sandalL.setTranslateX(-8);
        sandalL.setTranslateY(47);
        sandalL.setTranslateZ(6);
        getChildren().addAll(sockL, sandalL);

        Box sockR = new Box(6, 10, 10);
        sockR.setMaterial(materialFactory.getMaterial("socks"));
        sockR.setTranslateX(8);
        sockR.setTranslateY(43);
        sockR.setTranslateZ(5);

        Box sandalR = new Box(7, 4, 14);
        sandalR.setMaterial(materialFactory.getMaterial("sandals"));
        sandalR.setTranslateX(8);
        sandalR.setTranslateY(47);
        sandalR.setTranslateZ(6);
        getChildren().addAll(sockR, sandalR);

        // --- 4. CABEZA Y DETALLES DE ROSTRO (SHARINGAN ACTIVADO Y OJERAS) ---
        Group headGroup = new Group();
        headGroup.setTranslateY(-74);

        // Cabeza base de piel
        Sphere head = new Sphere(13.5);
        head.setMaterial(materialFactory.getMaterial("skin"));
        headGroup.getChildren().add(head);

        // Ojos Sharingan brillantes (Emisores rojos en el frontal de la cara)
        Sphere eyeL = new Sphere(1.5);
        eyeL.setMaterial(materialFactory.getMaterial("sharingan"));
        eyeL.setTranslateX(-4.2);
        eyeL.setTranslateY(0.8);
        eyeL.setTranslateZ(12.6);
        
        Sphere eyeR = new Sphere(1.5);
        eyeR.setMaterial(materialFactory.getMaterial("sharingan"));
        eyeR.setTranslateX(4.2);
        eyeR.setTranslateY(0.8);
        eyeR.setTranslateZ(12.6);
        headGroup.getChildren().addAll(eyeL, eyeR);

        // Surcos faciales de Itachi (Las líneas características bajo sus ojos)
        Box creaseL = new Box(5.2, 0.6, 0.4);
        creaseL.setMaterial(materialFactory.getMaterial("black"));
        creaseL.setTranslateX(-5.0);
        creaseL.setTranslateY(3.5);
        creaseL.setTranslateZ(12.8);
        creaseL.getTransforms().add(new Rotate(22, Rotate.Z_AXIS));

        Box creaseR = new Box(5.2, 0.6, 0.4);
        creaseR.setMaterial(materialFactory.getMaterial("black"));
        creaseR.setTranslateX(5.0);
        creaseR.setTranslateY(3.5);
        creaseR.setTranslateZ(12.8);
        creaseR.getTransforms().add(new Rotate(-22, Rotate.Z_AXIS));
        headGroup.getChildren().addAll(creaseL, creaseR);

        // --- 5. BANDANA PROTECTORA CON RAYÓN NINJA RENEGADO ---
        Box headband = new Box(28, 5.2, 28);
        headband.setMaterial(materialFactory.getMaterial("headband"));
        headband.setTranslateY(-2);
        headGroup.getChildren().add(headband);

        Box metalPlate = new Box(11.5, 4.6, 1);
        metalPlate.setMaterial(materialFactory.getMaterial("metalPlate"));
        metalPlate.setTranslateY(-2);
        metalPlate.setTranslateZ(13.8);
        headGroup.getChildren().add(metalPlate);

        // Símbolo de la Hoja rayado
        Box slashMark = new Box(9.2, 0.7, 0.5);
        slashMark.setMaterial(materialFactory.getMaterial("black"));
        slashMark.setTranslateY(-2);
        slashMark.setTranslateZ(14.2);
        slashMark.getTransforms().add(new Rotate(18, Rotate.Z_AXIS));
        headGroup.getChildren().add(slashMark);

        // --- 6. CABELLO AZABACHE CON MECHONES LARGOS Y COLETA ---
        // Mechón Izquierdo enmarcando la cara
        Box bangL = new Box(3.2, 18, 4.2);
        bangL.setMaterial(materialFactory.getMaterial("hair"));
        bangL.setTranslateX(-9);
        bangL.setTranslateY(5.0);
        bangL.setTranslateZ(8);
        bangL.getTransforms().add(new Rotate(-12, Rotate.Z_AXIS));

        // Mechón Derecho
        Box bangR = new Box(3.2, 18, 4.2);
        bangR.setMaterial(materialFactory.getMaterial("hair"));
        bangR.setTranslateX(9);
        bangR.setTranslateY(5.0);
        bangR.setTranslateZ(8);
        bangR.getTransforms().add(new Rotate(12, Rotate.Z_AXIS));
        headGroup.getChildren().addAll(bangL, bangR);

        // Cabello superior
        Sphere topHair = new Sphere(14.3);
        topHair.setMaterial(materialFactory.getMaterial("hair"));
        topHair.setTranslateY(-4);
        topHair.setTranslateZ(-2);
        headGroup.getChildren().add(topHair);

        // Coleta en la espalda (Ponytail)
        Cylinder ponytailUp = new Cylinder(3.8, 18, 8);
        ponytailUp.setMaterial(materialFactory.getMaterial("hair"));
        ponytailUp.setTranslateZ(-14);
        ponytailUp.setTranslateY(11);
        ponytailUp.getTransforms().add(new Rotate(-16, Rotate.X_AXIS));
        headGroup.getChildren().add(ponytailUp);

        // Liga roja de la coleta
        Cylinder hairTie = new Cylinder(4.8, 3.2, 8);
        hairTie.setMaterial(materialFactory.getMaterial("redCloud"));
        hairTie.setTranslateZ(-17.8);
        hairTie.setTranslateY(19.5);
        hairTie.getTransforms().add(new Rotate(-16, Rotate.X_AXIS));
        headGroup.getChildren().add(hairTie);

        // Cola de caballo inferior
        Cylinder ponytailLow = new Cylinder(2.8, 21, 8);
        ponytailLow.setMaterial(materialFactory.getMaterial("hair"));
        ponytailLow.setTranslateZ(-20.5);
        ponytailLow.setTranslateY(29.0);
        ponytailLow.getTransforms().add(new Rotate(-16, Rotate.X_AXIS));
        headGroup.getChildren().add(ponytailLow);

        getChildren().add(headGroup);
    }

    /**
     * Agrega el imponente aro de chakra brillante a sus pies.
     */
    private void addChakraAuraRing() {
        auraBaseMat = new PhongMaterial();
        auraBaseMat.setDiffuseColor(Color.web("#CC1414"));

        auraOuterMat = new PhongMaterial();
        auraOuterMat.setDiffuseColor(Color.web("#800D0D"));

        // Aro central plano
        auraBase = new Cylinder(38, 0.6, 16);
        auraBase.setMaterial(auraBaseMat);
        auraBase.setTranslateY(48.5); // Nivel de suelo de plaza
        getChildren().add(auraBase);

        // Aro externo de expansión
        auraOuter = new Cylinder(44, 0.3, 16);
        auraOuter.setMaterial(auraOuterMat);
        auraOuter.setTranslateY(48.7);
        getChildren().add(auraOuter);
    }

    /**
     * Inicializa 25 partículas de chakra en espiral ascendente con materiales individuales
     * para posibilitar el blending alfa suave por partícula.
     */
    private void setupParticles() {
        int count = 25;
        for (int i = 0; i < count; i++) {
            // Cada partícula tiene su PhongMaterial propio para que la opacidad no afecte a las demás
            PhongMaterial pMat = new PhongMaterial();
            pMat.setDiffuseColor(Color.web(i % 2 == 0 ? "#CC1414" : "#240003"));
            
            // Geometría de microesfera de chakra
            Sphere part = new Sphere(1.8 + Math.random() * 1.5);
            part.setMaterial(pMat);

            ParticleData data = new ParticleData();
            data.angleOffset = Math.random() * 2.0 * Math.PI;
            data.speedY = 1.2 + Math.random() * 1.5;
            data.baseRadius = 14.0 + Math.random() * 20.0;
            data.currentY = 40.0 - Math.random() * 120.0; // Altura aleatoria inicial

            part.setTranslateY(data.currentY);
            
            particleNodes.add(part);
            particleDataList.add(data);
            particlesGroup.getChildren().add(part);
        }
    }

    /**
     * Instancia cuervos ninja de bajo poligonaje que volarán en órbitas asimétricas.
     */
    private void spawnAkatsukiCrows() {
        int numCrows = 3;
        for (int i = 0; i < numCrows; i++) {
            Group crow = create3DCrow();
            crowNodes.add(crow);
            getChildren().add(crow);
        }
    }

    /**
     * Crea un Cuervo Ninja 3D en bajo poligonaje (Low-Poly).
     */
    private Group create3DCrow() {
        Group crow = new Group();

        // Cuerpo (Esfera negra alargada)
        Sphere body = new Sphere(4.5);
        body.setMaterial(materialFactory.getMaterial("crow"));
        body.setScaleZ(1.8);
        body.setScaleY(0.85);
        crow.getChildren().add(body);

        // Cabeza
        Sphere head = new Sphere(2.8);
        head.setMaterial(materialFactory.getMaterial("crow"));
        head.setTranslateZ(8);
        head.setTranslateY(-1.5);
        crow.getChildren().add(head);

        // Pico
        Box beak = new Box(1.2, 1.2, 3);
        beak.setMaterial(materialFactory.getMaterial("black"));
        beak.setTranslateZ(10.5);
        beak.setTranslateY(-1.2);
        crow.getChildren().add(beak);

        // Ala Izquierda inclinada
        Box wingL = new Box(18, 0.4, 7);
        wingL.setMaterial(materialFactory.getMaterial("crow"));
        wingL.setTranslateX(-9.5);
        wingL.setTranslateY(-2);
        wingL.getTransforms().add(new Rotate(-20, Rotate.Z_AXIS));
        wingL.getTransforms().add(new Rotate(10, Rotate.Y_AXIS));
        crow.getChildren().add(wingL);

        // Ala Derecha
        Box wingR = new Box(18, 0.4, 7);
        wingR.setMaterial(materialFactory.getMaterial("crow"));
        wingR.setTranslateX(9.5);
        wingR.setTranslateY(-2);
        wingR.getTransforms().add(new Rotate(20, Rotate.Z_AXIS));
        wingR.getTransforms().add(new Rotate(-10, Rotate.Y_AXIS));
        crow.getChildren().add(wingR);

        // Plumas de cola
        Box tail = new Box(6, 0.3, 8);
        tail.setMaterial(materialFactory.getMaterial("crow"));
        tail.setTranslateZ(-10);
        tail.setTranslateY(1.0);
        tail.getTransforms().add(new Rotate(12, Rotate.X_AXIS));
        crow.getChildren().add(tail);

        return crow;
    }

    /**
     * Helper método para construir la nube Akatsuki en 3D.
     */
    private Group create3DCloud() {
        Group cloud = new Group();

        Sphere center = new Sphere(5.5);
        center.setMaterial(materialFactory.getMaterial("redCloud"));
        cloud.getChildren().add(center);

        Sphere left = new Sphere(3.8);
        left.setMaterial(materialFactory.getMaterial("redCloud"));
        left.setTranslateX(-5);
        left.setTranslateY(2);
        left.setTranslateZ(0.5);
        cloud.getChildren().add(left);

        Sphere right = new Sphere(4.2);
        right.setMaterial(materialFactory.getMaterial("redCloud"));
        right.setTranslateX(5.5);
        right.setTranslateY(-1);
        right.setTranslateZ(0.5);
        cloud.getChildren().add(right);

        Box waveTail = new Box(8, 2.5, 2.5);
        waveTail.setMaterial(materialFactory.getMaterial("redCloud"));
        waveTail.setTranslateX(8);
        waveTail.setTranslateY(2);
        waveTail.getTransforms().add(new Rotate(-25, Rotate.Z_AXIS));
        cloud.getChildren().add(waveTail);

        return cloud;
    }

    /**
     * Actualiza el bucle físico del sistema de partículas de Chakra y su órbita en espiral.
     */
    public void updateParticles(double time, boolean isChakraActive) {
        double speedMultiplier = isChakraActive ? 2.5 : 1.0;
        double radiusMultiplier = isChakraActive ? 1.8 : 1.0;

        for (int i = 0; i < particleNodes.size(); i++) {
            Sphere p = particleNodes.get(i);
            ParticleData d = particleDataList.get(i);

            // 1. Ascender la partícula
            d.currentY -= d.speedY * speedMultiplier;

            // 2. Si excede el límite superior (cerca de la cabeza/cuervos), restablecer al suelo
            if (d.currentY < -110.0) {
                d.currentY = 45.0; // Reiniciar en la base
                d.baseRadius = 14.0 + Math.random() * 20.0;
                d.angleOffset = Math.random() * 2.0 * Math.PI;
            }

            // 3. Trayectoria espiral trigonométrica
            double angle = time * 2.2 + (d.currentY * 0.04) + d.angleOffset;
            double curRadius = d.baseRadius * (1.0 - (45.0 - d.currentY) / 160.0) * radiusMultiplier;

            p.setTranslateY(d.currentY);
            p.setTranslateX(Math.cos(angle) * curRadius);
            p.setTranslateZ(Math.sin(angle) * curRadius);

            // 4. Suavizar opacidad (Fading individual) en base a su altura
            double progress = (d.currentY - (-110.0)) / (45.0 - (-110.0)); // 0 en tope, 1 en base
            if (progress < 0) progress = 0;
            if (progress > 1) progress = 1;

            Color baseColor = (i % 2 == 0) ? Color.web("#CC1414") : Color.web("#3A0007");
            
            // En modo flare el chakra resplandece en naranja caliente
            if (isChakraActive) {
                baseColor = (i % 2 == 0) ? Color.web("#FF5722") : Color.web("#CC3300");
            }

            Color fadedColor = Color.color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                progress * 0.88 // Multiplicador de opacidad máxima
            );

            ((PhongMaterial) p.getMaterial()).setDiffuseColor(fadedColor);
        }
    }

    /**
     * Actualiza las órbitas de los cuervos con orientación tangencial de aleteo.
     */
    public void updateCrows(double time) {
        double[] speeds = {0.8, -1.0, 1.2};
        double[] radii = {70.0, 52.0, 85.0};
        double[] heights = {-120.0, -85.0, -140.0};
        double[] rotOffsets = {0.0, 180.0, 0.0};

        for (int i = 0; i < crowNodes.size(); i++) {
            Group crow = crowNodes.get(i);
            
            double speed = speeds[i];
            double r = radii[i];
            double h = heights[i];

            // Ángulo circular en radianes
            double angle = time * speed + (i * 2.0 * Math.PI / 3.0);

            // Posición circular
            double x = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;

            crow.setTranslateX(x);
            crow.setTranslateY(h);
            crow.setTranslateZ(z);

            // Aleteo sinusoidal en las alas
            Box wingL = (Box) crow.getChildren().get(3);
            Box wingR = (Box) crow.getChildren().get(4);

            double flap = Math.sin(time * 12.0 + i * 5.0) * 25.0;
            
            wingL.getTransforms().clear();
            wingL.getTransforms().addAll(new Rotate(-20 + flap, Rotate.Z_AXIS), new Rotate(10, Rotate.Y_AXIS));
            
            wingR.getTransforms().clear();
            wingR.getTransforms().addAll(new Rotate(20 - flap, Rotate.Z_AXIS), new Rotate(-10, Rotate.Y_AXIS));

            // Orientación Tangencial (La cabeza apunta hacia donde viaja)
            // La tangente es (-sin(angle), 0, cos(angle)) -> Ángulo de rotación de Yaw
            double angleDegrees = Math.toDegrees(-angle);
            crow.setRotate(angleDegrees + rotOffsets[i] + 90.0);
        }
    }

    /**
     * Dispara instantáneamente el Chakra Flare (Interacción F).
     */
    public void triggerChakraFlare() {
        isFlareActive = true;
        
        // 1. Escalar el aro base
        auraBase.setScaleX(1.8);
        auraBase.setScaleZ(1.8);
        auraBase.setMaterial(materialFactory.getMaterial("glowingAuraActive"));

        // 2. Escalar el aro secundario
        auraOuter.setScaleX(2.0);
        auraOuter.setScaleZ(2.0);
        auraOuter.setMaterial(materialFactory.getMaterial("redCloud"));
    }

    /**
     * Contrae y disipa gradualmente el Chakra Flare de vuelta a los valores por defecto.
     */
    public void shrinkAura(double progress) {
        if (progress <= 0) {
            isFlareActive = false;
            auraBase.setScaleX(1.0);
            auraBase.setScaleZ(1.0);
            auraBase.setMaterial(auraBaseMat);

            auraOuter.setScaleX(1.0);
            auraOuter.setScaleZ(1.0);
            auraOuter.setMaterial(auraOuterMat);
        } else {
            // Interpolación lineal
            double scaleB = 1.0 + (0.8 * progress);
            double scaleO = 1.0 + (1.0 * progress);

            auraBase.setScaleX(scaleB);
            auraBase.setScaleZ(scaleB);

            auraOuter.setScaleX(scaleO);
            auraOuter.setScaleZ(scaleO);

            // Interpolación de color entre rojo profundo y naranja incandescente
            Color auraColor = Color.web("#CC1414").interpolate(Color.web("#FF5722"), progress);
            auraBaseMat.setDiffuseColor(auraColor);
        }
    }

    public Group getParticlesGroup() {
        return particlesGroup;
    }

    public boolean isFlareActive() {
        return isFlareActive;
    }
}
