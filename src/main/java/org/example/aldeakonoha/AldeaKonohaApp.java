package org.example.aldeakonoha;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * APLICACIÓN PRINCIPAL (AldeaKonohaApp) - Proyecto Final de Graficación Computacional.
 * Responsabilidad: Orquestar, instanciar e integrar todos los subsistemas lógicos y visuales
 * (3D SubScene, 2D Glassmorphic HUD, Audio, Luces, Cámara, Bucle de Animación y Eventos).
 * Actúa como la raíz y cargador principal del ciclo de vida de la aplicación.
 * 
 * CONCEPTOS DE EXPOSICIÓN ACADÉMICA:
 * 1. Arquitectura de Acoplamiento Débil (Loose Coupling): Coordinación de responsabilidades mediante
 *    clases especializadas independientes, facilitando el mantenimiento y cumplimiento de principios SOLID.
 * 2. Composición de SubScenes: Aislamiento del espacio tridimensional (3D SubScene con cámara de perspectiva
 *    y suavizado antialiasing de hardware) superpuesto sobre un contenedor StackPane 2D que actúa como
 *    cielo degradado y HUD ortogonal.
 * 3. Ciclo de Vida del Software: Control de inicialización (.start) y apagado limpio de hilos paralelos
 *    (hilos de renderizado continuo de AnimationTimer e hilos de decodificación de audio de MediaPlayer).
 */
public class AldeaKonohaApp extends Application {

    private AudioManager audioManager;
    private AnimationManager animationManager;

