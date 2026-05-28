package org.example.aldeakonoha;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * ESCENARIO DE LA ALDEA DE KONOHA (KonohaVillage) - Proyecto Final de Graficación Computacional.
 * Responsabilidad: Integrar, distribuir y ensamblar de forma coherente todos los componentes arquitectónicos,
 * hitos monumentales, ríos, puentes y props ornamentales del mapa de Konoha.
 * 
 * CONCEPTOS DE EXPOSICIÓN ACADÉMICA:
 * 1. Composición Urbana y Distribución Espacial: Disposición ordenada de casas (Minkas), puestos comerciales
 *    y sedes de poder a lo largo de un sistema de coordenadas cartesianas locales.
 * 2. Jerarquía Modular Compleja: Ensamblado procedimental de macroestructuras (como el Edificio del Hokage
 *    y el Puesto de Ramen) utilizando bucles de repetición geométrica (como las barandillas y banquetas).
 * 3. Coordenadas de Referencia para Colisiones/Interacciones: Exposición de vectores tridimensionales
 *    para que otros subsistemas (como HudManager) calculen distancias euclidianas a puntos de interés.
 */
public class KonohaVillage extends Group {

    private final MaterialFactory materialFactory;
    private final VillageProps villageProps;

    // Nodos de referencia para el cálculo de proximidades
    private Node ramenIchirakuNode;
    private Node hokageOfficeNode;
    private Node monumentNode;

    public KonohaVillage(MaterialFactory materialFactory, VillageProps villageProps) {
        this.materialFactory = materialFactory;
        this.villageProps = villageProps;

        buildBasesAndInfrastructure();
        buildPlazaCentral();
        buildWaterAndBridgeSystem();
        buildHokageOffice();
        buildRamenIchiraku();
        buildNeighborhoods();
        buildHokageMonument();
        buildPerimeterWalls();
        buildPropsScatter();
    }

    /**
     * Construye las sendas principales e instala el Portal de Entrada.
     */
    private void buildBasesAndInfrastructure() {
        // Senda Central (Arena) - Dividida en dos secciones para dar espacio al río
        Box pathFront = new Box(130, 2, 400);
        pathFront.setMaterial(materialFactory.getMaterial("pathSand"));
        pathFront.setTranslateY(-1);
        pathFront.setTranslateZ(-150); // Desde entrada hasta el río
        
        Box pathBack = new Box(130, 2, 350);
        pathBack.setMaterial(materialFactory.getMaterial("pathSand"));
        pathBack.setTranslateY(-1);
        pathBack.setTranslateZ(255); // Desde el río hasta el Hokage Office
        
        getChildren().addAll(pathFront, pathBack);

        // Caminos transversales secundarios
        Box crossPath1 = new Box(750, 1.5, 60);
        crossPath1.setMaterial(materialFactory.getMaterial("pathSand"));
        crossPath1.setTranslateY(-0.8);
        crossPath1.setTranslateZ(-150); // Cruza por la Plaza Central
        
        Box crossPath2 = new Box(650, 1.5, 50);
        crossPath2.setMaterial(materialFactory.getMaterial("pathSand"));
        crossPath2.setTranslateY(-0.8);
        crossPath2.setTranslateZ(200); // Fila trasera residencial
        
        getChildren().addAll(crossPath1, crossPath2);

        // Gran Portal de Konoha (Leaf Gate)
        Group leafGate = villageProps.createLeafGate();
        leafGate.setTranslateX(0);
        leafGate.setTranslateZ(-350); // En el límite delantero de la aldea
        getChildren().add(leafGate);

        // Vallas de madera reforzando los flancos de la puerta
        for (int i = 0; i < 5; i++) {
            Group fenceL = villageProps.createNinjaFence();
            fenceL.setTranslateX(-150 - (i * 52));
            fenceL.setTranslateZ(-350);
            
            Group fenceR = villageProps.createNinjaFence();
            fenceR.setTranslateX(150 + (i * 52));
            fenceR.setTranslateZ(-350);
            getChildren().addAll(fenceL, fenceR);
        }
    }

