package org.example.aldeakonoha;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;

/**
 * CONTROLADOR DE CÁMARA AVANZADO (CameraController) -
 * Responsabilidad: Controlar los movimientos interactivos de la cámara en 3D. Soporta dos modos:
 * - FREE (WASD + Q/E + Mouse Orbit + Sprint + Reset + Bird's eye)
 * - EXPO/CINEMATIC (Movimiento automático orbital en grúa alrededor de la plaza para exposición ante jurado).
 */
public class CameraController {

    private final PerspectiveCamera camera;
    private final Group cameraPivot;

    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS); // Cabeceo (Pitch)
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS); // Guiñada (Yaw)

    // Posición del ratón en fotograma anterior
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;

    // Constantes físicas calibradas
    private static final double ROTATION_SPEED = 0.22;
    private static final double TRANSLATION_SPEED = 6.8;
    private static final double ZOOM_SPEED = 3.5;
    private static final double MIN_ZOOM = -1600.0;
    private static final double MAX_ZOOM = -50.0;

    // Modos de cámara: FREE, EXPO
    private String cameraMode = "FREE";

    // Flags de movimiento de entrada
    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean sprint = false;

    public CameraController(PerspectiveCamera camera, Group cameraPivot) {
        this.camera = camera;
        this.cameraPivot = cameraPivot;

        // Adjuntar transformaciones rotacionales al grupo pivot
        this.cameraPivot.getTransforms().addAll(rotateY, rotateX);

        // Inicializar
        resetCamera();
    }

    /**
     * Restablece la cámara orbital a su posición inicial cinematográfica de plano general.
     */
    public final void resetCamera() {
        cameraPivot.setTranslateX(0);
        cameraPivot.setTranslateY(-45); // Altura de ojos ninja
        cameraPivot.setTranslateZ(-120); // Foco cerca de la plaza

        rotateX.setAngle(-18); // Inclinación cenital ligera
        rotateY.setAngle(180); // Mirar de frente hacia la aldea y el monumento
        camera.setTranslateZ(-850); // Alejamiento orbital inicial
        
        // Limpiar flags de entrada
        clearMovementFlags();
        
        System.out.println("🎥 [CameraController] Cámara Ninja restablecida al plano inicial.");
    }

    /**
     * Eleva la cámara velozmente al cielo para obtener una vista cenital (Bird's Eye).
     */
    public void flyToBirdsEye() {
        cameraPivot.setTranslateX(0);
        cameraPivot.setTranslateY(-450); // Muy alto
        cameraPivot.setTranslateZ(50);

        rotateX.setAngle(-70); // Mirar directamente hacia abajo
        rotateY.setAngle(180);
        camera.setTranslateZ(-900);
        
        clearMovementFlags();
        System.out.println("🦅 [CameraController] Vista aérea de pájaro ninja activada.");
    }

    /**
     * Limpia todos los flags de teclas held para evitar inercias indeseadas.
     */
    public void clearMovementFlags() {
        forward = false;
        backward = false;
        left = false;
        right = false;
        up = false;
        down = false;
        sprint = false;
    }

    /**
     * Registra los eventos de arrastre y scroll del mouse sobre la escena.
     */
    public void registerMouseControls(Scene scene) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent me) -> {
            // Anular en modo exposición automática
            if ("EXPO".equals(cameraMode)) return;

            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();

            double deltaX = (mousePosX - mouseOldX);
            double deltaY = (mousePosY - mouseOldY);

            // Guiñada libre
            rotateY.setAngle(rotateY.getAngle() + deltaX * ROTATION_SPEED);
            
            // Cabeceo acotado de seguridad (-85° a 10°) para evitar volteo de campana
            double newPitch = rotateX.getAngle() - deltaY * ROTATION_SPEED;
            if (newPitch > -85 && newPitch < 10) {
                rotateX.setAngle(newPitch);
            }
        });

        scene.setOnScroll((ScrollEvent se) -> {
            if ("EXPO".equals(cameraMode)) return;

            double zoom = camera.getTranslateZ() + se.getDeltaY() * ZOOM_SPEED;
            if (zoom >= MIN_ZOOM && zoom <= MAX_ZOOM) {
                camera.setTranslateZ(zoom);
            }
        });
    }

    /**
     * Bucle de actualización por frame. Calcula las traslaciones o aplica la cinemática EXPO.
     */
    public void update(double time) {
        if ("EXPO".equals(cameraMode)) {
            // --- MODO EXPOSICIÓN AUTOMÁTICA EN GRÚA ORBITAL ---
            // 1. Foco bloqueado al centro de la plaza de adoquines
            cameraPivot.setTranslateX(0);
            cameraPivot.setTranslateY(-75 + Math.sin(time * 0.4) * 45); // Oscilación de grúa vertical
            cameraPivot.setTranslateZ(-150);

            // 2. Rotación orbital continua y suave de 360 grados
            rotateY.setAngle(rotateY.getAngle() + 0.125); 

            // 3. Modulación oscilatoria de cabeceo y distancia focal
            rotateX.setAngle(-22 + Math.sin(time * 0.2) * 8.0);
            camera.setTranslateZ(-780 + Math.cos(time * 0.35) * 120);
        } else {
            // --- MODO EXPLORACIÓN LIBRE (FREE) ---
            if (!hasMovementInput()) return;

            double speed = TRANSLATION_SPEED * (sprint ? 2.4 : 1.0);

            // Trigonometría relativa en el plano horizontal XZ
            double angleRad = Math.toRadians(rotateY.getAngle());
            double cos = Math.cos(angleRad);
            double sin = Math.sin(angleRad);

            double moveX = 0;
            double moveZ = 0;

            if (forward) {
                moveZ += speed * cos;
                moveX -= speed * sin;
            }
            if (backward) {
                moveZ -= speed * cos;
                moveX += speed * sin;
            }
            if (left) {
                moveX -= speed * cos;
                moveZ -= speed * sin;
            }
            if (right) {
                moveX += speed * cos;
                moveZ += speed * sin;
            }

            cameraPivot.setTranslateX(cameraPivot.getTranslateX() + moveX);
            cameraPivot.setTranslateZ(cameraPivot.getTranslateZ() + moveZ);

            if (up) {
                cameraPivot.setTranslateY(cameraPivot.getTranslateY() - speed);
            }
            if (down) {
                cameraPivot.setTranslateY(cameraPivot.getTranslateY() + speed);
            }
        }
    }

    private boolean hasMovementInput() {
        return forward || backward || left || right || up || down;
    }

    // --- GETTERS & SETTERS PARA CONTROLES DE ENTRADA ---
    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setBackward(boolean backward) {
        this.backward = backward;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }

    public String getCameraMode() {
        return cameraMode;
    }

    public void setCameraMode(String cameraMode) {
        this.cameraMode = cameraMode;
        clearMovementFlags();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public Group getCameraPivot() {
        return cameraPivot;
    }
}
