package org.example.aldeakonoha;

import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * CONSTRUCTOR DEL ENTORNO (EnvironmentBuilder) - Proyecto Final de Graficación Computacional.
 * Responsabilidad: Diseñar el relieve geográfico, relieve montañoso del fondo y los elementos
 * atmosféricos (cielo, nubes 3D y colinas).
 * 
 * CONCEPTOS DE EXPOSICIÓN ACADÉMICA:
 * 1. Efecto Parallax en 3D: Disponer cordilleras en rangos progresivos (capas de profundidad Z)
 *    para dar una sensación cinematográfica de escala infinita en el horizonte.
 * 2. Relieve Orgánico: Romper la planicie matemática mediante colinas hechas de domos y esferas
 *    achatadas y estiradas, simulando colinas naturales en los linderos de la aldea.
 * 3. Gradientes Cromáticos en la Iluminación del Cielo: Uso de LinearGradients interactivos
 *    para simular fotogramas de transición atmosférica diurna, crepuscular o nocturna.
 */
public class EnvironmentBuilder {

    private final MaterialFactory materialFactory;

    public EnvironmentBuilder(MaterialFactory materialFactory) {
        this.materialFactory = materialFactory;
    }

    /**
     * Crea la base del terreno incorporando colinas y montículos ondulados de césped.
     */
    public Group createGroundWithHills() {
        Group groundGroup = new Group();

        // Terreno base plano principal
        Box groundBase = new Box(2400, 10, 2400);
        groundBase.setMaterial(materialFactory.getMaterial("groundGrass"));
        groundBase.setTranslateY(5); // Queda alineado a Y = 0
        groundGroup.getChildren().add(groundBase);

        // --- COLINAS PERIMETRALES (DOMOS Y PLATAFORMAS ACHATADAS) ---
        // Colina Izquierda delantera
        Sphere hill1 = new Sphere(180);
        hill1.setMaterial(materialFactory.getMaterial("groundHill"));
        hill1.setTranslateX(-500);
        hill1.setTranslateZ(-350);
        hill1.setTranslateY(12); // Enterrada parcialmente
        hill1.setScaleY(0.12);   // Aplanar
        hill1.setScaleX(1.8);    // Estirar en X
        hill1.setScaleZ(1.3);    // Estirar en Z
        groundGroup.getChildren().add(hill1);

        // Colina Izquierda trasera (mayor elevación)
        Box hill2 = new Box(550, 80, 550);
        hill2.setMaterial(materialFactory.getMaterial("groundHill"));
        hill2.setTranslateX(-600);
        hill2.setTranslateZ(150);
        hill2.setTranslateY(30); // Sobresale como una meseta verde
        hill2.getTransforms().add(new Rotate(12, Rotate.Y_AXIS));
        groundGroup.getChildren().add(hill2);

        // Colina Derecha delantera
        Sphere hill3 = new Sphere(160);
        hill3.setMaterial(materialFactory.getMaterial("groundHill"));
        hill3.setTranslateX(520);
        hill3.setTranslateZ(-250);
        hill3.setTranslateY(10);
        hill3.setScaleY(0.15);
        hill3.setScaleX(1.5);
        hill3.setScaleZ(1.5);
        groundGroup.getChildren().add(hill3);

        // Colina Derecha trasera (detrás de las casas de la derecha)
        Sphere hill4 = new Sphere(220);
        hill4.setMaterial(materialFactory.getMaterial("groundHill"));
        hill4.setTranslateX(550);
        hill4.setTranslateZ(200);
        hill4.setTranslateY(15);
        hill4.setScaleY(0.14);
        hill4.setScaleX(1.6);
        hill4.setScaleZ(2.2);
        groundGroup.getChildren().add(hill4);

        // Pequeño montículo en la plaza central
        Sphere centerHill = new Sphere(85);
        centerHill.setMaterial(materialFactory.getMaterial("groundHill"));
        centerHill.setTranslateX(-240);
        centerHill.setTranslateZ(-50);
        centerHill.setTranslateY(2);
        centerHill.setScaleY(0.06);
        centerHill.setScaleX(1.2);
        groundGroup.getChildren().add(centerHill);

        return groundGroup;
    }

    /**
     * Crea cordilleras montañosas en tres capas de profundidad (Parallax natural).
     */
    public Group createMountainRanges() {
        Group mountains = new Group();

        // --- CAPA 1: MONTAÑAS CERCANAS (Enmarcan el Hokage Monument) ---
        Sphere mountNearL = new Sphere(250);
        mountNearL.setMaterial(materialFactory.getMaterial("stone"));
        mountNearL.setTranslateX(-530);
        mountNearL.setTranslateZ(500);
        mountNearL.setTranslateY(-95);
        mountNearL.setScaleY(1.75);
        mountNearL.setScaleX(1.35);
        mountsLayerSetup(mountNearL, mountains);

        Sphere mountNearR = new Sphere(230);
        mountNearR.setMaterial(materialFactory.getMaterial("stone"));
        mountNearR.setTranslateX(530);
        mountNearR.setTranslateZ(500);
        mountNearR.setTranslateY(-85);
        mountNearR.setScaleY(1.65);
        mountNearR.setScaleX(1.35);
        mountsLayerSetup(mountNearR, mountains);

        // --- CAPA 2: CAPA MEDIA (Montañas más grandes detrás) ---
        Sphere mountMidL = new Sphere(330);
        mountMidL.setMaterial(materialFactory.getMaterial("stoneDark"));
        mountMidL.setTranslateX(-760);
        mountMidL.setTranslateZ(660);
        mountMidL.setTranslateY(-145);
        mountMidL.setScaleY(1.85);
        mountMidL.setScaleX(1.55);
        mountsLayerSetup(mountMidL, mountains);

        Sphere mountMidR = new Sphere(310);
        mountMidR.setMaterial(materialFactory.getMaterial("stoneDark"));
        mountMidR.setTranslateX(760);
        mountMidR.setTranslateZ(660);
        mountMidR.setTranslateY(-135);
        mountMidR.setScaleY(1.95);
        mountMidR.setScaleX(1.45);
        mountsLayerSetup(mountMidR, mountains);

        // --- CAPA 3: CAPA LEJANA (Gigantescas en el horizonte de fondo) ---
        Sphere mountFarC = new Sphere(420);
        mountFarC.setMaterial(materialFactory.getMaterial("stoneDark"));
        mountFarC.setTranslateX(160);
        mountFarC.setTranslateZ(890);
        mountFarC.setTranslateY(-230);
        mountFarC.setScaleY(2.35);
        mountFarC.setScaleX(2.65);
        mountsLayerSetup(mountFarC, mountains);

        Sphere mountFarL = new Sphere(380);
        mountFarL.setMaterial(materialFactory.getMaterial("stone"));
        mountFarL.setTranslateX(-360);
        mountFarL.setTranslateZ(860);
        mountFarL.setTranslateY(-210);
        mountFarL.setScaleY(2.2);
        mountFarL.setScaleX(2.3);
        mountsLayerSetup(mountFarL, mountains);

        return mountains;
    }

