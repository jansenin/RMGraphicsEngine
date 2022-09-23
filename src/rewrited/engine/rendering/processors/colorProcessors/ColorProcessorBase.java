package rewrited.engine.rendering.processors.colorProcessors;

import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;
import rewrited.engine.rendering.processors.colorProcessors.processorCombiners.AddProcessorCombiner;
import rewrited.engine.rendering.processors.colorProcessors.processorCombiners.AverageProcessorCombiner;
import rewrited.engine.rendering.processors.colorProcessors.processorCombiners.MaxProcessorCombiner;
import rewrited.engine.rendering.processors.colorProcessors.processorCombiners.MultiplyProcessorCombiner;

import java.awt.*;

public abstract class ColorProcessorBase implements ColorProcessor {
    @Override
    public abstract Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor);

    public ColorProcessorBase add(ColorProcessor processor2) {
        return new AddProcessorCombiner(this, processor2);
    }

    public ColorProcessorBase average(ColorProcessor colorProcessor2) {
        return new AverageProcessorCombiner(this, colorProcessor2);
    }

    public ColorProcessorBase max(ColorProcessor colorProcessor2) {
        return new MaxProcessorCombiner(this, colorProcessor2);
    }

    public ColorProcessorBase multiply(ColorProcessor colorProcessor2) {
        return new MultiplyProcessorCombiner(this, colorProcessor2);
    }

    public ColorProcessorBase then(ColorProcessor colorProcessor2) {
        return new ColorProcessorBase() {
            @Override
            public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
                return colorProcessor2.process(raymarchResult, rp, process(raymarchResult, rp, currentColor));
            }
        };
    }
}
