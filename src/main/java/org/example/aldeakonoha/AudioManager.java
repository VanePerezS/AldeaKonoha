package org.example.aldeakonoha;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * GESTOR DE AUDIO (AudioManager) - Proyecto Final de Graficación Computacional.
 * Responsabilidad: Controlar la carga y reproducción continua de la banda sonora de fondo ninja.
 * Implementa resiliencia extrema: si el archivo de audio local "/audio/background.mp3" no existe,
 * se desactiva el sonido silenciosamente y se permite jugar con total normalidad sin caídas ni crashes.
 * 
 * CONCEPTOS DE EXPOSICIÓN ACADÉMICA:
 * 1. Resiliencia de Software (Fail-Safe): Captura controlada de excepciones de hardware y de E/S.
 *    Evita interrumpir el hilo principal de renderizado (Application Thread) si falta un recurso secundario.
 * 2. Reproducción Concurrente y Loops: Hilos paralelos gestionados por JavaFX Media Engine que decodifican
 *    el codec MPEG-3 sin ralentizar los fotogramas de la simulación 3D.
 */
public class AudioManager {

    private MediaPlayer mediaPlayer;
    private boolean audioAvailable = false;
    private boolean playing = false;

    public AudioManager() {
        try {
            // Intento 1: Buscar en el classpath raíz (Estándar de empaquetado Maven)
            URL audioUrl = getClass().getResource("/audio/background.mp3");

            // Intento 2: Buscar en la estructura física del proyecto
            if (audioUrl == null) {
                java.io.File file = new java.io.File("src/main/resources/audio/background.mp3");
                if (file.exists()) {
                    audioUrl = file.toURI().toURL();
                }
            }

            // Intento 3: Buscar en la estructura target transpilada
            if (audioUrl == null) {
                java.io.File file = new java.io.File("target/classes/audio/background.mp3");
                if (file.exists()) {
                    audioUrl = file.toURI().toURL();
                }
            }

            if (audioUrl != null) {
                Media sound = new Media(audioUrl.toExternalForm());
                mediaPlayer = new MediaPlayer(sound);

                // Bucle continuo
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.35); // Volumen prudencial de fondo
                
                audioAvailable = true;
                System.out.println("🎵 [AudioManager] Archivo 'background.mp3' cargado exitosamente. Listo para sonar.");
            } else {
                System.err.println("⚠️ [AudioManager] ADVERTENCIA: El archivo '/audio/background.mp3' no fue hallado.");
                System.err.println("   La aplicación continuará en MODO SILENCIOSO resiliente.");
            }
        } catch (Exception e) {
            System.err.println("❌ [AudioManager] No se pudo inicializar el sistema de audio: " + e.getMessage());
            System.err.println("   Modo silencioso activado por seguridad.");
        }
    }

    /**
     * Inicia la reproducción de fondo.
     */
    public void play() {
        if (audioAvailable && mediaPlayer != null) {
            try {
                mediaPlayer.play();
                playing = true;
                System.out.println("🎵 [AudioManager] Música de fondo iniciada.");
            } catch (Exception e) {
                System.err.println("❌ [AudioManager] Error al invocar play(): " + e.getMessage());
            }
        }
    }

    /**
     * Alterna (pausa/reanuda) la música.
     */
    public void pauseResume() {
        if (!audioAvailable || mediaPlayer == null) return;

        try {
            if (playing) {
                mediaPlayer.pause();
                playing = false;
                System.out.println("🎵 [AudioManager] Música PAUSADA.");
            } else {
                mediaPlayer.play();
                playing = true;
                System.out.println("🎵 [AudioManager] Música REANUDADA.");
            }
        } catch (Exception e) {
            System.err.println("❌ [AudioManager] Error al pausar/reanudación de audio: " + e.getMessage());
        }
    }

    /**
     * Detiene por completo la reproducción al cerrar el Stage principal.
     */
    public void stop() {
        if (audioAvailable && mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                playing = false;
            } catch (Exception e) {
                System.err.println("❌ [AudioManager] Error al detener audio en cierre: " + e.getMessage());
            }
        }
    }

    public boolean isAudioAvailable() {
        return audioAvailable;
    }

    public boolean isPlaying() {
        return playing;
    }
}