    /**
     * Ensambla la plaza central adoquinada.
     */
    private void buildPlazaCentral() {
        Cylinder plazaCircle = new Cylinder(85, 2, 16);
        plazaCircle.setMaterial(materialFactory.getMaterial("pathCobble"));
        plazaCircle.setTranslateX(0);
        plazaCircle.setTranslateZ(-150);
        plazaCircle.setTranslateY(-1.2);
        getChildren().add(plazaCircle);

        // Bancos de plaza radiales
        double[][] benchData = {
            {-60, -150, 90},  // Izquierda
            {60, -150, -90},  // Derecha
            {0, -95, 0},      // Frente
            {0, -205, 180}    // Atrás
        };

        for (double[] b : benchData) {
            Group bench = villageProps.createBench();
            bench.setTranslateX(b[0]);
            bench.setTranslateZ(b[1]);
            bench.getTransforms().add(new Rotate(b[2], Rotate.Y_AXIS));
            getChildren().add(bench);
        }
    }

    /**
     * Sistema de agua (río con riachuelo azul brillante) y puente de madera arqueado.
     */
    private void buildWaterAndBridgeSystem() {
        // Riachuelo horizontal que cruza transversalmente toda la aldea
        Group stream = villageProps.createStream(900, 34);
        stream.setTranslateX(0);
        stream.setTranslateZ(80); // Posicionado entre la plaza y la fila trasera
        getChildren().add(stream);

        // Puente Arqueado de madera en la intersección exacta de la senda y el río
        Group bridge = villageProps.createArchedBridge();
        bridge.setTranslateX(0);
        bridge.setTranslateZ(80);
        getChildren().add(bridge);
    }

    /**
     * Construye el Gran Edificio Hokage: cilindro rojo monumental con terrazas y barandillas 3D.
     */
    private void buildHokageOffice() {
        Group office = new Group();
        office.setTranslateX(0);
        office.setTranslateZ(380); // Posicionado al fondo de la aldea
        office.setTranslateY(0);

        PhongMaterial redM = materialFactory.getMaterial("redOffice");
        PhongMaterial greenM = materialFactory.getMaterial("roofGreen");
        PhongMaterial woodM = materialFactory.getMaterial("wood");

        // 1. Base cilíndrica masiva roja
        Cylinder baseCyl = new Cylinder(45, 75, 16);
        baseCyl.setMaterial(redM);
        baseCyl.setTranslateY(-37.5);
        office.getChildren().add(baseCyl);

        // 2. Terraza circular intermedia (Placa sobresaliente marrón)
        Cylinder terrace1 = new Cylinder(49, 3, 16);
        terrace1.setMaterial(woodM);
        terrace1.setTranslateY(-50);
        office.getChildren().add(terrace1);

        // 3. Barandillas de la terraza 1 (Bucle rotatorio de pequeños cilindros)
        int numRails = 32;
        for (int i = 0; i < numRails; i++) {
            double angle = i * (2.0 * Math.PI / numRails);
            Cylinder post = new Cylinder(0.8, 10, 6);
            post.setMaterial(materialFactory.getMaterial("woodLight"));
            post.setTranslateX(Math.cos(angle) * 48);
            post.setTranslateZ(Math.sin(angle) * 48);
            post.setTranslateY(-55);
            office.getChildren().add(post);
        }

        // 4. Segundo piso cilíndrico rojo (más estrecho)
        Cylinder upperCyl = new Cylinder(36, 40, 16);
        upperCyl.setMaterial(redM);
        upperCyl.setTranslateY(-70);
        office.getChildren().add(upperCyl);

        // 5. Tejado Cónico tradicional de color verde
        Cylinder coneRoof = new Cylinder(42, 14, 16);
        coneRoof.setMaterial(greenM);
        coneRoof.setTranslateY(-97);
        office.getChildren().add(coneRoof);

        // Símbolo del Fuego Kan-ji ("Hi") en el frente de la torre Hokage (simulado con planos circulares blancos y rojos)
        Group fireEmblem = new Group();
        fireEmblem.setTranslateZ(-45.2);
        fireEmblem.setTranslateY(-37.5);

        Cylinder emblemCircle = new Cylinder(10, 1.2, 12);
        emblemCircle.setMaterial(materialFactory.getMaterial("shoji"));
        emblemCircle.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        fireEmblem.getChildren().add(emblemCircle);

        // Geometría roja estilizada que simula el trazo Kanji
        Box kanji1 = new Box(2, 12, 1.5);
        kanji1.setMaterial(materialFactory.getMaterial("redCloud"));
        kanji1.setTranslateY(-1);
        
        Box kanji2 = new Box(9, 2, 1.5);
        kanji2.setMaterial(materialFactory.getMaterial("redCloud"));
        kanji2.setTranslateY(2);
        
        fireEmblem.getChildren().addAll(kanji1, kanji2);
        office.getChildren().add(fireEmblem);

        getChildren().add(office);

        // Guardar referencia para proximidad
        this.hokageOfficeNode = office;
    }