    @Override
    public void start(Stage stage) {
        // --- 1. INICIALIZACIÓN DE COMPONENTES CORE ---
        MaterialFactory materialFactory = new MaterialFactory();
        VillageProps villageProps = new VillageProps(materialFactory);
        EnvironmentBuilder environmentBuilder = new EnvironmentBuilder(materialFactory);
        LightingManager lightingManager = new LightingManager();

        // --- 2. CONFIGURACIÓN DEL GRAFO DE ESCENA 3D ---
        Group worldRoot = new Group();

        // Terreno ondulado con colinas
        worldRoot.getChildren().add(environmentBuilder.createGroundWithHills());

        // Cordilleras montañosas en capas de profundidad (Parallax)
        worldRoot.getChildren().add(environmentBuilder.createMountainRanges());

        // Nubes volumétricas 3D en el cielo
        Group cloudsGroup = environmentBuilder.createSkyClouds();
        worldRoot.getChildren().add(cloudsGroup);

        // Construir e integrar el escenario de la Aldea de Konoha
        KonohaVillage village = new KonohaVillage(materialFactory, villageProps);
        worldRoot.getChildren().add(village);

        // Vincular e instanciar dinámicamente las antorchas con sus PointLights al LightingManager
        village.linkTorchesToLights(lightingManager);

        // Construir e integrar al personaje Itachi Uchiha al centro de la Plaza Circular
        ItachiModel itachi = new ItachiModel(materialFactory);
        itachi.setScaleX(1.3);
        itachi.setScaleY(1.3);
        itachi.setScaleZ(1.3);
        
        // Posicionar a Itachi en el centro exacto de la plaza de adoquines (X = 0, Z = -150)
        itachi.setTranslateX(0);
        itachi.setTranslateY(-48); // De pie sobre el adoquín de plaza
        itachi.setTranslateZ(-150);
        
        // Hacer que Itachi mire de frente a la cámara (rotación de 180 grados sobre el eje Y)
        itachi.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));
        worldRoot.getChildren().add(itachi);

        // --- 3. INYECCIÓN DE SISTEMA DE ILUMINACIÓN GLOBAL ---
        worldRoot.getChildren().add(lightingManager.getAmbientLight());
        worldRoot.getChildren().add(lightingManager.getSunLight());

        // --- 4. CONFIGURACIÓN DE CÁMARA DE EXPLORACIÓN DE PERSPECTIVA Y SU PIVOTE ---
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(4000.0);
        camera.setFieldOfView(45); // Apertura óptica natural cinematográfica

        Group cameraPivot = new Group();
        cameraPivot.getChildren().add(camera);
        worldRoot.getChildren().add(cameraPivot);

        // --- 5. LIENZO 3D (SUBSCENE) CON SUAVIZADO ANTIALIASING ---
        SubScene subScene3D = new SubScene(worldRoot, 1024, 768, true, SceneAntialiasing.BALANCED);
        subScene3D.setCamera(camera);
        subScene3D.setFill(Color.TRANSPARENT); // Transparente para traslucir el cielo 2D degradado del fondo

        // Ajustar el lienzo cuando se redimensione la ventana
        subScene3D.widthProperty().bind(stage.widthProperty());
        subScene3D.heightProperty().bind(stage.heightProperty());

        // --- 6. CONTENEDOR RAÍZ CON DEGRADADO DE FONDO (CIELO) ---
        StackPane mainRoot = new StackPane();
        
        // Inicializar el degradado en Modo Atardecer (Sunset inicial)
        environmentBuilder.updateSkyColor(mainRoot, "SUNSET");

        // Capas ortogonales: primero el render 3D, encima el HUD 2D
        mainRoot.getChildren().add(subScene3D);

        Scene scene = new Scene(mainRoot, 1024, 768);

        // --- 7. VINCULACIÓN E INSTANCIACIÓN DE GESTORES DE LOGICA Y COMPORTAMIENTO (MANAGERS) ---
        // 7.1. Cargar el HUD 2D
        HudManager hudManager = new HudManager(mainRoot);

        // 7.2. Configurar el controlador de cámara y mouse
        CameraController cameraController = new CameraController(camera, cameraPivot);
        cameraController.registerMouseControls(scene);

        // 7.3. Inicializar el decodificador de sonido resiliente
        audioManager = new AudioManager();
        audioManager.play();

        // 7.4. Levantar el loop principal de simulación física a 60 FPS (AnimationManager)
        animationManager = new AnimationManager(
            itachi, cameraController, hudManager, village, lightingManager, cloudsGroup
        );
        animationManager.start();

        // 7.5. Configurar el despachador de teclado (InteractionManager)
        InteractionManager interactionManager = new InteractionManager(
            cameraController, lightingManager, audioManager, hudManager, animationManager, environmentBuilder, mainRoot
        );
        interactionManager.registerKeyHandlers(scene);

        // 7.6. Forzar primer render del HUD informativo
        hudManager.updateHUD(
            cameraController.getCameraMode(),
            "SUNSET",
            audioManager.isPlaying(),
            audioManager.isAudioAvailable()
        );

        // Imprimir bitácora técnica descriptiva en la consola ninja
        printExpositionIntro();

        // --- 8. DESPLIEGUE FINAL ---
        stage.setTitle("⛩️ Project Konoha 3D // Final Graphics");
        stage.setScene(scene);

        // Asegurar cierre limpio de hilos al salir
        stage.setOnCloseRequest(event -> {
            animationManager.stop();
            audioManager.stop();
            System.out.println("⛩️ ¡Que el camino del ninja te acompañe en tu exposición final!");
        });

        stage.show();
    }

    /**
     * Imprime en la consola del desarrollador un resumen teórico para facilitar
     * la exposición del alumno ante el jurado calificador de la universidad.
     */
    private void printExpositionIntro() {
        System.out.println("\n=========================================================================");
        System.out.println("⛩️ SIMULACIÓN 3D ALDEA DE KONOHA - SISTEMA OPERATIVO INICIADO");
        System.out.println("=========================================================================");
        System.out.println("GUÍA METODOLÓGICA DE LA EXPOSICIÓN (TEMAS A RESALTAR AL DOCENTE):");
        System.out.println("1. COMPOSICIÓN GEOMÉTRICA DE ALTA COMPLEJIDAD:");
        System.out.println("   - Modelado procedural 100% mediante primitivas JavaFX 3D sin assets GLTF.");
        System.out.println("   - Puesto Ramen Ichiraku detallado con banquetas, barra y cortinas Noren.");
        System.out.println("   - Edificio Hokage cilíndrico con terrazas, barandillas circulares y emblema.");
        System.out.println("   - Río azul especular cruzado por un puente de madera arqueado (Arch Bridge).");
        System.out.println("2. PERSONAJE DINÁMICO (ITACHI UCHIHA):");
        System.out.println("   - Capa con nubes 3D en relieve,Sharingans activos y ponytail de cabello.");
        System.out.println("   - Sistema de Partículas (Chakra Aura): Bucle Y con desvanecimiento alfa.");
        System.out.println("   - Cuervos ninja con aleteo sinusoidal y orientación tangencial de órbita.");
        System.out.println("3. SISTEMA DE ENTRADA ANTI-LAG (GAME INPUT SYSTEM):");
        System.out.println("   - Desacoplamiento de eventos por booleanos para movimiento libre fluido 60 FPS.");
        System.out.println("4. CICLOS DINÁMICOS DE TIEMPO (DAY / SUNSET / NIGHT):");
        System.out.println("   - Cambio en tiempo real de gradiente de cielo y balance lumínico.");
        System.out.println("   - Encendido automático nocturno de PointLights en farolas con titileo.");
        System.out.println("5. INTERACTIVIDAD DE PROXIMIDAD Y EVENTOS (HUD + KEY F):");
        System.out.println("   - Alerta flotante 2D al entrar en radios de colisión euclidiana.");
        System.out.println("   - Botón F: Desata un Chakra Flare (Aura Itachi se escala 1.8x, cambia");
        System.out.println("     su material a incandescente y despliega diálogos con frases del personaje).");
        System.out.println("6. ARQUITECTURA ROBUSTA Y LIMPIA:");
        System.out.println("   - División estricta en 12 clases modulares y fallbacks de audio resilientes.");
        System.out.println("=========================================================================\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
