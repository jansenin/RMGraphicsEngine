package rewrited.engine.rendering.processors.colorProcessors;

import rewrited.Utils.ColorUtils;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;

import java.awt.*;

public class DistanceColorProcessor extends ColorProcessorBase {
    double halfLightDistance;
    boolean processEmptySpace = false;

    public DistanceColorProcessor(double halfLightDistance) {
        this.halfLightDistance = halfLightDistance;
    }

    public DistanceColorProcessor(double halfLightDistance, boolean processEmptySpace) {
        this(halfLightDistance);
        this.processEmptySpace = processEmptySpace;
    }

    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        if (raymarchResult.intersectedFigure == null) return currentColor;
        if(raymarchResult.intersectedFigure != null) {
            double distance = raymarchResult.steps.stream().map(raymarchStep -> raymarchStep.step.length()).reduce(0.0, Double::sum);
            float colorIntensity = 1f / (float)Math.pow(2, distance / halfLightDistance);
            return ColorUtils.multiply(currentColor, colorIntensity);
        } else if (processEmptySpace) return Color.BLACK;
        return currentColor;
    }
}
