package org.example.aldeakonoha;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * GESTOR DE ENTRADAS E INTERACCIONES (InteractionManager)
 * Responsabilidad: Escuchar, mapear y despachar todos los eventos globales de teclado en la escena.
 * Centraliza la lógica de control para el movimiento continuo, alternado de iluminación,
 * reproducción de sonido, interactividad ninja y ocultado de interfaz.
 */
public class InteractionManager {

    private final CameraController cameraController;
    private final LightingManager lightingManager;
    private final AudioManager audioManager;
    private final HudManager hudManager;
    private final AnimationManager animationManager;
    private final EnvironmentBuilder environmentBuilder;
    private final StackPane mainRoot;

    // Estado del ciclo de tiempo
    private final String[] timeModes = {"DAY", "SUNSET", "NIGHT"};
    private int activeTimeIndex = 1; // Por defecto Sunset ninja

    public InteractionManager(CameraController cameraController, LightingManager lightingManager,
                              AudioManager audioManager, HudManager hudManager,
                              AnimationManager animationManager, EnvironmentBuilder environmentBuilder,
                              StackPane mainRoot) {
        this.cameraController = cameraController;
        this.lightingManager = lightingManager;
        this.audioManager = audioManager;
        this.hudManager = hudManager;
        this.animationManager = animationManager;
        this.environmentBuilder = environmentBuilder;
        this.mainRoot = mainRoot;
    }

    /**
     * Registra los KeyPressed y KeyReleased en la escena para anular el retardo de teclado del OS.
     */
    public void registerKeyHandlers(Scene scene) {
        // --- 1. DETECCIÓN DE PRESIÓN (KEY PRESSED) ---
        scene.setOnKeyPressed((KeyEvent ke) -> {
            KeyCode code = ke.getCode();

            // Movimiento relativo libre
            if (code == KeyCode.W || code == KeyCode.UP) cameraController.setForward(true);
            if (code == KeyCode.S || code == KeyCode.DOWN) cameraController.setBackward(true);
            if (code == KeyCode.A || code == KeyCode.LEFT) cameraController.setLeft(true);
            if (code == KeyCode.D || code == KeyCode.RIGHT) cameraController.setRight(true);
            if (code == KeyCode.Q) cameraController.setUp(true);
            if (code == KeyCode.E) cameraController.setDown(true);
            if (code == KeyCode.SHIFT) cameraController.setSprint(true);

            // Comandos Directos Ninja
            switch (code) {
                case R:
                    // Restablecer cámara a foco original
                    if (!"EXPO".equals(cameraController.getCameraMode())) {
                        cameraController.resetCamera();
                    }
                    break;

                case SPACE:
                    // Vuelo rápido a ojo de pájaro
                    if (!"EXPO".equals(cameraController.getCameraMode())) {
                        cameraController.flyToBirdsEye();
                    }
                    break;

                case H:
                    // Ocultar / Mostrar HUD guía rápida
                    hudManager.toggleHelp();
                    break;

                case M:
                    // Alternar música de fondo (Pause/Resume)
                    audioManager.pauseResume();
                    updateHUDPanel();
                    break;

                case L:
                case T:
                    // Rotar ciclo de iluminación y color de cielo (DÍA -> SUNSET -> NOCHE)
                    activeTimeIndex = (activeTimeIndex + 1) % timeModes.length;
                    String newTimeMode = timeModes[activeTimeIndex];
                    
                    lightingManager.setCycle(newTimeMode);
                    environmentBuilder.updateSkyColor(mainRoot, newTimeMode);
                    updateHUDPanel();
                    break;

                case C:
                    // Alternar entre control libre (FREE) y recorrido automático (EXPO)
                    String currentMode = cameraController.getCameraMode();
                    String newMode = "FREE".equals(currentMode) ? "EXPO" : "FREE";
                    cameraController.setCameraMode(newMode);
                    updateHUDPanel();
                    break;

                case F:
                    // Interactuar con Itachi (Chakra Flare + Frase)
                    if (hudManager.isNearItachi()) {
                        animationManager.startChakraFlare();
                        hudManager.triggerItachiDialogue();
                    }
                    break;

                default:
                    break;
            }
        });

        // --- 2. DETECCIÓN DE LIBERACIÓN (KEY RELEASED) ---
        scene.setOnKeyReleased((KeyEvent ke) -> {
            KeyCode code = ke.getCode();

            if (code == KeyCode.W || code == KeyCode.UP) cameraController.setForward(false);
            if (code == KeyCode.S || code == KeyCode.DOWN) cameraController.setBackward(false);
            if (code == KeyCode.A || code == KeyCode.LEFT) cameraController.setLeft(false);
            if (code == KeyCode.D || code == KeyCode.RIGHT) cameraController.setRight(false);
            if (code == KeyCode.Q) cameraController.setUp(false);
            if (code == KeyCode.E) cameraController.setDown(false);
            if (code == KeyCode.SHIFT) cameraController.setSprint(false);
        });
    }

    /**
     * Helper para despachar la actualización al HUD central.
     */
    private void updateHUDPanel() {
        hudManager.updateHUD(
            cameraController.getCameraMode(),
            timeModes[activeTimeIndex],
            audioManager.isPlaying(),
            audioManager.isAudioAvailable()
        );
    }
}