    /**
     * Construye el Puesto de Ramen Ichiraku: Barra abierta con taburetes, cortinas y cartel tradicional.
     */
    private void buildRamenIchiraku() {
        Group ramen = new Group();
        ramen.setTranslateX(-130);
        ramen.setTranslateZ(0); // Cerca del camino transversal
        ramen.setTranslateY(0);
        ramen.getTransforms().add(new Rotate(90, Rotate.Y_AXIS)); // Mirando hacia la calle central

        PhongMaterial woodM = materialFactory.getMaterial("wood");
        PhongMaterial whiteM = materialFactory.getMaterial("shoji");
        PhongMaterial bannerM = materialFactory.getMaterial("banners");
        PhongMaterial goldM = materialFactory.getMaterial("gold");

        // 1. Barra de atención (Caja de madera)
        Box bar = new Box(45, 16, 12);
        bar.setMaterial(woodM);
        bar.setTranslateY(-8);
        ramen.getChildren().add(bar);

        // 2. Estante superior de cocina y pilares traseros
        Cylinder pillarL = new Cylinder(1.2, 38, 8);
        pillarL.setMaterial(woodM);
        pillarL.setTranslateX(-21);
        pillarL.setTranslateY(-19);
        pillarL.setTranslateZ(4);

        Cylinder pillarR = new Cylinder(1.2, 38, 8);
        pillarR.setMaterial(woodM);
        pillarR.setTranslateX(21);
        pillarR.setTranslateY(-19);
        pillarR.setTranslateZ(4);
        ramen.getChildren().addAll(pillarL, pillarR);

        // 3. Taburetes 3D (3 Stools circulares)
        double[] stoolX = {-15, 0, 15};
        for (double sx : stoolX) {
            Group stool = new Group();
            stool.setTranslateX(sx);
            stool.setTranslateZ(-10);

            // Pata metálica
            Cylinder leg = new Cylinder(0.8, 10, 8);
            leg.setMaterial(materialFactory.getMaterial("black"));
            leg.setTranslateY(-5);
            stool.getChildren().add(leg);

            // Asiento de madera acolchado
            Cylinder seat = new Cylinder(4.5, 2.0, 8);
            seat.setMaterial(materialFactory.getMaterial("socks")); // Blanco/crema
            seat.setTranslateY(-10);
            stool.getChildren().add(seat);

            ramen.getChildren().add(stool);
        }

        // 4. Cortinas Noren tradicionales (Planos colgados con color blanco y rojo alternado)
        double norenWidth = 9.0;
        for (int i = 0; i < 4; i++) {
            Box curtain = new Box(norenWidth, 10, 0.5);
            curtain.setMaterial(i % 2 == 0 ? whiteM : bannerM);
            curtain.setTranslateX(-13.5 + (i * norenWidth));
            curtain.setTranslateY(-25);
            curtain.setTranslateZ(-4.8);
            curtain.getTransforms().add(new Rotate(6, Rotate.X_AXIS)); // Inclinación ondeante
            ramen.getChildren().add(curtain);
        }

        // Tejadillo rústico inclinado superior
        Box roof = new Box(52, 2.5, 18);
        roof.setMaterial(materialFactory.getMaterial("roofBrown"));
        roof.setTranslateY(-31);
        roof.setTranslateZ(-2);
        roof.getTransforms().add(new Rotate(18, Rotate.X_AXIS));
        ramen.getChildren().add(roof);

        // Cartel/Letrero con letras tradicionales Ichiraku en oro
        Box signBoard = new Box(35, 7, 1);
        signBoard.setMaterial(woodM);
        signBoard.setTranslateY(-35);
        signBoard.setTranslateZ(-5);
        ramen.getChildren().add(signBoard);

        // Letras geométricas doradas en relieve simulan caracteres japoneses
        Box l1 = new Box(4, 4, 1.2);
        l1.setMaterial(goldM);
        l1.setTranslateX(-10);
        l1.setTranslateY(-35);
        l1.setTranslateZ(-5.6);
        l1.getTransforms().add(new Rotate(20, Rotate.Z_AXIS));

        Box l2 = new Box(4, 4, 1.2);
        l2.setMaterial(goldM);
        l2.setTranslateX(0);
        l2.setTranslateY(-35);
        l2.setTranslateZ(-5.6);

        Box l3 = new Box(4, 4, 1.2);
        l3.setMaterial(goldM);
        l3.setTranslateX(10);
        l3.setTranslateY(-35);
        l3.setTranslateZ(-5.6);
        l3.getTransforms().add(new Rotate(-20, Rotate.Z_AXIS));

        ramen.getChildren().addAll(l1, l2, l3);

        getChildren().add(ramen);

        // Guardar referencia para proximidad
        this.ramenIchirakuNode = ramen;
    }

