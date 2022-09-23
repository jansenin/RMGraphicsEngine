package rewrited.engine.sceneObjects.figureCombiners;

import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class InversedFigure extends FigureBase {
    public InversedFigure(Figure figure) {
        setDistanceAndColorFunctions(
                (point) -> -figure.getDistance(point),
                figure::getColor
        );
    }
}