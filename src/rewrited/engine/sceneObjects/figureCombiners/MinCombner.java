package rewrited.engine.sceneObjects.figureCombiners;

import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class MinCombner extends FigureBase {
    public MinCombner(Figure figure1, Figure figure2) {
        setDistanceAndColorFunctions(
                (point) -> Math.min(figure1.getDistance(point), figure2.getDistance(point)),
                (point) -> figure1.getDistance(point) < figure2.getDistance(point) ? figure1.getColor(point) : figure2.getColor(point)
        );
    }
}