    private void mountsLayerSetup(Sphere m, Group parent) {
        parent.getChildren().add(m);
    }

    /**
     * Construye un conjunto de nubes volumétricas 3D flotando alto en el cielo ninja.
     */
    public Group createSkyClouds() {
        Group clouds = new Group();

        // Nube 1 (Izquierda trasera)
        Group cloud1 = create3DCloud(1.9);
        cloud1.setTranslateX(-480);
        cloud1.setTranslateZ(380);
        cloud1.setTranslateY(-350);
        clouds.getChildren().add(cloud1);

        // Nube 2 (Derecha delantera)
        Group cloud2 = create3DCloud(1.5);
        cloud2.setTranslateX(400);
        cloud2.setTranslateZ(-120);
        cloud2.setTranslateY(-320);
        clouds.getChildren().add(cloud2);

        // Nube 3 (Central lejana)
        Group cloud3 = create3DCloud(2.3);
        cloud3.setTranslateX(120);
        cloud3.setTranslateZ(650);
        cloud3.setTranslateY(-420);
        clouds.getChildren().add(cloud3);

        return clouds;
    }

    /**
     * Helper método para crear una nube 3D volumétrica acumulando esferas blancas.
     */
    private Group create3DCloud(double scale) {
        Group cloud = new Group();
        cloud.setScaleX(scale);
        cloud.setScaleY(scale);
        cloud.setScaleZ(scale);

        PhongMaterial cloudMat = new PhongMaterial();
        cloudMat.setDiffuseColor(Color.web("#E5ECF4")); // Blanco azulado nuboso suave
        cloudMat.setSpecularColor(Color.WHITE);

        // Esfera central grande
        Sphere c1 = new Sphere(26);
        c1.setMaterial(cloudMat);
        cloud.getChildren().add(c1);

        // Esfera izquierda
        Sphere c2 = new Sphere(19);
        c2.setMaterial(cloudMat);
        c2.setTranslateX(-21);
        c2.setTranslateY(4);
        c2.setTranslateZ(3);
        cloud.getChildren().add(c2);

        // Esfera derecha
        Sphere c3 = new Sphere(20);
        c3.setMaterial(cloudMat);
        c3.setTranslateX(21);
        c3.setTranslateY(3);
        c3.setTranslateZ(2);
        cloud.getChildren().add(c3);

        // Esferas laterales menores para estilizar
        Sphere c4 = new Sphere(14);
        c4.setMaterial(cloudMat);
        c4.setTranslateX(-33);
        c4.setTranslateY(8);
        cloud.getChildren().add(c4);

        Sphere c5 = new Sphere(13);
        c5.setMaterial(cloudMat);
        c5.setTranslateX(33);
        c5.setTranslateY(8);
        cloud.getChildren().add(c5);

        return cloud;
    }

    /**
     * Actualiza el degradado de fondo del cielo en la interfaz 2D (StackPane)
     * para coincidir perfectamente con la hora del ciclo de iluminación.
     */
    public void updateSkyColor(StackPane mainRoot, String timeMode) {
        if (mainRoot == null) return;

        LinearGradient skyGradient;

        switch (timeMode) {
            case "DAY":
                // Cielo diurno brillante y limpio
                skyGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#4A90E2")),  // Azul celestial intenso
                    new Stop(0.6, Color.web("#87CEEB")), // Azul cielo despejado
                    new Stop(1, Color.web("#E0F7FA"))   // Horizonte claro con neblina solar
                );
                break;

            case "SUNSET":
                // Horizonte cálido de atardecer crepuscular (Original premium)
                skyGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#15112B")),  // Azul noche
                    new Stop(0.5, Color.web("#3A1A45")), // Morado crepúsculo ninja
                    new Stop(1, Color.web("#B85D3B"))   // Horizonte naranja encendido
                );
                break;

            case "NIGHT":
                // Noche profunda mística con gradiente oscuro-lavanda
                skyGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#060614")),  // Espacio exterior profundo
                    new Stop(0.5, Color.web("#0A0B22")), // Penumbra azul marino
                    new Stop(1, Color.web("#1F1535"))   // Horizonte con brillo de la luna lavanda
                );
                break;

            default:
                skyGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#15112B")),
                    new Stop(1, Color.web("#B85D3B"))
                );
                break;
        }

        mainRoot.setBackground(new Background(new BackgroundFill(skyGradient, null, null)));
    }
}
