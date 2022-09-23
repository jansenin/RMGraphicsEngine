package rewrited.engine.sceneObjects.figureCombiners;

import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class ShelledFigure extends FigureBase {
    private double shellThickness = 0;

    public ShelledFigure(Figure figure, double shellThickness) {
        this.shellThickness = shellThickness;
        setDistanceAndColorFunctions(
                (point) -> figure.getDistance(point) - this.shellThickness,
                figure::getColor
        );
    }

    public double getShellThickness() {
        return shellThickness;
    }

    public void setShellThickness(double shellThickness) {
        this.shellThickness = shellThickness;
    }
}