    /**
     * Distribuye residencias tradicionales con variedad de color de tejas y rotaciones.
     */
    private void buildNeighborhoods() {
        PhongMaterial wallM = materialFactory.getMaterial("wall");
        PhongMaterial woodM = materialFactory.getMaterial("wood");
        PhongMaterial shojiM = materialFactory.getMaterial("shoji");

        PhongMaterial redR = materialFactory.getMaterial("roofRed");
        PhongMaterial blueR = materialFactory.getMaterial("roofBlue");
        PhongMaterial brownR = materialFactory.getMaterial("roofBrown");

        // Fila Izquierda Principal (Camino central)
        getChildren().add(createMinkaHouse(-190, -290, 1.25, wallM, redR, woodM, shojiM, 0));
        getChildren().add(createMinkaHouse(-155, -60, 1.0, wallM, blueR, woodM, shojiM, 15));
        getChildren().add(createMinkaHouse(-215, 100, 1.35, wallM, brownR, woodM, shojiM, -10));
        getChildren().add(createMinkaHouse(-170, 270, 1.15, wallM, redR, woodM, shojiM, 5));

        // Fila Derecha Principal
        getChildren().add(createMinkaHouse(190, -260, 1.15, wallM, blueR, woodM, shojiM, -15));
        getChildren().add(createMinkaHouse(165, -30, 1.3, wallM, redR, woodM, shojiM, 10));
        getChildren().add(createMinkaHouse(220, 130, 1.05, wallM, brownR, woodM, shojiM, 5));
        getChildren().add(createMinkaHouse(175, 300, 1.25, wallM, blueR, woodM, shojiM, -5));

        // Vecindarios Laterales (Se bifurcan)
        getChildren().add(createMinkaHouse(-340, -150, 1.1, wallM, brownR, woodM, shojiM, 90));
        getChildren().add(createMinkaHouse(-440, -150, 1.20, wallM, redR, woodM, shojiM, 90));

        getChildren().add(createMinkaHouse(340, -150, 1.15, wallM, blueR, woodM, shojiM, -90));
        getChildren().add(createMinkaHouse(440, -150, 1.25, wallM, redR, woodM, shojiM, -90));
    }

