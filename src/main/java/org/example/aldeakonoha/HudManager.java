package org.example.aldeakonoha;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * GESTOR DE INTERFAZ 2D (HudManager)
 * Responsabilidad: Diseñar, actualizar e inyectar la capa 2D translúcida de visualización (HUD)
 * sobre la vista 3D. Controla las guías rápidas, reportes de estado, alertas de proximidad
 * y la caja de diálogos interactiva del personaje principal.
 */
public class HudManager {

    private final StackPane mainRoot;

    // Componentes visuales
    private VBox statusPanel;
    private VBox controlsPanel;
    private VBox alertPanel;
    private VBox dialoguePanel;

    // Textos de estado
    private Text textProject;
    private Text textMusic;
    private Text textCamera;
    private Text textTime;

    // Textos de alertas y diálogos
    private Text textAlert;
    private Text textDialogueName;
    private Text textDialogueContent;

    // Variables de control de estado
    private boolean isControlsHelpVisible = true;
    private boolean isNearItachi = false;
    private String activeZoneName = "NINGUNA";

    // Frases icónicas de Itachi Uchiha
    private final String[] itachiQuotes = {
        "\"La gente vive apoyándose en el conocimiento y la conciencia. A eso lo llaman realidad.\"",
        "\"Incluso el más poderoso de los oponentes tiene siempre una debilidad.\"",
        "\"Aquellos que no son capaces de reconocerse a sí mismos, están destinados al fracaso.\"",
        "\"No importa qué tan poderoso te vuelvas, nunca intentes cargar con todo tú solo.\"",
        "\"Somos humanos, no dioses. No conocemos nuestro destino real hasta el final.\""
    };
    private int quoteIndex = 0;

    public HudManager(StackPane mainRoot) {
        this.mainRoot = mainRoot;
        buildHUD();
    }

