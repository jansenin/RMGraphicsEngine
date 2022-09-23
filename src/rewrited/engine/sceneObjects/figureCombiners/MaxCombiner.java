package rewrited.engine.sceneObjects.figureCombiners;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;
import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class MaxCombiner extends FigureBase implements WithDefinedBoundingBox {
    private Figure figure1;
    private Figure figure2;

    public MaxCombiner(Figure figure1, Figure figure2) {
        this.figure1 = figure1;
        this.figure2 = figure2;
        setDistanceAndColorFunctions(
                (point) -> Math.max(figure1.getDistance(point), figure2.getDistance(point)),
                (point) -> figure1.getDistance(point) < figure2.getDistance(point) ? figure1.getColor(point) : figure2.getColor(point)
        );
    }

    @Override
    public boolean isInBoundingBox(Vec3d vector) {
        boolean inFigure1;
        boolean inFigure2;

        if (figure1 instanceof WithDefinedBoundingBox) inFigure1 = ((WithDefinedBoundingBox) figure1).isInBoundingBox(vector);
        else inFigure1 = figure1.getDistance(vector) < 0;
        if (figure2 instanceof WithDefinedBoundingBox) inFigure2 = ((WithDefinedBoundingBox) figure2).isInBoundingBox(vector);
        else inFigure2 = figure2.getDistance(vector) < 0;
        return inFigure1 || inFigure2;
    }
}