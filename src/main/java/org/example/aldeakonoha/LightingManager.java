package org.example.aldeakonoha;

import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.DirectionalLight;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * GESTOR DE ILUMINACIÓN (LightingManager)
 * Responsabilidad: Configurar y alternar de forma interactiva la atmósfera lumínica de la escena 3D,
 * controlando la luz de ambiente global, la luz direccional del sol/luna y el encendido automático 
 * de las luces de punto (PointLights) de las farolas urbanas de noche.
 */
public class LightingManager {

    private final AmbientLight ambientLight;
    private final DirectionalLight sunLight;
    private final List<PointLight> streetLights = new ArrayList<>();

    public LightingManager() {
        // Inicializar luz de ambiente
        ambientLight = new AmbientLight();
        
        // Inicializar luz direccional solar
        sunLight = new DirectionalLight();

        // Configuración por defecto: Modo Atardecer (Crepuscular premium inicial)
        setCycle("SUNSET");
    }

    /**
     * Registra una PointLight urbana de antorcha en el listado para poder encenderla/apagarla en bucle.
     */
    public void registerStreetLight(PointLight light) {
        if (light != null) {
            streetLights.add(light);
        }
    }

    /**
     * Aplica los parámetros lumínicos en tiempo real según el modo de hora activo.
     */
    public void setCycle(String timeMode) {
        switch (timeMode) {
            case "DAY":
                // 1. Luz de ambiente clara para rellenar
                ambientLight.setColor(Color.web("#6C7A89"));

                // 2. Sol brillante, blanco-amarillo cenital alto
                sunLight.setColor(Color.web("#FFFFE0"));
                sunLight.setDirection(new Point3D(0.2, 0.9, 0.4));

                // 3. Apagar PointLights de faroles (Color negro = nulo en iluminación)
                updateStreetLights(Color.BLACK);
                System.out.println("☀️ [LightingManager] Iluminación ajustada a MODO DÍA. Farolas APAGADAS.");
                break;

            case "SUNSET":
                // 1. Luz de ambiente violeta crepuscular
                ambientLight.setColor(Color.web("#3A2045"));

                // 2. Sol poniente, naranja-dorado cálido de ángulo muy bajo
                sunLight.setColor(Color.web("#FFA630"));
                sunLight.setDirection(new Point3D(-0.45, 0.58, 0.68));

                // 3. Mantener faroles apagados para contraste de sombras
                updateStreetLights(Color.BLACK);
                System.out.println("⛩️ [LightingManager] Iluminación ajustada a MODO ATARDECER. Farolas APAGADAS.");
                break;

            case "NIGHT":
                // 1. Luz de ambiente mínima para penumbra azulada ninja
                ambientLight.setColor(Color.web("#0B0B20"));

                // 2. Luz de luna mortecina azul-lavanda
                sunLight.setColor(Color.web("#3A3D5C"));
                sunLight.setDirection(new Point3D(-0.15, 0.7, -0.3));

                // 3. Encendido automático dePointLights en color naranja cálido
                updateStreetLights(Color.web("#FFB732"));
                System.out.println("🌙 [LightingManager] Iluminación ajustada a MODO NOCHE NINJA. Farolas ENCENDIDAS (PointLights activos).");
                break;

            default:
                break;
        }
    }

    /**
     * Helper método para encender o apagar todas las farolas modificando su color en lote.
     */
    private void updateStreetLights(Color color) {
        for (PointLight pl : streetLights) {
            pl.setColor(color);
        }
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public DirectionalLight getSunLight() {
        return sunLight;
    }

    public List<PointLight> getStreetLights() {
        return streetLights;
    }
}
