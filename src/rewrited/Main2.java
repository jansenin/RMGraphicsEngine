package rewrited;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.cameraControlling.DefaultController;
import rewrited.engine.Scene;
import rewrited.engine.cameras.Camera;
import rewrited.engine.cameras.ScreenCamera;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;
import rewrited.engine.rendering.processors.colorProcessors.*;
import rewrited.engine.rendering.processors.colorProcessors.processorCombiners.AddProcessorCombiner;
import rewrited.engine.rendering.processors.colorProcessors.processorCombiners.MultiplyProcessorCombiner;
import rewrited.engine.rendering.processors.colorProcessors.processorData.LightProcessorData;
import rewrited.engine.rendering.processors.raymarchingProcessors.MirrorProcessor;
import rewrited.engine.rendering.processors.raymarchingProcessors.RaymarchingProcessor;
import rewrited.engine.rendering.processors.raymarchingProcessors.processorData.Mirror;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;
import rewrited.engine.sceneObjects.figureCombiners.InversedFigure;
import rewrited.engine.sceneObjects.figureCombiners.SmoothMaxCombiner;
import rewrited.engine.sceneObjects.figureCombiners.SmoothMinCombiner;
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
import java.util.function.Function;

public class Main2 extends JFrame {
    private static JFrame frame;
    private static Renderer renderer;
    private static Camera camera;
    private static Camera cloneCamera;
    private static Scene scene;
    private static DirectionalLightColorProcessor directionalLightColorProcessor;
    private static DirectionalLightColorProcessor directionalLightColorProcessor2;
    private static DirectionalLightColorProcessor directionalLightColorProcessor3;
    private static Figure figure;
    private static Figure figure2;
    private static Figure figure3;
    private static boolean displayRenderingProcess = false;
    private static int timer = 0;
    private static double k = 0;
    private static BufferedImage boxImg;

    public static void main(String[] args) {
        new Main();
    }

