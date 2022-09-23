package rewrited.engine.rendering.processors.colorProcessors;

import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;

import java.awt.*;

public class FigureColorProcessor extends ColorProcessorBase {
    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        if (raymarchResult.startedInFigure) return raymarchResult.intersectedFigure.getColor(raymarchResult.endPoint);
        if (raymarchResult.intersectedFigure == null) return rp.getSceneColor();
        return raymarchResult.intersectedFigure.getColor(raymarchResult.steps.get(raymarchResult.steps.size() - 1).endPoint);
    }
}
