package org.example.aldeakonoha;

import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * UTILERÍA DE LA ALDEA (VillageProps) 
 * Responsabilidad: Crear y modelar procedimentalmente objetos 3D decorativos, defensivos y de entrenamiento 
 * utilizando primitivas geométricas (Box, Cylinder, Sphere) con jerarquías complejas y transformaciones afines.
 */
public class VillageProps {

    private final MaterialFactory materialFactory;

    public VillageProps(MaterialFactory materialFactory) {
        this.materialFactory = materialFactory;
    }

    /**
     * Construye un árbol detallado con tronco cilíndrico rugoso y follaje asimétrico en capas.
     */
    public Group createTree(double scale) {
        Group tree = new Group();

        double trunkH = 65.0 * scale;
        double trunkR = 5.5 * scale;

        // Tronco estructural
        Cylinder trunk = new Cylinder(trunkR, trunkH, 8);
        trunk.setMaterial(materialFactory.getMaterial("wood"));
        trunk.setTranslateY(-trunkH / 2.0); // Se asienta a nivel de suelo Y = 0
        tree.getChildren().add(trunk);

        // Follaje orgánico (Esferas apiladas)
        double foliageBaseY = -trunkH;

        // Capa inferior grande
        Sphere lowerLeaves = new Sphere(28.0 * scale);
        lowerLeaves.setMaterial(materialFactory.getMaterial("leavesDark"));
        lowerLeaves.setTranslateY(foliageBaseY);
        tree.getChildren().add(lowerLeaves);

        // Capa media
        Sphere middleLeaves = new Sphere(22.0 * scale);
        middleLeaves.setMaterial(materialFactory.getMaterial("leavesLight"));
        middleLeaves.setTranslateY(foliageBaseY - (18.0 * scale));
        middleLeaves.setTranslateX(3.0 * scale);
        tree.getChildren().add(middleLeaves);

        // Capa superior (punta del árbol)
        Sphere topLeaves = new Sphere(16.0 * scale);
        topLeaves.setMaterial(materialFactory.getMaterial("leavesDark"));
        topLeaves.setTranslateY(foliageBaseY - (34.0 * scale));
        tree.getChildren().add(topLeaves);

        // Ramificaciones asimétricas para evitar un aspecto rígido
        Sphere leftLeaves = new Sphere(14.0 * scale);
        leftLeaves.setMaterial(materialFactory.getMaterial("leavesLight"));
        leftLeaves.setTranslateX(-16.0 * scale);
        leftLeaves.setTranslateY(foliageBaseY + (2.0 * scale));
        tree.getChildren().add(leftLeaves);

        Sphere rightLeaves = new Sphere(13.0 * scale);
        rightLeaves.setMaterial(materialFactory.getMaterial("leavesDark"));
        rightLeaves.setTranslateX(16.0 * scale);
        rightLeaves.setTranslateY(foliageBaseY + (4.0 * scale));
        tree.getChildren().add(rightLeaves);

        return tree;
    }