    private static void init() {
        /*
        for (int i = 10;i <= 99;i++) {
            Path source = Paths.get("images130\\image" + i + ".png");
            try {
                Files.move(source, source.resolveSibling("image0" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

        camera = new ScreenCamera();
        cloneCamera = new ScreenCamera();
        scene = new Scene();

        /*
        scene.addFigure(
                new MaxCombiner(
                        new MaxCombiner(
                                new Sphere(new Vec3d(-1, 5, 0), 2, Color.WHITE),
                                new Sphere(new Vec3d(1, 5, 0), 2, Color.WHITE)
                        ),
                        new InversedFigure(new Box(new Vec3d(0,5, -6), 10))
                )
        );
        */

        /*
        0.0,  6.00000000000297,  -54.000000000208296
        0.17516067476198444, -1.6547206026569707, -1.1608715812992079
         */
        //scene.addFigure(new MandelBulb(new Vec3d(0, 5, 0), Color.WHITE));

        /*
        camera.setPosition(new Vec3d(0.17516067476198444, -1.6547206026569707, -1.1608715812992079));
        camera.setRotation(0.0,  6.00000000000297,  -54.000000000208296);

        scene.addFigure(new XYPlane(10.5, Color.WHITE));
        scene.addFigure(new XYPlane(-10.5, Color.WHITE));
        scene.addFigure(new YZPlane(10.5, Color.RED));
        scene.addFigure(new YZPlane(-10.5, Color.RED));
        scene.addFigure(new XZPlane(21.5, Color.YELLOW));
        scene.addFigure(new XZPlane(-21.5, Color.YELLOW));
        */

        /*
        camera.setPosition(new Vec3d(-2.2080134541264886, 2.9240804632283717, -1.6452286299303935));
        camera.setRotation(0.0, -29.99999999999889, -60.000000000040444);
        */

        directionalLightColorProcessor = new DirectionalLightColorProcessor(new Vec3d(-3, 1, 0), 1, Color.WHITE);
        directionalLightColorProcessor2 = new DirectionalLightColorProcessor(new Vec3d(-1, 1, 4), 1, Color.WHITE);
        directionalLightColorProcessor3 = new DirectionalLightColorProcessor(new Vec3d(1, -1, 8), 1, Color.WHITE);

        renderer = new Renderer(new Renderer.RenderParameters(
                new Raymarcher.RaymarchParameters(scene, camera, 2e-4, 1e5, new ArrayList<RaymarchingProcessor>() {{
                    //add(new BlackHoleProcessor());
                    //add(new MirrorProcessor());
                }}),
                new ArrayList<ColorProcessor>() {{
                    add(new FigureColorProcessor());
                    ///add(new DistanceColorProcessor(10, true));
                    add(new AddProcessorCombiner(
                            new AddProcessorCombiner(
                                    new AddProcessorCombiner(
                                            directionalLightColorProcessor,
                                            directionalLightColorProcessor2
                                    ),
                                    directionalLightColorProcessor3
                            ),
                            new GeometryComplexnessColorProcessor()
                    ));
                    /*add(directionalLightColorProcessor
                            .add(directionalLightColorProcessor2)
                            .add(directionalLightColorProcessor3)
                            .add(new GeometryComplexnessColorProcessor())
                    );*/
                    //add(directionalLightColorProcessor);
                    // add(new EdgeHilightColorProcessor(Color.RED));//}}));
                    //add(new TestProcessor());
                }}).setPixelsPerRay(10));
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

        //scene.addFigure(new Sphere(new Vec3d(0, 0, 10), 0.2, Color.WHITE));

        /*
        scene.addFigure(
                new MaxCombiner(
                    new InversedFigure(
                            new Sphere(new Vec3d(0, 5, 0), 0.7, Color.RED)
                    ),
                    new MaxCombiner(
                            new Sphere(new Vec3d(0, 5, 0), 1, Color.BLUE),
                            new MaxCombiner(
                                    new InversedFigure(new Box(new Vec3d(2, 5, 0), 3)),
                                    new InversedFigure(new Box(new Vec3d(-2, 5, 0), 3))
                            )
                    )
                )
        );
        */

        //scene.addFigure(new ShelledFigure(new Box(new Vec3d(0, 5, 0), 1), 0.5));
        //scene.addFigure(new Sphere(new Vec3d(0, 5, 0), 1, Color.WHITE));

        //scene.addFigure(new InversedFigure(new Box(new Vec3d(0, 0, 0), 10)));
        //scene.addFigure(new InversedFigure(new Sphere(new Vec3d(0, 0, 0), 10, Color.MAGENTA)));

        /*
        scene.addFigure(new ShelledFigure(new XYPlane(10, Color.WHITE), 0.5));
        scene.addFigure(new ShelledFigure(new XYPlane(-10, Color.WHITE), 0.5));
        scene.addFigure(new ShelledFigure(new YZPlane(10, Color.RED), 0.5));
        scene.addFigure(new ShelledFigure(new YZPlane(-10, Color.RED), 0.5));
        scene.addFigure(new ShelledFigure(new XZPlane(21, Color.YELLOW), 0.5));
        scene.addFigure(new ShelledFigure(new XZPlane(-21, Color.YELLOW), 0.5));
        */

        /*
        scene.addFigure(new XZPlane(10, p -> Color.MAGENTA));
        scene.addFigure(new XZPlane(-10, p -> Color.RED));
        scene.addFigure(new XYPlane(10, p -> Color.WHITE));
        scene.addFigure(new XYPlane(-10, p -> Color.BLUE));
        scene.addFigure(new YZPlane(10, p -> Color.DARK_GRAY));
        scene.addFigure(new YZPlane(-10, p -> Color.GREEN));
        */

        /*
        scene .addFigure(new Sphere(p -> Color.WHITE).setPosition(new Vec3d(-3, 1, 3)).setRotation(30, 40, 50));

        class Fig extends FigureBase implements Mirror {
            public Fig(Figure figure) {
                super(figure::getDistance, figure::getColor);
            }


        }

        scene.addFigure(new Fig(new XYPlane(5, p -> Color.WHITE)));
        scene.addFigure(new Sphere(new Vec3d(-3 + 2, 1 + 2, 5), p -> Color.DARK_GRAY).setScale(0.2));
        scene.addFigure(new Sphere(new Vec3d(-3 + 2, 1 - 2, 5), p -> Color.DARK_GRAY).setScale(0.2));
        scene.addFigure(new Sphere(new Vec3d(-3 - 2, 1 + 2, 5), p -> Color.DARK_GRAY).setScale(0.2));
        scene.addFigure(new Sphere(new Vec3d(-3 - 2, 1 - 2, 5), p -> Color.DARK_GRAY).setScale(0.2));
        */

        /*
        scene.addFigure(new FigXZ(10, p -> Color.MAGENTA));
        scene.addFigure(new FigXZ(-10, p -> Color.RED));
        scene.addFigure(new FigXY(10, p -> Color.WHITE));
        scene.addFigure(new FigXY(-10, p -> Color.BLUE));
        scene.addFigure(new FigYZ(10, p -> Color.DARK_GRAY));
        scene.addFigure(new FigYZ(-10, p -> Color.GREEN));
        */

        /*
        scene.addFigure(new InfiniteReplicator(new MaxCombiner(
                new Box(new Vec3d(1, 1, 1), 2),
                new InversedFigure(new Sphere(new Vec3d(1, 1, 1), 1.42, Color.WHITE))
        ), 2));
        */

        //scene.addFigure(new BoxFractal(new Vec3d(0, 0, 0), Color.WHITE, 1, 6));

        /*
        * 0.0  -38.999999999996284  -17.999999999981412
        * Vec3d[-0.8371315486277939, -0.22067508310460032, -0.25533005789847957]
        *
        * 0.0  0.0  -90.0
        * Vec3d[4.2372779250499067E-16, -3.079999999999793, 0.0]
        *
        * 0.0  -18.000000000000135  -57.00000000013345
        * Vec3d[0.020086104634372146, -0.8965053873710139, 0.08344870772300939]
        *
        * 0.0  -21.000000000000064  -30.00000000004536
        * Vec3d[0.0277394276972509, -0.8665634977890099, 0.06565623363472714]
        */

        //scene.addFigure(new XZPlane(10, Constants.SCENE_COLOR));
        //scene.addFigure(new MandelBulb(new Vec3d(0, 0, 5), Color.WHITE));
        //scene.addFigure(figure = new BoxFractal(new Vec3d(0, 0, 0), Color.WHITE, 2, 5));
        //figure2 = new Sphere(new Vec3d(0, 0, 0), 0.5, Color.WHITE);
        /*Figure figure3 = new MaxCombiner(
                new Box(new Vec3d(0, 0, 0), 1),
                new InversedFigure(figure2)
        );*/
        /*
        scene.addFigure(figure = new Box(new Vec3d(-1, 0, 3), 1, 10, 20, 30, Color -> java.awt.Color.WHITE));
        scene.addFigure(figure2 = new BoxFractal(new Vec3d(0, 0, 3), 1, 70, 80, 90, Color -> java.awt.Color.YELLOW, 5));
        scene.addFigure(figure3 = new Box(new Vec3d(1, 0, 3), 1, 40, 50, 60, Color -> java.awt.Color.DARK_GRAY));
        */

        /*
        Figure local = new MaxCombiner(
                new MaxCombiner(
                        new Shell(new Sphere(new Vec3d(0, 0, 0), 2, p -> Color.WHITE), 0.1),
                        new InversedFigure(new Box(new Vec3d(-10, 0, 0), 19, p -> Color.WHITE))
                ),
                new InversedFigure(new Box(new Vec3d(10, 0, 0), 19, p -> Color.WHITE))
        ).move(new Vec3d(0, 0, 2)).rotate(0, 0, 0);

        class Fig extends FigureBase implements WithDefinedBoundingBox {
            public Fig(Figure figure) {
                super(figure::getDistance, figure::getColor);
            }

            @Override
            public boolean isInBoundingBox(Vec3d vector) {
                return vector.length() < 5;
            }
        }

        scene.addFigure(new Fig(local));
        */

        /*
        new Color(
                        Utils.clamp((float)Utils.mod((Math.sin(p.x)-2*Math.sin(2*p.y)+0.1*Math.sin(3*p.z)+2*Math.sin(4*p.x) / 16), 1), 0, 1),
                        Utils.clamp((float)Utils.mod((Math.sin(p.y)-2*Math.sin(2*p.z)+0.1*Math.sin(3*p.x)+2*Math.sin(4*p.y) /  8), 1), 0, 1),
                        Utils.clamp((float)Utils.mod((Math.sin(p.z)-2*Math.sin(2*p.x)+0.1*Math.sin(3*p.y)+2*Math.sin(4*p.z) /  4), 1), 0, 1)
                );
        */

        /*
        class Fig extends FigureBase implements WithDefinedBoundingBox {
            public Fig() {
                figure = new SmoothMinCombiner(
                        new Box(new Vec3d(-1, 0, 4), p -> Color.DARK_GRAY),
                        new Sphere(new Vec3d(0, 0, 4), p -> Color.DARK_GRAY), k, p -> Color.DARK_GRAY);
                setDistanceAndColorFunctions(figure::getDistance, figure::getColor);
            }

            @Override
            public boolean isInBoundingBox(Vec3d vector) {
                vector = new Vec3d(vector.x, vector.y, vector.z - 4);
                return vector.length() < 2.5;
            }
        }
        */

        //scene.addFigure(new Box(new Vec3d(0, 0, 5), p -> Color.WHITE));
        //scene.addFigure(new Fig());
        /*
        scene.addFigure(new Box(new Vec3d(-0.5, 0, 2), p -> {
            if (boxImg == null) return Color.WHITE;
            return new Color(boxImg.getRGB(
                    Math.min(Math.max(0, (int)((boxImg.getWidth() - 1) * (p.x + 0.5))), boxImg.getWidth() - 1),
                    Math.min(Math.max(0, (int)((boxImg.getHeight() - 1) * (p.y + 0.5))), boxImg.getHeight() - 1)
            ));
        }));
        scene.addFigure(figure = new BoxFractal(new Vec3d(0.5, 0, 2.01), p -> new Color(
                ((int)(p.length()*p.length()*p.length() * 1000)) % 256,
                ((int)(p.length()*p.length() * 2000)) % 256,
                ((int)(p.length() * 4000)) % 256
        ), 5));
        */

        scene.addFigure(figure =
                new Box(new Vec3d(0, 0, 3), point -> Color.WHITE
//                new Fig(
                /*new MandelBulb(new Vec3d(0, 0, 2.5), p -> new Color(
                        ((int)(p.length()*p.length() * 1000)) % 256,
                        ((int)(p.length() * 2000)) % 256,
                        ((int)(p.length() * 4000)) % 256
                ), 8*/
                /*point -> new Color(
                        ((int)(Math.sqrt(point.x*point.x + point.y*point.y) * 1000)) % 256,
                        ((int)(Math.sqrt(point.x*point.x + point.z*point.z) * 1000)) % 256,
                        ((int)(Math.sqrt(point.y*point.y + point.z*point.z) * 1000)) % 256
                )*/
                        //new XYPlane(/*new Vec3d(0, 0, 3), */point -> Color.WHITE//(Utils.mod(point.x / 10, 2) - 1) * (Utils.mod(point.y / 10, 2) - 1) < 0 ? Color.RED : Color.BLUE
                /*new FigureBase(
                new Vec3d(0, 0, 10),
                vec3d -> Math.max(Math.abs(vec3d.x), Math.max(Math.abs(vec3d.y), Math.abs(vec3d.z))) - 0.5,
                //figure3::getDistance,
                //vec3d -> Math.pow(Math.pow(Math.abs(vec3d.x), 3) + Math.pow(Math.abs(vec3d.y), 3) + Math.pow(Math.abs(vec3d.z), 3), 1.0 / 3) - 1,
                point -> new Color(
                    ((int)(Math.abs(point.x * 1000))) % 256,
                    ((int)(Math.abs(point.y * 1000))) % 256,
                    ((int)(Math.abs(point.z * 1000))) % 256
                )*/ ));

        /*
        for(int i = -5;i <= 5;i++)
            for(int j = -5;j <= 5;j++)
        scene.addFigure(new Sphere(new Vec3d(Math.random() * 10, Math.random() * 10, Math.random() * 10), 1, new Color((float)Math.random(), (float)Math.random(), (float)Math.random())));
        */

        /*
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0) continue;
                    Color color = Color.WHITE;
                    if (Math.abs(i) == 1 && Math.abs(j) + Math.abs(k) == 0) color = Color.RED;
                    if (Math.abs(j) == 1 && Math.abs(i) + Math.abs(k) == 0) color = Color.GREEN;
                    if (Math.abs(k) == 1 && Math.abs(j) + Math.abs(i) == 0) color = Color.BLUE;
                    if (k == -1 && i == 0 && j == 0) color = Color.BLACK;
                    scene.addFigure(new Sphere(new Vec3d(i * 5, j * 5, k * 5), 1, color));
                }
            }
        }
        */

        //camera.rotate(0, 0, -90);
        cloneCamera.setPosition(new Vec3d(0.2700229076961992, 0.0, 1.2248637369515127));
    }

