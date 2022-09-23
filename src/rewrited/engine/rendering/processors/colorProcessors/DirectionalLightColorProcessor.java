package rewrited.engine.rendering.processors.colorProcessors;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.ColorUtils;
import rewrited.Utils.Utils;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;
import rewrited.engine.rendering.processors.colorProcessors.processorData.LightProcessorData;
import rewrited.engine.sceneObjects.WithDefinedNormal;
import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.XYPlane;

import java.awt.*;

public class DirectionalLightColorProcessor extends ColorProcessorBase {
    private Vec3d position;
    private double intensity;
    private Color color;

    public DirectionalLightColorProcessor(Vec3d position, double intensity, Color color) {
        this.position = position;
        this.intensity = intensity;
        this.color = color;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public Vec3d getPosition() {
        return new Vec3d(position);
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public DirectionalLightColorProcessor(Vec3d position, double intensity) {
        this(position, intensity, Color.WHITE);
    }

    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        if (raymarchResult.intersectedFigure == null) return currentColor;
        if (raymarchResult.startedInFigure) return Color.BLACK;
        Vec3d endPoint = raymarchResult.endPoint;
        Raymarcher.RaymarchResult lightRaymarch;
        Vec3d direction = new Vec3d(endPoint);
        direction.sub(position);
        direction.normalize();
        lightRaymarch = Raymarcher.raymarch(rp.getRaymarchParameters(), position, direction);

        if (lightRaymarch.startedInFigure) return Color.BLACK;

        if (
                (lightRaymarch.intersectedFigure == raymarchResult.intersectedFigure) &&
                Utils.isItEqualPoints(
                        raymarchResult.endPoint,
                        lightRaymarch.endPoint,
                        rp.getRaymarchParameters().getMinDistance() * 1e2
                )
        ) {
            double diffuseKoefficient = 0.7;
            double specularKoefficient = 0.3;
            double specularity = 0;
            double ambientKoefficient = 0.2;
            if (lightRaymarch.intersectedFigure instanceof LightProcessorData) {
                diffuseKoefficient = ((LightProcessorData) lightRaymarch.intersectedFigure).getDiffuseKoefficient();
                specularKoefficient = ((LightProcessorData) lightRaymarch.intersectedFigure).getSpecularKoefficient();
                specularity = ((LightProcessorData) lightRaymarch.intersectedFigure).getSpecularity();
                ambientKoefficient = ((LightProcessorData) lightRaymarch.intersectedFigure).getAmbientKoefficient();
            }


            double colorIntensity;
            Vec3d normal;
            if (!(lightRaymarch.intersectedFigure instanceof WithDefinedNormal)) {
                normal = Utils.getNormal(lightRaymarch.intersectedFigure, endPoint);
            } else {
                normal = ((WithDefinedNormal) lightRaymarch.intersectedFigure).getNormal(endPoint);
            }
            colorIntensity = -(float)normal.dot(direction);
            if (colorIntensity < 0) colorIntensity = 0;
            colorIntensity = specularKoefficient * Utils.sign(colorIntensity) * Math.pow(colorIntensity, specularity) + diffuseKoefficient * colorIntensity + ambientKoefficient;
            Color colorWithLight = ColorUtils.multiply(currentColor, ColorUtils.multiply(color, intensity));
            return ColorUtils.multiply(colorWithLight, colorIntensity);
        }
        return Color.BLACK;
    }
}