    /**
     * Muro rocoso gigante del fondo y las tres caras abstractas talladas del Monumento Hokage.
     */
    private void buildHokageMonument() {
        // Enorme pared montañosa rocosa trasera
        Box hokageWall = new Box(1200, 360, 130);
        hokageWall.setMaterial(materialFactory.getMaterial("stoneDark"));
        hokageWall.setTranslateY(-175);
        hokageWall.setTranslateZ(550);
        getChildren().add(hokageWall);

        // Primer Rostro (Izquierda) - Primer Hokage (Lacio)
        Group face1 = createAbstractHokageFace("lacio");
        face1.setTranslateX(-160);
        face1.setTranslateY(-210);
        face1.setTranslateZ(480);
        getChildren().add(face1);

        // Segundo Rostro (Centro) - Cuarto Hokage (Spiky)
        Group face2 = createAbstractHokageFace("spiky");
        face2.setTranslateX(0);
        face2.setTranslateY(-220);
        face2.setTranslateZ(480);
        getChildren().add(face2);

        // Tercer Rostro (Derecha) - Tercer Hokage (Sombrero y Barba)
        Group face3 = createAbstractHokageFace("barba");
        face3.setTranslateX(160);
        face3.setTranslateY(-210);
        face3.setTranslateZ(480);
        getChildren().add(face3);

        // Guardar referencia para proximidad
        this.monumentNode = face2; // El centro geométrico
    }

    /**
     * Construye murallas perimetrales semicirculares de piedra para delimitar y enmarcar la aldea.
     */
    private void buildPerimeterWalls() {
        PhongMaterial wallMat = materialFactory.getMaterial("stoneDark");
        
        int wallSegments = 12;
        double radius = 620.0;
        
        for (int i = 0; i <= wallSegments; i++) {
            // Generar arco en la zona trasera (de 30° a 150° para rodear las montañas y flancos)
            double angleDeg = 15.0 + (i * 150.0 / wallSegments);
            double angleRad = Math.toRadians(angleDeg);
            
            double x = Math.cos(angleRad) * radius;
            double z = Math.sin(angleRad) * radius + 150.0; // Desplazamiento hacia el fondo
            
            Box wallSec = new Box(160, 140, 30);
            wallSec.setMaterial(wallMat);
            wallSec.setTranslateX(x);
            wallSec.setTranslateZ(z);
            wallSec.setTranslateY(-70); // Asentado en Y = 0
            
            // Alinear rotación tangente al círculo perimetral
            wallSec.getTransforms().add(new Rotate(-angleDeg + 90.0, Rotate.Y_AXIS));
            
            getChildren().add(wallSec);
        }
    }