    /**
     * Construye y organiza todas las piezas de la UI 2D en el StackPane principal.
     */
    private void buildHUD() {
        // --- 1. PANEL DE ESTADOS (Esquina Superior Izquierda) ---
        statusPanel = new VBox(6);
        statusPanel.setPadding(new Insets(14));
        statusPanel.setMaxWidth(300);
        statusPanel.setMaxHeight(140);
        styleGlassmorphic(statusPanel, "rgba(10, 10, 15, 0.82)", "rgba(180, 20, 20, 0.7)");

        textProject = new Text("PROJECT KONOHA 3D // FINAL GRAPHICS");
        textProject.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        textProject.setFill(Color.web("#B30F0F")); // Rojo Akatsuki

        textMusic = new Text("AUDIO: PLAYING");
        textMusic.setFont(Font.font("System", FontWeight.BOLD, 12));
        textMusic.setFill(Color.web("#D8DEE9"));

        textCamera = new Text("CAMERA: FREE MODE");
        textCamera.setFont(Font.font("System", FontWeight.BOLD, 12));
        textCamera.setFill(Color.web("#D8DEE9"));

        textTime = new Text("TIME: SUNSET NINJA");
        textTime.setFont(Font.font("System", FontWeight.BOLD, 12));
        textTime.setFill(Color.GOLD);

        statusPanel.getChildren().addAll(textProject, textMusic, textCamera, textTime);
        StackPane.setAlignment(statusPanel, Pos.TOP_LEFT);
        StackPane.setMargin(statusPanel, new Insets(15));

        // --- 2. GUÍA RÁPIDA DE CONTROLES (Esquina Superior Derecha) ---
        controlsPanel = new VBox(6);
        controlsPanel.setPadding(new Insets(12));
        controlsPanel.setMaxWidth(340);
        controlsPanel.setMaxHeight(260);
        styleGlassmorphic(controlsPanel, "rgba(10, 10, 15, 0.82)", "rgba(212, 175, 55, 0.6)"); // Ribete dorado

        Text textCtrlTitle = new Text("⛩️ GUÍA DE EXPLORACIÓN 3D:");
        textCtrlTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
        textCtrlTitle.setFill(Color.GOLD);

        String desc = "• [W, A, S, D]: Caminar relativo en XZ\n"
                    + "• [Q, E]: Subir / Bajar altura\n"
                    + "• [SHIFT] (Hold): Sprint ninja veloz\n"
                    + "• Drag Mouse: Orbitar Mirada (Yaw/Pitch)\n"
                    + "• Scroll Mouse: Zoom focal\n"
                    + "• Tecla [R]: Restablecer cámara inicial\n"
                    + "• Tecla [ESPACIO]: Elevación a ojo de pájaro\n"
                    + "• Tecla [L] o [T]: Ciclo Día / Tarde / Noche\n"
                    + "• Tecla [M]: Silenciar / Reanudar música\n"
                    + "• Tecla [H]: Ocultar / Mostrar esta guía\n";
        Text textCtrlDesc = new Text(desc);
        textCtrlDesc.setFont(Font.font("System", 11));
        textCtrlDesc.setFill(Color.web("#E5E9F0"));

        controlsPanel.getChildren().addAll(textCtrlTitle, textCtrlDesc);
        StackPane.setAlignment(controlsPanel, Pos.TOP_RIGHT);
        StackPane.setMargin(controlsPanel, new Insets(15));

        // --- 3. PANEL DE ALERTAS DE PROXIMIDAD (Centro Inferior) ---
        alertPanel = new VBox(10);
        alertPanel.setPadding(new Insets(10, 20, 10, 20));
        alertPanel.setMaxWidth(500);
        alertPanel.setMaxHeight(40);
        alertPanel.setAlignment(Pos.CENTER);
        alertPanel.setVisible(false); // Oculto por defecto
        styleGlassmorphic(alertPanel, "rgba(20, 10, 10, 0.88)", "rgba(255, 60, 60, 0.85)");

        textAlert = new Text("⚠️ DETECTADO...");
        textAlert.setFont(Font.font("System", FontWeight.BOLD, 12));
        textAlert.setFill(Color.web("#FFD700"));
        alertPanel.getChildren().add(textAlert);

        StackPane.setAlignment(alertPanel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(alertPanel, new Insets(0, 0, 140, 0)); // Elevado sobre el diálogo

        // --- 4. PANEL DE DIÁLOGOS ITACHI (Fondo Inferior Centro) ---
        dialoguePanel = new VBox(4);
        dialoguePanel.setPadding(new Insets(12));
        dialoguePanel.setMaxWidth(620);
        dialoguePanel.setMaxHeight(75);
        dialoguePanel.setVisible(false); // Oculto por defecto
        styleGlassmorphic(dialoguePanel, "rgba(12, 5, 5, 0.94)", "rgba(180, 15, 15, 0.9)");

        textDialogueName = new Text("ITACHI UCHIHA");
        textDialogueName.setFont(Font.font("System", FontWeight.BOLD, 13));
        textDialogueName.setFill(Color.web("#FF1E1E"));

        textDialogueContent = new Text("\"...\"");
        textDialogueContent.setFont(Font.font("Georgia", 14));
        textDialogueContent.setFill(Color.web("#FAF6EE"));

        dialoguePanel.getChildren().addAll(textDialogueName, textDialogueContent);
        StackPane.setAlignment(dialoguePanel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialoguePanel, new Insets(0, 0, 45, 0));

        // Agregar todos los paneles al StackPane principal
        mainRoot.getChildren().addAll(statusPanel, controlsPanel, alertPanel, dialoguePanel);
    }

    /**
     * Aplica la opacidad translúcida y bordes Akatsuki/Hokage a los paneles 2D.
     */
    private void styleGlassmorphic(VBox p, String bgStyle, String borderStyle) {
        p.setStyle("-fx-background-color: " + bgStyle + ";"
                 + "-fx-background-radius: 10px;"
                 + "-fx-border-color: " + borderStyle + ";"
                 + "-fx-border-width: 2.0px;"
                 + "-fx-border-radius: 10px;"
                 + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.65), 8, 0, 0, 0);");
    }

    /**
     * Actualiza las lecturas del panel informativo superior izquierdo en tiempo real.
     */
    public void updateHUD(String cameraMode, String timeMode, boolean isMusicPlaying, boolean audioAvailable) {
        // 1. Audio
        if (!audioAvailable) {
            textMusic.setText("AUDIO: NO AUDIO HARDWARE");
            textMusic.setFill(Color.GRAY);
        } else {
            textMusic.setText("AUDIO: " + (isMusicPlaying ? "PLAYING 🎵" : "PAUSED 🔇"));
            textMusic.setFill(isMusicPlaying ? Color.LIGHTGREEN : Color.SALMON);
        }

        // 2. Cámara
        textCamera.setText("CAMERA: " + cameraMode);

        // 3. Hora del día
        textTime.setText("TIME: " + timeMode);
        if ("DAY".equals(timeMode)) textTime.setFill(Color.DEEPSKYBLUE);
        else if ("SUNSET".equals(timeMode)) textTime.setFill(Color.GOLD);
        else textTime.setFill(Color.web("#9A8CFF"));
    }

