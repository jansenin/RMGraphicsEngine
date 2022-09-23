package rewrited.engine.rendering;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.rendering.processors.colorProcessors.ColorProcessor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    public static class RenderParameters {
        private Raymarcher.RaymarchParameters raymarchParameters;
        private List<ColorProcessor> colorProcessors = new ArrayList<>();
        //private Color sceneColor = new Color(0, 30, 42); // default
        private Color sceneColor = new Color(0, 185, 254); // default
        private int pixelsPerRay = 1;

        public RenderParameters(Raymarcher.RaymarchParameters raymarchParameters) {
            this.raymarchParameters = raymarchParameters;
        }

        public RenderParameters(Raymarcher.RaymarchParameters raymarchParameters, List<ColorProcessor> colorProcessors) {
            this(raymarchParameters);
            this.colorProcessors = colorProcessors;
        }

        public List<ColorProcessor> getColorProcessors() {
            return colorProcessors;
        }

        public RenderParameters setColorProcessors(List<ColorProcessor> colorProcessors) {
            this.colorProcessors = colorProcessors;
            return this;
        }

        public Raymarcher.RaymarchParameters getRaymarchParameters() {
            return raymarchParameters;
        }

        public RenderParameters setRaymarchParameters(Raymarcher.RaymarchParameters raymarchParameters) {
            this.raymarchParameters = raymarchParameters;
            return this;
        }

        public Color getSceneColor() {
            return sceneColor;
        }

        public RenderParameters setSceneColor(Color sceneColor) {
            this.sceneColor = sceneColor;
            return this;
        }

        public int getPixelsPerRay() {
            return pixelsPerRay;
        }

        public RenderParameters setPixelsPerRay(int pixelsPerRay) {
            if (pixelsPerRay <= 0) throw new IllegalArgumentException("pixelsPerRay must be greater than zero");
            this.pixelsPerRay = pixelsPerRay;
            return this;
        }

        public RenderParameters addColorProcessor(ColorProcessor colorProcessor) {
            colorProcessors.add(colorProcessor);
            return this;
        }

        public RenderParameters removeColorProcessor(ColorProcessor colorProcessor) {
            colorProcessors.remove(colorProcessor);
            return this;
        }
    }
    private RenderParameters rp;

    public Renderer(RenderParameters renderParameters) {
        this.rp = renderParameters;
    }

    public RenderParameters getRp() {
        return rp;
    }

    public void setRp(RenderParameters rp) {
        this.rp = rp;
    }

    public void render(BufferedImage img) {
        Graphics2D g = ((Graphics2D) img.getGraphics());
        int w = img.getWidth();
        int h = img.getHeight();
        int pixelsPerRay = rp.pixelsPerRay;
        for(int x = 0;x < w;x += pixelsPerRay) {
            for (int y = 0; y < h; y += pixelsPerRay) {
                if (x == 10 && y == 10) {
                    int a = 1;
                }
                Vec3d direction = rp.raymarchParameters.camera.getDirectionVectorFromDisplayCoordinate(w, h, x, y);
                Vec3d position = rp.raymarchParameters.camera.getPosition();
                Raymarcher.RaymarchResult raymarchResult = Raymarcher.raymarch(rp.raymarchParameters, position, direction);
                Color color = rp.sceneColor;
                for (ColorProcessor colorProcessor : rp.colorProcessors) {
                    color = colorProcessor.process(raymarchResult, rp, color);
                }
                g.setColor(color);
                g.drawLine(x, h - y, x, h - y);
                for (int i = 0; i < pixelsPerRay; i++) {
                    g.drawLine(x, h - (y + i), x + pixelsPerRay - 1, h - (y + i));
                }
            }
        }
    }
}
