package rewrited;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.ColorUtils;
import rewrited.cameraControlling.DefaultController;
import rewrited.engine.Scene;
import rewrited.engine.cameras.Camera;
import rewrited.engine.cameras.ScreenCamera;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;
import rewrited.engine.rendering.processors.colorProcessors.*;
import rewrited.engine.rendering.processors.raymarchingProcessors.RaymarchingProcessor;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;
import rewrited.engine.sceneObjects.figures.*;
import rewrited.engine.sceneObjects.figures.Box;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main extends JFrame {
    private static JFrame frame;
    private static Renderer renderer;
    private static Camera camera;
    private static Camera cloneCamera;
    private static Scene scene;
    private static DirectionalLightColorProcessor lightProcessor1;
    private static DirectionalLightColorProcessor lightProcessor2;
    private static DirectionalLightColorProcessor lightProcessor3;
    private static boolean displayRenderingProcess = false;
    private static int displayImageX = 0;
    private static int displayImageY = 0;
    private static FigureBase figure1;
    private static FigureBase figure2;

    public static void main(String[] args) {
        new Main();
    }

    private static void init() {
        camera = new ScreenCamera();
        cloneCamera = new ScreenCamera();
        scene = new Scene();

        lightProcessor1 = new DirectionalLightColorProcessor(new Vec3d(-3, 1, 0), 1, Color.WHITE);
        lightProcessor2 = new DirectionalLightColorProcessor(new Vec3d(-1, 1, 0), 1, Color.WHITE);
        lightProcessor3 = new DirectionalLightColorProcessor(new Vec3d(1, -1, 0), 1, Color.WHITE);

        renderer = new Renderer(new Renderer.RenderParameters(
                new Raymarcher.RaymarchParameters(scene, camera, 1e-7, 1e5, new ArrayList<RaymarchingProcessor>() {{
                    //add(new BlackHoleProcessor());
                    //add(new MirrorProcessor());
                }}),
                new ArrayList<ColorProcessor>() {{
                    add(new FigureColorProcessor());
                    add(lightProcessor1.add(lightProcessor2).add(lightProcessor3));
                }}).setPixelsPerRay(1));
        DefaultController.addFrameEvents(cloneCamera, frame);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
                    renderer.getRp().setPixelsPerRay(10);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
                    renderer.getRp().setPixelsPerRay(1);
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    displayRenderingProcess = !displayRenderingProcess;
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    displayImageY -= 20;
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    displayImageY += 20;
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    displayImageX += 20;
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    displayImageX -= 20;
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    displayImageX = displayImageY = 0;
                }
            }
        });

        figure1 = new BoxFractal(ColorUtils::colorFn2, 6);
        FigureBase fig = new FigureBase(figure1
                .intersect(new Sphere(ColorUtils::colorFn2).setScale(0.5))::getDistance, figure1::getColor)
                .setPosition(new Vec3d(0, 0, 2.5));
        class Fig extends FigureBase implements WithDefinedBoundingBox {
            public Fig(Figure figure) {
                super(figure);
            }

            @Override
            public boolean isInBoundingBox(Vec3d vector) {
                vector = new Vec3d(vector);
                vector.sub(new Vec3d(0,0, 2.5));
                return vector.length() <= 1;
            }
        }

        fig = new Fig(fig);


        scene.addFigure(fig);
        figure2 = fig;

        cloneCamera.setPosition(new Vec3d(0.49103859998156274, 0.5465538465116195, 1.822999965107419));
        cloneCamera.setCameraDirection(new Vec3d(-0.4406222351271292, -0.587785252292473, 0.6784987421499371));
        /*
        cloneCamera.setPosition(new Vec3d(0.7014928864608042, 0.8476626193731059, 1.5631105651535857));
        cloneCamera.setCameraDirection(new Vec3d(-0.46767619217609707, -0.669130606358858, 0.5775319998974026));
        */
        lightProcessor3.setPosition(cloneCamera.getPosition());
    }

    public Main() {
        frame = this;
        init();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new Main.MyComponent(), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Timer t = new Timer(1000 / 30, e -> frame.repaint());
        t.start();
    }

    private class MyComponent extends JComponent {
        BufferedImage currentImage;
        BufferedImage img;
        String path = "images";
        int folderNumber = 1;
        int fileNumber = 1;

        public MyComponent() {
            while (Files.isDirectory(Paths.get(path + folderNumber))) folderNumber++;
            try {
                Files.createDirectories(Paths.get(path + folderNumber));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                while (true) {
                    try {
                        int w = 1000;//frame.getSize().width;
                        int h = 1000;//frame.getSize().height;
                        double viewAngleY = 70;
                        double viewAngleX = viewAngleY * w / h;
                        camera.setPosition(cloneCamera.getPosition());
                        camera.setCameraDirection(cloneCamera.getDirection());
                        camera.setViewAngle(viewAngleX, viewAngleY);
                        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                        System.out.println(camera.getPosition());
                        System.out.println(camera.getDirection());
                        System.out.println("---------------");
                        lightProcessor3.setPosition(camera.getPosition());
                        figure1.setScale(figure1.getScale() + 0.002);

                        renderer.render(img);
                        currentImage = img;
                        try {
                            File outputfile = new File(path + folderNumber + "\\image" + fileNumber++ + ".png");
                            ImageIO.write(img, "png", outputfile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void paint(Graphics g) {
            int w = img.getWidth();
            int i = 0;
            for (;i < w;i++) {
                if (img.getRGB(i, 1) == -16777216) break;
            }
            frame .setTitle((100.0 * i) / w + "% Done");


            Graphics2D frameG2 = (Graphics2D) g;
            if (displayRenderingProcess) frameG2.drawImage(img, displayImageX, displayImageY, null);
            else frameG2.drawImage(currentImage, displayImageX, displayImageY, null);
        }
    }
}
