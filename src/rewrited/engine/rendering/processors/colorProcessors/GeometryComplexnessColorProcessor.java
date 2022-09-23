package rewrited.engine.rendering.processors.colorProcessors;

import rewrited.Utils.ColorUtils;
import rewrited.Utils.Function2;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;

import java.awt.*;
import java.util.function.Function;

public class GeometryComplexnessColorProcessor extends ColorProcessorBase {
    private Function<Integer, Double> stepCountToIntensityFunction = (steps) -> 0.5 / (steps + 1) + 0.5;

    public GeometryComplexnessColorProcessor(Function<Integer, Double> stepCountToIntensityFunction) {
        this.stepCountToIntensityFunction = stepCountToIntensityFunction;
    }

    public GeometryComplexnessColorProcessor() {
    }

    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        return ColorUtils.multiply(currentColor, Math.min(1, stepCountToIntensityFunction.apply(raymarchResult.steps.size())));
    }
}
