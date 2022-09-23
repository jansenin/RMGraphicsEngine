package rewrited.engine.rendering.processors.raymarchingProcessors;

import rewrited.engine.rendering.Raymarcher;

public interface RaymarchingProcessor {
    void processDataForStep(Raymarcher.RaymarchStep.StartingData raymarchStartingData, Raymarcher.RaymarchParameters rp, Raymarcher.RaymarchResult result);

    void processStep(Raymarcher.RaymarchStep raymarchStep, Raymarcher.RaymarchParameters rp, Raymarcher.RaymarchResult result);

    void processResult(Raymarcher.RaymarchResult raymarchResult, Raymarcher.RaymarchParameters rp);
}