    /**
     * Crea un Farol Japonés Tradicional (Poste de Luz) e inyecta una luz de punto (PointLight)
     * que se registra automáticamente en el LightingManager.
     */
    public Group createStreetTorch(LightingManager lightingManager) {
        Group torch = new Group();

        // 1. Poste de soporte vertical
        Cylinder pole = new Cylinder(2.4, 65, 8);
        pole.setMaterial(materialFactory.getMaterial("wood"));
        pole.setTranslateY(-32.5);
        torch.getChildren().add(pole);

        // 2. Travesaño superior horizontal
        Box crossBar = new Box(24, 3, 3);
        crossBar.setMaterial(materialFactory.getMaterial("wood"));
        crossBar.setTranslateY(-65);
        torch.getChildren().add(crossBar);

        // 3. Soportes angulares oblicuos
        Box supportL = new Box(2, 11, 2);
        supportL.setMaterial(materialFactory.getMaterial("wood"));
        supportL.setTranslateX(-7);
        supportL.setTranslateY(-59);
        supportL.getTransforms().add(new Rotate(45, Rotate.Z_AXIS));

        Box supportR = new Box(2, 11, 2);
        supportR.setMaterial(materialFactory.getMaterial("wood"));
        supportR.setTranslateX(7);
        supportR.setTranslateY(-59);
        supportR.getTransforms().add(new Rotate(-45, Rotate.Z_AXIS));
        torch.getChildren().addAll(supportL, supportR);

        // 4. Farolas colgantes laterales (Carcasa negra de metal + núcleo brillante)
        // Farola Izquierda
        Box lanternL = new Box(6.5, 9.5, 6.5);
        lanternL.setMaterial(materialFactory.getMaterial("black"));
        lanternL.setTranslateX(-10);
        lanternL.setTranslateY(-58);

        Sphere flameL = new Sphere(3.2);
        flameL.setMaterial(materialFactory.getMaterial("glowingFire"));
        flameL.setTranslateX(-10);
        flameL.setTranslateY(-58);
        torch.getChildren().addAll(lanternL, flameL);

        // Farola Derecha
        Box lanternR = new Box(6.5, 9.5, 6.5);
        lanternR.setMaterial(materialFactory.getMaterial("black"));
        lanternR.setTranslateX(10);
        lanternR.setTranslateY(-58);

        Sphere flameR = new Sphere(3.2);
        flameR.setMaterial(materialFactory.getMaterial("glowingFire"));
        flameR.setTranslateX(10);
        flameR.setTranslateY(-58);
        torch.getChildren().addAll(lanternR, flameR);

        // 5. Inyección de Luz de Punto (PointLight)
        // Se coloca al centro geométrico del cabezal de iluminación
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.BLACK); // Por defecto apagada de día
        pointLight.setTranslateY(-58);
        torch.getChildren().add(pointLight);

        // Registrar la luz en el gestor dinámico de iluminación
        if (lightingManager != null) {
            lightingManager.registerStreetLight(pointLight);
        }

