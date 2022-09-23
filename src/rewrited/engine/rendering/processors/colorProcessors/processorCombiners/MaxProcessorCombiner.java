package rewrited.engine.rendering.processors.colorProcessors.processorCombiners;

import rewrited.Utils.ColorUtils;
import rewrited.engine.rendering.processors.colorProcessors.ColorProcessor;

public class MaxProcessorCombiner extends ProcessorCombiner {
    public MaxProcessorCombiner(ColorProcessor processor1, ColorProcessor processor2) {
        super(processor1, processor2, ColorUtils::outOfMaxComponents);
    }
}