    /**
     * Distribuye aleatoriamente elementos ornamentales (Árboles, faroles, rocas, barriles y cajas).
     */
    private void buildPropsScatter() {
        // 1. Bosque periférico y árboles en las veredas
        double[][] treeCoords = {
            {-100, -210, 1.2}, {100, -210, 1.1},
            {-280, 20, 1.4}, {280, 20, 1.3},
            {-300, 220, 1.0}, {300, 220, 1.25},
            {-480, -280, 1.5}, {480, -280, 1.4},
            {-550, -40, 1.35}, {550, -40, 1.45},
            {-120, 140, 1.0}, {120, 140, 0.9}
        };

        for (double[] t : treeCoords) {
            Group tree = villageProps.createTree(t[2]);
            tree.setTranslateX(t[0]);
            tree.setTranslateZ(t[1]);
            getChildren().add(tree);
        }

        // 2. Rocas y peñascos decorativos
        Group rock1 = villageProps.createRock(1.5, 1.2, 1.5);
        rock1.setTranslateX(-95);
        rock1.setTranslateZ(-60);
        
        Group rock2 = villageProps.createRock(1.0, 1.4, 0.9);
        rock2.setTranslateX(95);
        rock2.setTranslateZ(40);

        Group rock3 = villageProps.createRock(2.4, 1.8, 2.0);
        rock3.setTranslateX(-450);
        rock3.setTranslateZ(100);
        rock3.setTranslateY(15); // Sobre colina

        Group rock4 = villageProps.createRock(1.8, 1.8, 1.8);
        rock4.setTranslateX(400);
        rock4.setTranslateZ(-320);
        
        getChildren().addAll(rock1, rock2, rock3, rock4);

        // 3. Postes de faroles tradicionales
        double[][] torchData = {
            {-55, -200}, {55, -200},
            {-55, -100}, {55, -100},
            {-100, -320}, {100, -320},
            {-100, 320}, {100, 320},
            {-300, -180}, {300, -180}
        };

        // Creamos las luces y las inyectamos al root de la aldea
        // La propia factoría las añade al LightingManager si no es nulo
        for (double[] t : torchData) {
            // Nota: El LightingManager se inyectará en la aplicación y se vinculará
            // aquí pasándole la referencia.
            // Para mantener acoplamiento flojo, las farolas se pueden vincular en AldeaKonohaApp
        }

        // 4. Barriles y cajas de entrenamiento esparcidas cerca de las viviendas
        // Zona de entrenamiento izquierda trasera
        Group box1 = villageProps.createTrainingBox();
        box1.setTranslateX(-170);
        box1.setTranslateZ(180);
        
        Group box2 = villageProps.createTrainingBox();
        box2.setTranslateX(-188);
        box2.setTranslateZ(180);
        box2.getTransforms().add(new Rotate(15, Rotate.Y_AXIS));
        
        Group barrel1 = villageProps.createBarrel();
        barrel1.setTranslateX(-178);
        barrel1.setTranslateZ(195);
        
        getChildren().addAll(box1, box2, barrel1);

        // Zona comercial al lado derecho del río
        Group barrel2 = villageProps.createBarrel();
        barrel2.setTranslateX(150);
        barrel2.setTranslateZ(140);
        
        Group barrel3 = villageProps.createBarrel();
        barrel3.setTranslateX(162);
        barrel3.setTranslateZ(135);
        barrel3.getTransforms().add(new Rotate(-20, Rotate.Y_AXIS));
        
        getChildren().addAll(barrel2, barrel3);
    }

    /**
     * Vincula las farolas al gestor de iluminación para su encendido nocturno.
     * Esto se ejecuta una vez inicializado el LightingManager.
     */
    public void linkTorchesToLights(LightingManager lm) {
        double[][] torchData = {
            {-55, -200}, {55, -200},
            {-55, -100}, {55, -100},
            {-100, -320}, {100, -320},
            {-100, 320}, {100, 320},
            {-300, -180}, {300, -180}
        };

        for (double[] t : torchData) {
            Group torch = villageProps.createStreetTorch(lm);
            torch.setTranslateX(t[0]);
            torch.setTranslateZ(t[1]);
            getChildren().add(torch);
        }
    }

