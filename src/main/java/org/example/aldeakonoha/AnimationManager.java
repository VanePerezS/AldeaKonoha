package org.example.aldeakonoha;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;

/**
 * GESTOR DE ANIMACIÓN GLOBAL (AnimationManager)
 * Responsabilidad: Orquestar todas las simulaciones temporales continuas y físicas en el Grafo de Escena.
 * Corre a ~60 FPS utilizando la API de refresco de pantalla del sistema (AnimationTimer).
 */
public class AnimationManager extends AnimationTimer {

    private final ItachiModel itachi;
    private final CameraController cameraController;
    private final HudManager hudManager;
    private final KonohaVillage village;
    private final LightingManager lightingManager;
    private final Group cloudsGroup;

    // Control del temporizador de interacción (Chakra Flare)
    private double flareTimeLeft = 0.0;
    private double lastNanoTime = 0.0;

    public AnimationManager(ItachiModel itachi, CameraController cameraController, 
                            HudManager hudManager, KonohaVillage village, 
                            LightingManager lightingManager, Group cloudsGroup) {
        this.itachi = itachi;
        this.cameraController = cameraController;
        this.hudManager = hudManager;
        this.village = village;
        this.lightingManager = lightingManager;
        this.cloudsGroup = cloudsGroup;
    }

    @Override
    public void handle(long now) {
        if (lastNanoTime == 0.0) {
            lastNanoTime = now;
            return;
        }

        // 1. Calcular delta-tiempo exacto
        double elapsedSeconds = now / 1_000_000_000.0;
        double deltaTime = (now - lastNanoTime) / 1_000_000_000.0;
        lastNanoTime = now;

        // Limitar deltatime ante caídas de fotogramas masivas
        if (deltaTime > 0.05) deltaTime = 0.05;

        // 2. Actualizar controlador de cámara (traslaciones y giros automáticos de grúa)
        cameraController.update(elapsedSeconds);

        // 3. Actualizar sistema de partículas y órbitas en Itachi
        itachi.updateParticles(elapsedSeconds, itachi.isFlareActive());
        itachi.updateCrows(elapsedSeconds);

        // 4. Detección de proximidades de cámara y renderizado de alertas en HUD
        double camX = cameraController.getCameraPivot().getTranslateX();
        double camZ = cameraController.getCameraPivot().getTranslateZ();
        hudManager.checkProximities(camX, camZ, village);

        // 5. Animación de Nubes Volumétricas 3D (Desplazamiento horizontal continuo con wrap)
        if (cloudsGroup != null) {
            for (Node cloud : cloudsGroup.getChildren()) {
                cloud.setTranslateX(cloud.getTranslateX() + 0.35);
                if (cloud.getTranslateX() > 900) {
                    cloud.setTranslateX(-900); // Re-entrar por el flanco opuesto
                }
            }
        }

        // 6. Titileo/Pulsado de Farolas de Noche (PointLights)
        double pulse = Math.sin(elapsedSeconds * 12.0) * 0.15 + 0.85; // Modulación entre 0.7 y 1.0
        Color pulseColor = Color.web("#FFB732").interpolate(Color.web("#FF8C00"), pulse);
        for (PointLight pl : lightingManager.getStreetLights()) {
            if (!pl.getColor().equals(Color.BLACK)) {
                pl.setColor(pulseColor);
            }
        }

        // 7. Interpolador y Disipador del Chakra Flare de Itachi
        if (itachi.isFlareActive()) {
            flareTimeLeft -= deltaTime;
            if (flareTimeLeft <= 0.0) {
                itachi.shrinkAura(0.0); // Apagar
                flareTimeLeft = 0.0;
            } else if (flareTimeLeft < 1.0) {
                // En el último segundo, se contrae suavemente de forma lineal
                itachi.shrinkAura(flareTimeLeft);
            } else {
                // Durante el clímax de la interacción, se mantiene al máximo de escala
                itachi.shrinkAura(1.0);
            }
        }
    }

    /**
     * Inicia el temporizador de Chakra Flare por 4.5 segundos.
     */
    public void startChakraFlare() {
        this.flareTimeLeft = 4.5;
        itachi.triggerChakraFlare();
    }
}