    public Main2() {
        frame = this;
        init();
        frame.setSize(1000, 1000);
        frame.setTitle("Drawing Shapes");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new Main2.MyComponent(), BorderLayout.CENTER);
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
                        Graphics2D frameG2 = (Graphics2D) frame.getGraphics();
                        int w = frame.getSize().width;
                        int h = frame.getSize().height;
                        double viewAngleY = 70;
                        double viewAngleX = viewAngleY * w / h;
                        camera.setPosition(cloneCamera.getPosition());
                        camera.setCameraDirection(cloneCamera.getDirection());
                        camera.setViewAngle(viewAngleX, viewAngleY);
                        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                        //figure.move(new Vec3d(Math.sin(timer / 100f) / 100, 0, 0));
                        //((BoxFractal)figure).setEdgeLength(((BoxFractal)figure).getEdgeLength() + 0.1);
                        //figure.move(new Vec3d(Math.sin(timer / 10f) / 10, 0, 0));
                        //((FigureBase) figure).setScale((Math.sin(timer / 20f) / 2 + 1));
                        //((FigureBase)figure).setRotation(Math.sin(timer / 100f) * 360, Math.sin(timer / 100f) * 180, Math.sin(timer / 100f) * 90);
                        //((FigureBase)figure).setRotation(360 * Math.random(), 360 * Math.random(), 360 * Math.random());
                        //((FigureBase) figure).rotate(1, 2, 4);
                        //((MandelBulb) figure).setPower(Math.cos(timer / 20.0) * 3.5 + 4.5);