    /**
     * Construye una Casa Tradicional Japonesa (Minka) modularmente en 3D.
     */
    private Group createMinkaHouse(double x, double z, double scale, PhongMaterial wallMat, 
                                   PhongMaterial roofMat, PhongMaterial woodMat, PhongMaterial shojiMat, double rotY) {
        Group house = new Group();
        house.setTranslateX(x);
        house.setTranslateZ(z);

        double baseW = 60 * scale;
        double baseH = 45 * scale;
        double baseD = 50 * scale;

        // Paredes
        Box walls = new Box(baseW, baseH, baseD);
        walls.setMaterial(wallMat);
        walls.setTranslateY(-baseH / 2.0);
        house.getChildren().add(walls);

        // Vigas de soporte esquineras
        double pillarOffsetW = (baseW / 2.0) - (2.0 * scale);
        double pillarOffsetD = (baseD / 2.0) - (2.0 * scale);
        double pillarRadius = 2.5 * scale;

        double[][] pillars = {
            {-pillarOffsetW, -pillarOffsetD},
            {pillarOffsetW, -pillarOffsetD},
            {-pillarOffsetW, pillarOffsetD},
            {pillarOffsetW, pillarOffsetD}
        };

        for (double[] coord : pillars) {
            Cylinder pillar = new Cylinder(pillarRadius, baseH, 8);
            pillar.setMaterial(woodMat);
            pillar.setTranslateX(coord[0]);
            pillar.setTranslateZ(coord[1]);
            pillar.setTranslateY(-baseH / 2.0);
            house.getChildren().add(pillar);
        }

        // Puertas y marcos Shoji
        Box shojiL = new Box(12 * scale, 24 * scale, 1);
        shojiL.setMaterial(shojiMat);
        shojiL.setTranslateX(-8 * scale);
        shojiL.setTranslateY(-12 * scale);
        shojiL.setTranslateZ(baseD / 2.0 + 0.5);

        Box shojiR = new Box(12 * scale, 24 * scale, 1);
        shojiR.setMaterial(shojiMat);
        shojiR.setTranslateX(8 * scale);
        shojiR.setTranslateY(-12 * scale);
        shojiR.setTranslateZ(baseD / 2.0 + 0.5);

        Box frameL = new Box(12.5 * scale, 24.5 * scale, 0.4);
        frameL.setMaterial(woodMat);
        frameL.setTranslateX(-8 * scale);
        frameL.setTranslateY(-12 * scale);
        frameL.setTranslateZ(baseD / 2.0 + 0.2);

        Box frameR = new Box(12.5 * scale, 24.5 * scale, 0.4);
        frameR.setMaterial(woodMat);
        frameR.setTranslateX(8 * scale);
        frameR.setTranslateY(-12 * scale);
        frameR.setTranslateZ(baseD / 2.0 + 0.2);

        house.getChildren().addAll(frameL, shojiL, frameR, shojiR);

        // Ventana de rejilla (Incrustación Shoji + marco)
        Box window = new Box(16 * scale, 16 * scale, 1);
        window.setMaterial(shojiMat);
        window.setTranslateX(-baseW / 2.0 - 0.5);
        window.setTranslateY(-26 * scale);
        window.setTranslateZ(0);
        window.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

        Box windowFrame = new Box(16.5 * scale, 16.5 * scale, 0.4);
        windowFrame.setMaterial(woodMat);
        windowFrame.setTranslateX(-baseW / 2.0 - 0.2);
        windowFrame.setTranslateY(-26 * scale);
        windowFrame.setTranslateZ(0);
        windowFrame.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        house.getChildren().addAll(windowFrame, window);

        // Letrero comercial colgante en relieve al costado
        Box signHanger = new Box(12 * scale, 1, 1);
        signHanger.setMaterial(woodMat);
        signHanger.setTranslateX(baseW / 2.0 + 6.0 * scale);
        signHanger.setTranslateY(-30 * scale);
        
        Box bannerSign = new Box(1, 14 * scale, 8 * scale);
        bannerSign.setMaterial(materialFactory.getMaterial("banners"));
        bannerSign.setTranslateX(baseW / 2.0 + 6.0 * scale);
        bannerSign.setTranslateY(-23 * scale);
        house.getChildren().addAll(signHanger, bannerSign);

        // Tejado a dos aguas tradicional
        double roofLength = baseD + (10 * scale);
        double panelWidth = (baseW / 2.0) * 1.25;
        double panelThickness = 4.0 * scale;

        Box leftRoof = new Box(panelWidth, panelThickness, roofLength);
        leftRoof.setMaterial(roofMat);
        leftRoof.setTranslateX(-panelWidth / 2.0 + (3.0 * scale));
        leftRoof.setTranslateY(-baseH - (baseH * 0.18));
        leftRoof.getTransforms().add(new Rotate(26, Rotate.Z_AXIS));
        
        Box rightRoof = new Box(panelWidth, panelThickness, roofLength);
        rightRoof.setMaterial(roofMat);
        rightRoof.setTranslateX(panelWidth / 2.0 - (3.0 * scale));
        rightRoof.setTranslateY(-baseH - (baseH * 0.18));
        rightRoof.getTransforms().add(new Rotate(-26, Rotate.Z_AXIS));

        house.getChildren().addAll(leftRoof, rightRoof);

        if (rotY != 0) {
            house.getTransforms().add(new Rotate(rotY, Rotate.Y_AXIS));
        }

        return house;
    }

