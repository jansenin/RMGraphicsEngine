package rewrited.engine.rendering.processors.colorProcessors.processorCombiners;

import rewrited.Utils.ColorUtils;
import rewrited.engine.rendering.processors.colorProcessors.ColorProcessor;

public class MultiplyProcessorCombiner extends ProcessorCombiner {
    public MultiplyProcessorCombiner(ColorProcessor processor1, ColorProcessor processor2) {
        super(processor1, processor2, ColorUtils::multiply);
    }
}