    /**
     * Oculta o muestra la guía de controles rápidos.
     */
    public void toggleHelp() {
        isControlsHelpVisible = !isControlsHelpVisible;
        controlsPanel.setVisible(isControlsHelpVisible);
        System.out.println("HUD: Guía rápida " + (isControlsHelpVisible ? "VISIBLE" : "OCULTA"));
    }

    /**
     * Calcula la distancia cartesiana horizontal de la cámara a las zonas de interés 
     * y maneja la aparición dinámica de alertas en el HUD.
     */
    public void checkProximities(double camX, double camZ, KonohaVillage village) {
        if (village == null) return;

        // 1. Proximidad a Itachi Uchiha (Ubicado en 0, -150)
        double distItachi = computeDistance(camX, camZ, 0, -150);
        
        // 2. Proximidad a Ramen Ichiraku (Ubicado en -130, 0)
        double distRamen = computeDistance(camX, camZ, -130, 0);

        // 3. Proximidad a Oficina Hokage (Ubicado en 0, 380)
        double distHokage = computeDistance(camX, camZ, 0, 380);

        // 4. Proximidad a Rostros Monumento (Ubicado en 0, 480)
        double distMonument = computeDistance(camX, camZ, 0, 480);

        // Lógica de visualización jerárquica de alertas
        if (distItachi < 120) {
            isNearItachi = true;
            activeZoneName = "ITACHI";
            textAlert.setText("⚠️ ALERTA: Itachi Uchiha detectado. Presiona [F] para interactuar.");
            textAlert.setFill(Color.web("#FF3C3C"));
            alertPanel.setVisible(true);
        } else if (distRamen < 130) {
            isNearItachi = false;
            activeZoneName = "RAMEN";
            textAlert.setText("🍜 ZONA COMERCIAL: Puesto de Ramen Ichiraku.");
            textAlert.setFill(Color.web("#FFB732"));
            alertPanel.setVisible(true);
        } else if (distHokage < 140) {
            isNearItachi = false;
            activeZoneName = "HOKAGE_OFFICE";
            textAlert.setText("🏢 SEDE DE PODER: Oficina del Hokage.");
            textAlert.setFill(Color.web("#FF5252"));
            alertPanel.setVisible(true);
        } else if (distMonument < 155) {
            isNearItachi = false;
            activeZoneName = "MONUMENT";
            textAlert.setText("🗻 MONUMENTO HOKAGE: Rostros de piedra tallados.");
            textAlert.setFill(Color.LIGHTGRAY);
            alertPanel.setVisible(true);
        } else {
            isNearItachi = false;
            activeZoneName = "NINGUNA";
            alertPanel.setVisible(false);
            
            // Si nos alejamos de Itachi, se oculta el diálogo automáticamente
            if (dialoguePanel.isVisible()) {
                dialoguePanel.setVisible(false);
            }
        }
    }

    private double computeDistance(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }

    /**
     * Muestra la caja de diálogos con una frase célebre de Itachi Uchiha de forma aleatoria.
     * Incorpora una transición visual de fade suave.
     */
    public void triggerItachiDialogue() {
        if (!isNearItachi) return;

        // Ocultar alerta mientras habla
        alertPanel.setVisible(false);

        // Seleccionar frase y mostrar panel
        textDialogueContent.setText(itachiQuotes[quoteIndex]);
        dialoguePanel.setVisible(true);

        // Animación suave de aparición
        FadeTransition ft = new FadeTransition(Duration.millis(350), dialoguePanel);
        ft.setFromValue(0.2);
        ft.setToValue(1.0);
        ft.play();

        // Rotar frases de forma secuencial
        quoteIndex = (quoteIndex + 1) % itachiQuotes.length;

        System.out.println("🗣️ Itachi Uchiha dice: " + textDialogueContent.getText());
    }

    public boolean isNearItachi() {
        return isNearItachi;
    }

    public VBox getDialoguePanel() {
        return dialoguePanel;
    }
}