    /**
     * Construye un rostro Hokage abstracto en piedra de granito.
     */
    private Group createAbstractHokageFace(String hairType) {
        Group face = new Group();
        PhongMaterial stoneDark = materialFactory.getMaterial("stoneDark");

        // Cabeza base esférica
        Sphere head = new Sphere(32);
        head.setMaterial(stoneDark);
        face.getChildren().add(head);

        // Nariz prominente
        Box nose = new Box(6, 18, 12);
        nose.setMaterial(stoneDark);
        nose.setTranslateZ(26);
        nose.setTranslateY(4);
        face.getChildren().add(nose);

        // Arco superciliar (Cejas marcadas)
        Box brow = new Box(22, 5, 8);
        brow.setMaterial(stoneDark);
        brow.setTranslateZ(28);
        brow.setTranslateY(-8);
        face.getChildren().add(brow);

        // Barbilla
        Box chin = new Box(14, 8, 8);
        chin.setMaterial(stoneDark);
        chin.setTranslateY(24);
        chin.setTranslateZ(18);
        face.getChildren().add(chin);

        // Cabello de piedra tallada según el tipo
        if ("spiky".equals(hairType)) {
            double[][] spikes = {
                {0, -32, 0, 0},
                {-18, -26, 0, -25},
                {18, -26, 0, 25},
                {-30, -10, 0, -45},
                {30, -10, 0, 45},
                {0, -36, 12, 10}
            };
            for (double[] data : spikes) {
                Box spike = new Box(16, 24, 20);
                spike.setMaterial(stoneDark);
                spike.setTranslateX(data[0]);
                spike.setTranslateY(data[1]);
                spike.setTranslateZ(data[2]);
                spike.getTransforms().add(new Rotate(data[3], Rotate.Z_AXIS));
                face.getChildren().add(spike);
            }
        } else if ("lacio".equals(hairType)) {
            Box hairL = new Box(12, 55, 24);
            hairL.setMaterial(stoneDark);
            hairL.setTranslateX(-28);
            hairL.setTranslateY(15);
            face.getChildren().add(hairL);

            Box hairR = new Box(12, 55, 24);
            hairR.setMaterial(stoneDark);
            hairR.setTranslateX(28);
            hairR.setTranslateY(15);
            face.getChildren().add(hairR);

            Box hairTop = new Box(50, 12, 28);
            hairTop.setMaterial(stoneDark);
            hairTop.setTranslateY(-26);
            face.getChildren().add(hairTop);
        } else if ("barba".equals(hairType)) {
            Box hatBase = new Box(80, 8, 45);
            hatBase.setMaterial(stoneDark);
            hatBase.setTranslateY(-28);
            hatBase.setTranslateZ(8);
            hatBase.getTransforms().add(new Rotate(-10, Rotate.X_AXIS));
            face.getChildren().add(hatBase);

            Sphere hatCone = new Sphere(32);
            hatCone.setMaterial(stoneDark);
            hatCone.setTranslateY(-45);
            hatCone.setScaleY(0.7);
            hatCone.setScaleX(1.1);
            hatCone.setScaleZ(0.8);
            face.getChildren().add(hatCone);

            Box beard = new Box(12, 20, 12);
            beard.setMaterial(stoneDark);
            beard.setTranslateY(32);
            beard.setTranslateZ(15);
            beard.getTransforms().add(new Rotate(-20, Rotate.X_AXIS));
            face.getChildren().add(beard);
        }

        return face;
    }

    public Node getRamenIchirakuNode() {
        return ramenIchirakuNode;
    }

    public Node getHokageOfficeNode() {
        return hokageOfficeNode;
    }

    public Node getMonumentNode() {
        return monumentNode;
    }
}