        return torch;
    }

    /**
     * Crea una valla de madera defensiva ninja (barricada tradicional).
     */
    public Group createNinjaFence() {
        Group fence = new Group();

        // Pilares de apoyo laterales
        Cylinder postL = new Cylinder(2.5, 32, 8);
        postL.setMaterial(materialFactory.getMaterial("woodLight"));
        postL.setTranslateX(-25);
        postL.setTranslateY(-16);

        Cylinder postR = new Cylinder(2.5, 32, 8);
        postR.setMaterial(materialFactory.getMaterial("woodLight"));
        postR.setTranslateX(25);
        postR.setTranslateY(-16);
        fence.getChildren().addAll(postL, postR);

        // Vigas oblicuas cruzadas de contención en "X"
        Box barX1 = new Box(64, 2.5, 2.5);
        barX1.setMaterial(materialFactory.getMaterial("wood"));
        barX1.setTranslateY(-16);
        barX1.getTransforms().add(new Rotate(16, Rotate.Z_AXIS));

        Box barX2 = new Box(64, 2.5, 2.5);
        barX2.setMaterial(materialFactory.getMaterial("wood"));
        barX2.setTranslateY(-16);
        barX2.getTransforms().add(new Rotate(-16, Rotate.Z_AXIS));
        fence.getChildren().addAll(barX1, barX2);

        // Vigas de unión horizontales
        Box railTop = new Box(55, 2.2, 2.2);
        railTop.setMaterial(materialFactory.getMaterial("woodLight"));
        railTop.setTranslateY(-27);

        Box railBottom = new Box(55, 2.2, 2.2);
        railBottom.setMaterial(materialFactory.getMaterial("woodLight"));
        railBottom.setTranslateY(-7);
        fence.getChildren().addAll(railTop, railBottom);

        return fence;
    }

    /**
     * Crea una roca facetada con formas angulares e irregulares mediante la superposición de primitivas.
     */
    public Group createRock(double scaleX, double scaleY, double scaleZ) {
        Group rock = new Group();
        rock.setScaleX(scaleX);
        rock.setScaleY(scaleY);
        rock.setScaleZ(scaleZ);

        // Núcleo esférico de piedra granito
        Sphere centerRock = new Sphere(16);
        centerRock.setMaterial(materialFactory.getMaterial("stone"));
        centerRock.setTranslateY(-12);
        rock.getChildren().add(centerRock);

        // Faceta angular 1 (Box rotada en X e Y)
        Box facet1 = new Box(22, 16, 22);
        facet1.setMaterial(materialFactory.getMaterial("stone"));
        facet1.setTranslateY(-14);
        facet1.setTranslateX(5);
        facet1.getTransforms().add(new Rotate(30, Rotate.Y_AXIS));
        facet1.getTransforms().add(new Rotate(15, Rotate.X_AXIS));
        rock.getChildren().add(facet1);

        // Faceta angular 2 (Box rotada en Z)
        Box facet2 = new Box(16, 20, 16);
        facet2.setMaterial(materialFactory.getMaterial("stone"));
        facet2.setTranslateY(-10);
        facet2.setTranslateX(-6);
        facet2.setTranslateZ(-4);
        facet2.getTransforms().add(new Rotate(-20, Rotate.Z_AXIS));
        rock.getChildren().add(facet2);

        return rock;
    }

    /**
     * Crea un banco tradicional japonés de madera.
     */
    public Group createBench() {
        Group bench = new Group();

        // Asiento plano
        Box plank = new Box(48, 2.5, 16);
        plank.setMaterial(materialFactory.getMaterial("wood"));
        plank.setTranslateY(-12);
        bench.getChildren().add(plank);

        // Pata Izquierda
        Box legL = new Box(4, 11, 12);
        legL.setMaterial(materialFactory.getMaterial("wood"));
        legL.setTranslateX(-18);
        legL.setTranslateY(-5.5);

        // Pata Derecha
        Box legR = new Box(4, 11, 12);
        legR.setMaterial(materialFactory.getMaterial("wood"));
        legR.setTranslateX(18);
        legR.setTranslateY(-5.5);
        bench.getChildren().addAll(legL, legR);

        return bench;
    }

    /**
     * Construye un espectacular puente de madera arqueado (Arch Bridge) en 3D.
     */
    public Group createArchedBridge() {
        Group bridge = new Group();

        // Rampas arqueadas mediante 3 segmentos de plataformas de madera con diferentes inclinaciones
        // Segmento central plano superior
        Box centerPlatform = new Box(24, 3, 50);
        centerPlatform.setMaterial(materialFactory.getMaterial("wood"));
        centerPlatform.setTranslateY(-14);
        bridge.getChildren().add(centerPlatform);

        // Rampa de subida (delantera)
        Box rampFront = new Box(24, 3, 30);
        rampFront.setMaterial(materialFactory.getMaterial("wood"));
        rampFront.setTranslateY(-8);
        rampFront.setTranslateZ(-35);
        rampFront.getTransforms().add(new Rotate(22, Rotate.X_AXIS));
        bridge.getChildren().add(rampFront);

        // Rampa de bajada (trasera)
        Box rampBack = new Box(24, 3, 30);
        rampBack.setMaterial(materialFactory.getMaterial("wood"));
        rampBack.setTranslateY(-8);
        rampBack.setTranslateZ(35);
        rampBack.getTransforms().add(new Rotate(-22, Rotate.X_AXIS));
        bridge.getChildren().add(rampBack);

        // Barandillas laterales (Postes de viga finos y rieles de baranda arqueados)
        // Lado Izquierdo (X = -13)
        buildBridgeRailing(-13, bridge);

        // Lado Derecho (X = 13)
        buildBridgeRailing(13, bridge);

        return bridge;
    }

    private void buildBridgeRailing(double xOffset, Group parent) {
        // Postes verticales
        double[] zCoords = {-42, -22, 0, 22, 42};
        double[] yCoords = {-4, -11, -14, -11, -4};

        for (int i = 0; i < 5; i++) {
            Cylinder post = new Cylinder(1.2, 18, 6);
            post.setMaterial(materialFactory.getMaterial("woodLight"));
            post.setTranslateX(xOffset);
            post.setTranslateZ(zCoords[i]);
            post.setTranslateY(yCoords[i] - 9); // Elevado sobre la rampa
            parent.getChildren().add(post);
        }

        // Riel horizontal arqueado (hecho de 2 vigas largas segmentadas)
        Box railFront = new Box(1.8, 1.8, 48);
        railFront.setMaterial(materialFactory.getMaterial("woodLight"));
        railFront.setTranslateX(xOffset);
        railFront.setTranslateY(-21);
        railFront.setTranslateZ(-22);
        railFront.getTransforms().add(new Rotate(11, Rotate.X_AXIS));

        Box railBack = new Box(1.8, 1.8, 48);
        railBack.setMaterial(materialFactory.getMaterial("woodLight"));
        railBack.setTranslateX(xOffset);
        railBack.setTranslateY(-21);
        railBack.setTranslateZ(22);
        railBack.getTransforms().add(new Rotate(-11, Rotate.X_AXIS));

        parent.getChildren().addAll(railFront, railBack);
    }

    /**
     * Crea un plano de agua (riachuelo) con material azul altamente especular y brillante.
     */
    public Group createStream(double width, double length) {
        Group stream = new Group();

        // Agua base (Placa muy fina y extendida)
        Box waterPlank = new Box(width, 1.0, length);
        waterPlank.setMaterial(materialFactory.getMaterial("water"));
        waterPlank.setTranslateY(-0.5); // A ras de césped
        stream.getChildren().add(waterPlank);

        return stream;
    }

    /**
     * Crea una caja de entrenamiento ninja (Training Box) tridimensional y detallada.
     */
    public Group createTrainingBox() {
        Group boxGroup = new Group();

        double size = 18.0;

        // Caja principal
        Box box = new Box(size, size, size);
        box.setMaterial(materialFactory.getMaterial("woodLight"));
        box.setTranslateY(-size / 2.0);
        boxGroup.getChildren().add(box);

        // Refuerzos o ribetes de metal oscuro en las esquinas
        Box rimL = new Box(1.5, size + 0.4, size + 0.4);
        rimL.setMaterial(materialFactory.getMaterial("black"));
        rimL.setTranslateX(-size / 2.0);
        rimL.setTranslateY(-size / 2.0);

        Box rimR = new Box(1.5, size + 0.4, size + 0.4);
        rimR.setMaterial(materialFactory.getMaterial("black"));
        rimR.setTranslateX(size / 2.0);
        rimR.setTranslateY(-size / 2.0);

        // Símbolo ninja en relieve al frente de la caja (espiral o triángulo metálico)
        Box badge = new Box(8, 8, 1);
        badge.setMaterial(materialFactory.getMaterial("metalPlate"));
        badge.setTranslateY(-size / 2.0);
        badge.setTranslateZ(size / 2.0 + 0.2);

        boxGroup.getChildren().addAll(rimL, rimR, badge);

        return boxGroup;
    }

    /**
     * Crea un barril ninja clásico en 3D utilizando varios cilindros finos superpuestos.
     */
    public Group createBarrel() {
        Group barrel = new Group();

        double height = 20.0;
        double radius = 8.0;

        // Cuerpo principal del barril (madera oscura)
        Cylinder body = new Cylinder(radius, height, 12);
        body.setMaterial(materialFactory.getMaterial("wood"));
        body.setTranslateY(-height / 2.0);
        barrel.getChildren().add(body);

        // Cinturón de metal superior e inferior (aros negros)
        Cylinder strapTop = new Cylinder(radius + 0.3, 1.5, 12);
        strapTop.setMaterial(materialFactory.getMaterial("black"));
        strapTop.setTranslateY(-height + 4);
        
        Cylinder strapBottom = new Cylinder(radius + 0.3, 1.5, 12);
        strapBottom.setMaterial(materialFactory.getMaterial("black"));
        strapBottom.setTranslateY(-4);

        barrel.getChildren().addAll(strapTop, strapBottom);

        return barrel;
    }

    /**
     * Construye el Gran Portal Rojo de la Aldea (Leaf Gate) a gran escala.
     */
    public Group createLeafGate() {
        Group gate = new Group();

        // Pilares principales rojos (Box gigantes verticales)
        Box pillarL = new Box(18, 180, 18);
        pillarL.setMaterial(materialFactory.getMaterial("roofRed"));
        pillarL.setTranslateX(-90);
        pillarL.setTranslateY(-90);

        Box pillarR = new Box(18, 180, 18);
        pillarR.setMaterial(materialFactory.getMaterial("roofRed"));
        pillarR.setTranslateX(90);
        pillarR.setTranslateY(-90);
        gate.getChildren().addAll(pillarL, pillarR);

        // Vigas de soporte angulares en la base (A-frame)
        Box supportL = new Box(8, 70, 8);
        supportL.setMaterial(materialFactory.getMaterial("roofRed"));
        supportL.setTranslateX(-110);
        supportL.setTranslateY(-32);
        supportL.getTransforms().add(new Rotate(-20, Rotate.Z_AXIS));

        Box supportR = new Box(8, 70, 8);
        supportR.setMaterial(materialFactory.getMaterial("roofRed"));
        supportR.setTranslateX(110);
        supportR.setTranslateY(-32);
        supportR.getTransforms().add(new Rotate(20, Rotate.Z_AXIS));
        gate.getChildren().addAll(supportL, supportR);

        // Dintel/Viga Superior Horizontal (Header)
        Box header = new Box(240, 16, 22);
        header.setMaterial(materialFactory.getMaterial("roofRed"));
        header.setTranslateY(-175);
        gate.getChildren().add(header);

        // Techo del portal (Losa negra/marrón)
        Box roof = new Box(250, 6, 32);
        roof.setMaterial(materialFactory.getMaterial("wood"));
        roof.setTranslateY(-185);
        gate.getChildren().add(roof);

        // Viga transversal inferior
        Box subHeader = new Box(180, 12, 14);
        subHeader.setMaterial(materialFactory.getMaterial("roofRed"));
        subHeader.setTranslateY(-140);
        gate.getChildren().add(subHeader);

        // Emblema Central del Portal: Círculo negro con el símbolo de la hoja en 3D
        Group emblem = new Group();
        emblem.setTranslateY(-140);
        emblem.setTranslateZ(11.5);

        // Marco circular
        Cylinder ring = new Cylinder(16, 2, 16);
        ring.setMaterial(materialFactory.getMaterial("black"));
        ring.getTransforms().add(new Rotate(90, Rotate.X_AXIS)); // Voltear vertical
        emblem.getChildren().add(ring);

        // Fondo del emblema (Blanco)
        Cylinder background = new Cylinder(14, 1.6, 16);
        background.setMaterial(materialFactory.getMaterial("shoji"));
        background.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        emblem.getChildren().add(background);

        // Símbolo de la hoja tridimensional (Geometría verde estilizada en espiral)
        Sphere spiralCenter = new Sphere(5);
        spiralCenter.setMaterial(materialFactory.getMaterial("leavesLight"));
        spiralCenter.setTranslateZ(1.5);
        emblem.getChildren().add(spiralCenter);

        Box spiralLeaf = new Box(14, 4, 3);
        spiralLeaf.setMaterial(materialFactory.getMaterial("leavesLight"));
        spiralLeaf.setTranslateX(5);
        spiralLeaf.setTranslateY(-4);
        spiralLeaf.setTranslateZ(1.5);
        spiralLeaf.getTransforms().add(new Rotate(35, Rotate.Z_AXIS));
        emblem.getChildren().add(spiralLeaf);

        gate.getChildren().add(emblem);

        return gate;
    }
}
