package rewrited.cameraControlling;

import rewrited.engine.cameras.Camera;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DefaultController {
    public static void addFrameEvents(Camera camera, JFrame frame) {
        addFrameEvents(camera, frame, 0.01, 3);
    }

    public static void addFrameEvents(Camera camera, JFrame frame, double unitMovement, double unitRotation) {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera.moveTowards(unitMovement);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera.moveBackwards(unitMovement);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera.moveLeft(unitMovement);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera.moveRight(unitMovement);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_X) {
                    camera.rotateScreenX(unitRotation);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    camera.rotateScreenX(-unitRotation);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    camera.rotateScreenY(unitRotation);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    camera.rotateScreenY(-unitRotation);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
                    camera.setViewAngle(camera.getViewAngleX() + 1, camera.getViewAngleY() + 1);
                    frame.setTitle(camera.getViewAngleX() + ";" + camera.getViewAngleY());
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
                    camera.setViewAngle(camera.getViewAngleX() - 1, camera.getViewAngleY() - 1);
                    frame.setTitle(camera.getViewAngleX() + ";" + camera.getViewAngleY());
                }
            }
        });
    }
}
