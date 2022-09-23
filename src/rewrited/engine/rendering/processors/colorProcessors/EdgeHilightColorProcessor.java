package rewrited.engine.rendering.processors.colorProcessors;

import rewrited.Utils.Utils;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;
import rewrited.engine.sceneObjects.figures.Figure;

import java.awt.*;

public class EdgeHilightColorProcessor extends ColorProcessorBase {
    private Color hilightColor = Color.RED;
    private double maxHilightDistance = 0.02; // magic constant

    public EdgeHilightColorProcessor() {
    }

    public EdgeHilightColorProcessor(Color hilightColor) {
        this.hilightColor = hilightColor;
    }

    public EdgeHilightColorProcessor(Color hilightColor, double maxHilightDistance) {
        this(hilightColor);
        this.maxHilightDistance = maxHilightDistance;
    }

    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        float hilightingKoefficient = 0;

        if (raymarchResult.intersectedFigure == null) {
            double minDistance = raymarchResult.steps.stream().map(raymarchStep -> raymarchStep.minDistanceToFigure).min(Double::compareTo).get();
            hilightingKoefficient = minDistance < maxHilightDistance ? 1 : 0;
            //(float)Math.pow(Math.min(raymarchResult.steps.size() / (float)magicConstant, 1), 3);
        } else {
            double firstMinDistance = raymarchResult.steps.get(raymarchResult.steps.size() - 1).minDistanceToFigure;
            double secondMinDistance = rp.getRaymarchParameters().getMaxDistance();
            Figure firstMinDistanceFigure = raymarchResult.intersectedFigure;
            Figure secondMinDistanceFigure = null;

            for (Raymarcher.RaymarchStep raymarchStep: raymarchResult.steps) {
                if (raymarchStep.minDistanceToFigure < secondMinDistance && raymarchStep.closestFigure != firstMinDistanceFigure) {
                    secondMinDistance = raymarchStep.minDistanceToFigure;
                    secondMinDistanceFigure = raymarchStep.closestFigure;
                }
            }

            hilightingKoefficient = 0;
            if (secondMinDistanceFigure != null) {
                if (secondMinDistance < maxHilightDistance) {
                    hilightingKoefficient = 1;
                }
            }
        }

        float curR = currentColor.getRed() / 255f;
        float curG = currentColor.getGreen() / 255f;
        float curB = currentColor.getBlue() / 255f;

        float deltaR = curR - hilightColor.getRed() / 255f;
        float deltaG = curG - hilightColor.getGreen() / 255f;
        float deltaB = curB - hilightColor.getBlue() / 255f;

        return new Color(
                Utils.clamp(curR - deltaR * hilightingKoefficient, 0, 1),
                Utils.clamp(curG - deltaG * hilightingKoefficient, 0, 1),
                Utils.clamp(curB - deltaB * hilightingKoefficient, 0, 1)
        );
    }
}
