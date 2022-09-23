package rewrited.engine;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.sceneObjects.figures.Figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {
    private List<Figure> figures = new ArrayList<>();

    public Scene() {}

    public List<Figure> getFigureList() {
        return figures;
    }

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void removeFigure(Figure figure) {
        figures.remove(figure);
    }

    public Figure getMinDistanceFigure(Vec3d point) {
        double minDistance = Double.MAX_VALUE;
        Figure closestFigure = null;
        for (Figure currentFigure : figures) {
            double curDistance = currentFigure.getDistance(point);
            if (curDistance < minDistance) {
                minDistance = curDistance;
                closestFigure = currentFigure;
            }
        }
        return closestFigure;
    }
}
