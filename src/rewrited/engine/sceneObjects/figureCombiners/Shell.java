package rewrited.engine.sceneObjects.figureCombiners;

import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class Shell extends FigureBase {
    private double shellThickness;

    public Shell(Figure figure, double shellThickness) {
        this.shellThickness = shellThickness;
        setDistanceAndColorFunctions(point -> Math.abs(figure.getDistance(point)) - this.shellThickness / 2, figure::getColor);
    }

    public double getShellThickness() {
        return shellThickness;
    }

    public void setShellThickness(double shellThickness) {
        this.shellThickness = shellThickness;
    }
}
