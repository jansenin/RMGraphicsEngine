package rewrited.engine.rendering.processors.colorProcessors.processorCombiners;

import rewrited.Utils.Function2;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;
import rewrited.engine.rendering.processors.colorProcessors.ColorProcessor;
import rewrited.engine.rendering.processors.colorProcessors.ColorProcessorBase;

import java.awt.*;

public class ProcessorCombiner extends ColorProcessorBase {
    protected ColorProcessor processor1;
    protected ColorProcessor processor2;
    protected Function2<Color, Color, Color> colorMixer;

    public ProcessorCombiner(ColorProcessor processor1, ColorProcessor processor2, Function2<Color, Color, Color> colorMixer) {
        this.processor1 = processor1;
        this.processor2 = processor2;
        this.colorMixer = colorMixer;
    }

    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        Color color1 = processor1.process(raymarchResult, rp, currentColor);
        Color color2 = processor2.process(raymarchResult, rp, currentColor);
        return colorMixer.apply(color1, color2);
    }
}
