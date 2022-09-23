package rewrited.engine.rendering.processors.colorProcessors;

import rewrited.engine.Scene;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;

import java.awt.*;

public interface ColorProcessor {
    Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor);
}