                        /*
                        figure.rotate(1, 2, 4);
                        figure2.rotate(3, 1, 2);
                        figure3.rotate(4, 2, 3);
                        */

                        //((SmoothMinCombiner) figure).setK(k += 0.003);
                        //figure.rotate(0, 0, 4);

                        //System.out.println(" " + camera.getXToYRotation() + " " + camera.getXToZRotation() + " " + camera.getYToZRotation());
                        //System.out.println(camera.getDirection());
                        //System.out.println(camera.getXToYRotation() + "  " + camera.getXToZRotation() + "  " + camera.getYToZRotation());
                        System.out.println(camera.getPosition());
                        //System.out.println(directionalLightColorProcessor.getPosition());
                        //directionalLightColorProcessor.setPosition(new Vec3d(5 * Math.sin(timer / 5.0), 5 * Math.cos(timer / 5.0), 0));
                        //System.out.println(directionalLightColorProcessor.getPosition());
                        directionalLightColorProcessor3.setPosition(camera.getPosition());
//                        directionalLightColorProcessor.setPosition(new Vec3d(5, 0, 3));
//                        ((Sphere) figure2).setRadius(((Math.sin(timer / 50f) + 1) * (Math.pow(2, 1.0 / 3) - 1) + 1) / 2);
                        timer++;

                        renderer.render(img);
                        currentImage = img;
                        boxImg = img;
                        try {
                            File outputfile = new File(path + folderNumber + "\\image" + fileNumber++ + ".png");
                            ImageIO.write(img, "png", outputfile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }).start();
        }

        @Override
        public void paint(Graphics g) {
            frame .setTitle(frame.getMousePosition() + "");
            Graphics2D frameG2 = (Graphics2D) g;
            if (displayRenderingProcess) frameG2.drawImage(img, 0, 0, null);
            else frameG2.drawImage(currentImage, 0, 0, null);
        }
    }
}
