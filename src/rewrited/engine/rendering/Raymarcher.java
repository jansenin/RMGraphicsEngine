package rewrited.engine.rendering;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.Scene;
import rewrited.engine.cameras.Camera;
import rewrited.engine.rendering.processors.raymarchingProcessors.RaymarchingProcessor;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;
import rewrited.engine.sceneObjects.figures.Figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Raymarcher {
    public static class RaymarchParameters {
        Scene scene;
        Camera camera;
        private double minDistance = 1e-2; // default
        private double maxDistance = 1e7; // default
        private int maxRaymarchStepCount = 500; // default
        private List<RaymarchingProcessor> raymarchingProcessors = new ArrayList<>();

        public RaymarchParameters(Scene scene, Camera camera) {
            this.scene = scene;
            this.camera = camera;
        }

        public RaymarchParameters(Scene scene, Camera camera, double minDistance, double maxDistance) {
            this(scene, camera);
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
        }

        public RaymarchParameters(Scene scene, Camera camera, List<RaymarchingProcessor> raymarchingProcessors) {
            this(scene, camera);
            this.raymarchingProcessors = raymarchingProcessors;
        }

        public RaymarchParameters(Scene scene, Camera camera, double minDistance, double maxDistance, List<RaymarchingProcessor> raymarchingProcessors) {
            this(scene, camera, minDistance, maxDistance);
            this.raymarchingProcessors = raymarchingProcessors;
        }

        public Scene getScene() {
            return scene;
        }

        public RaymarchParameters setScene(Scene scene) {
            this.scene = scene;
            return this;
        }

        public Camera getCamera() {
            return camera;
        }

        public RaymarchParameters setCamera(Camera camera) {
            this.camera = camera;
            return this;
        }

        public List<RaymarchingProcessor> getRaymarchingProcessors() {
            return raymarchingProcessors;
        }

        public RaymarchParameters setRaymarchingProcessors(List<RaymarchingProcessor> raymarchingProcessors) {
            this.raymarchingProcessors = raymarchingProcessors;
            return this;
        }

        public RaymarchParameters addRaymarchingProcessor(RaymarchingProcessor processor) {
            raymarchingProcessors.add(processor);
            return this;
        }

        public RaymarchParameters removeRaymarchingProcessor(RaymarchingProcessor processor) {
            raymarchingProcessors.remove(processor);
            return this;
        }

        public double getMinDistance() {
            return minDistance;
        }

        public RaymarchParameters setMinDistance(double minDistance) {
            this.minDistance = minDistance;
            return this;
        }

        public double getMaxDistance() {
            return maxDistance;
        }

        public RaymarchParameters setMaxDistance(double maxDistance) {
            this.maxDistance = maxDistance;
            return this;
        }

        public int getMaxRaymarchStepCount() {
            return maxRaymarchStepCount;
        }

        public RaymarchParameters setMaxRaymarchStepCount(int maxRaymarchStepCount) {
            if (maxRaymarchStepCount <=
                    0) throw new IllegalArgumentException("maxRaymarchStepCount must be greater than zero");
            this.maxRaymarchStepCount = maxRaymarchStepCount;
            return this;
        }
    }

    public static class RaymarchStep {
        public static class StartingData {
            public Vec3d startPoint;
            public Vec3d direction; // length = 1

            public StartingData(Vec3d startPoint, Vec3d direction) {
                this.startPoint = startPoint;
                this.direction = direction;
            }
        }
        public Figure closestFigure;
        public double minDistanceToFigure;
        public Vec3d startPoint;
        public Vec3d step;
        public Vec3d normalizedDirection;
        public Vec3d endPoint;
        public Map<Figure, Double> distancesToFigureBorders;

        public RaymarchStep(
                Figure closestFigure,
                double minDistanceToFigure,
                Vec3d startPoint,
                Vec3d endPoint,
                Vec3d step,
                Vec3d normalizedDirection,
                Map<Figure, Double> distancesToFigureBorders) {
            this.closestFigure = closestFigure;
            this.minDistanceToFigure = minDistanceToFigure;
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.step = step;
            this.normalizedDirection = normalizedDirection;
            this.distancesToFigureBorders = distancesToFigureBorders;
        }

        @Override
        public String toString() {
            return "RaymarchStep: {" +
                    "\n   closestFigure=" + closestFigure +
                    "\n   , minDistanceToFigure=" + minDistanceToFigure +
                    "\n   , startPoint=" + startPoint +
                    "\n   , step=" + step +
                    "\n   , endPoint=" + endPoint +
                    "\n   , normalizedDirection=" + normalizedDirection +
                    "\n   , distancesToFigureBorders=" + distancesToFigureBorders +
                    "\n}";
        }
    }

    public static class RaymarchResult {
        public List<RaymarchStep> steps = new ArrayList<>();
        public List<Object> raymarchProcessorData = new ArrayList<>();
        public Figure intersectedFigure = null;
        public boolean startedInFigure = false;
        public Vec3d startPoint = null;
        public Vec3d endPoint = null;
        public double finalDistance;

        public RaymarchResult() {
        }

        private void putStep(RaymarchStep step) {
            steps.add(step);
            endPoint = step.endPoint;
        }

        @Override
        public String toString() {
            return "RaymarchResult{" +
                    "steps=\n" + steps +
                    ",\nintersectedFigure=" + intersectedFigure +
                    ",\nstartedInFigure=" + startedInFigure +
                    ",\nendPoint=" + endPoint +
                    ",\nstartPoint=" + startPoint +
                    ",\nfinalDistance=" + finalDistance +
                    '}';
        }
    }

    private static boolean isSomeDistancesBecameLower(RaymarchStep step1, RaymarchStep step2) {
        return step1.distancesToFigureBorders.keySet().stream().anyMatch(figure -> step2.distancesToFigureBorders.get(figure) < step1.distancesToFigureBorders.get(figure));
    }

    private static boolean isInSomeFiguresBoundingBox(List<Figure> figures, Vec3d vector) {
        for (Figure figure : figures) {
            if (figure instanceof WithDefinedBoundingBox) {
                if (((WithDefinedBoundingBox) figure).isInBoundingBox(vector)) return true;
            }
        }
        return false;
    }

    private static void processStartingData(RaymarchStep.StartingData startingData, RaymarchParameters rp, RaymarchResult result) {
        for (RaymarchingProcessor processor : rp.raymarchingProcessors) {
            processor.processDataForStep(startingData, rp, result);
        }
    }

    private static void processStep(RaymarchStep step, RaymarchParameters rp, RaymarchResult result) {
        for (RaymarchingProcessor processor : rp.raymarchingProcessors) {
            processor.processStep(step, rp, result);
        }
    }

    private static void processResult(RaymarchResult result, RaymarchParameters rp) {
        for (RaymarchingProcessor processor : rp.raymarchingProcessors) {
            processor.processResult(result, rp);
        }
    }

    public static RaymarchResult raymarch(RaymarchParameters rp, Vec3d point, Vec3d direction) {
        RaymarchResult result = new RaymarchResult();
        List<Figure> figures = rp.scene.getFigureList();
        point = new Vec3d(point);
        direction = new Vec3d(direction);
        direction.normalize();

        RaymarchStep.StartingData sd = new RaymarchStep.StartingData(point, direction);
        processStartingData(sd, rp, result);

        result.startPoint = new Vec3d(sd.startPoint);

        RaymarchStep prevStep = getRaymarchStep(rp.scene, sd.startPoint, sd.direction);
        processStep(prevStep, rp, result);
        result.intersectedFigure = prevStep.closestFigure;
        if (prevStep.closestFigure == null) {
            processResult(result, rp);
            return result;
        }
        if (prevStep.minDistanceToFigure < rp.minDistance) {
            result.startedInFigure = true;
            result.endPoint = sd.startPoint;
            result.finalDistance = prevStep.minDistanceToFigure;
            processResult(result, rp);
            return result;
        }
        result.putStep(prevStep);
        sd.startPoint = new Vec3d(prevStep.endPoint);
        sd.direction = new Vec3d(prevStep.normalizedDirection);
        processStartingData(sd, rp, result);
        RaymarchStep curStep = getRaymarchStep(rp.scene, sd.startPoint, sd.direction);
        processStep(curStep, rp, result);
        result.intersectedFigure = curStep.closestFigure;
        if (rp.maxRaymarchStepCount == 1 || curStep.minDistanceToFigure < rp.minDistance) {
            result.intersectedFigure = null;
            if (curStep.minDistanceToFigure < rp.minDistance) {
                result.intersectedFigure = curStep.closestFigure;
            }
            result.endPoint = prevStep.endPoint;
            result.finalDistance = curStep.minDistanceToFigure;
            processResult(result, rp);
            return result;
        }
        sd.startPoint = new Vec3d(curStep.endPoint);
        sd.direction = new Vec3d(curStep.normalizedDirection);
        processStartingData(sd, rp, result);

        while ((isSomeDistancesBecameLower(prevStep, curStep) || isInSomeFiguresBoundingBox(figures, sd.startPoint)) && result.steps.size() < rp.maxRaymarchStepCount && curStep.endPoint.length() < rp.maxDistance) {
            prevStep = curStep;
            curStep = getRaymarchStep(rp.scene, sd.startPoint, sd.direction);
            processStep(curStep, rp, result);
            result.putStep(prevStep);
            result.endPoint = prevStep.endPoint;
            result.intersectedFigure = curStep.closestFigure;
            result.finalDistance = curStep.minDistanceToFigure;
            if (curStep.minDistanceToFigure < rp.minDistance) {
                processResult(result, rp);
                return result;
            }
            sd.startPoint = new Vec3d(curStep.endPoint);
            sd.direction = new Vec3d(curStep.normalizedDirection);
            processStartingData(sd, rp, result);
        }
        result.intersectedFigure = null;
        return result;
    }

    private static RaymarchStep getRaymarchStep(Scene scene, Vec3d point, Vec3d direction) {
        List<Figure> figures = scene.getFigureList();
        double minDistance = Double.MAX_VALUE;
        Figure closestFigure = null;
        Map<Figure, Double> distancesToFigureBorders = new HashMap<>(figures.size());
        for (Figure currentFigure : figures) {
            double curDistance = currentFigure.getDistance(point);
            distancesToFigureBorders.put(currentFigure, curDistance);
            if (curDistance < minDistance) {
                minDistance = curDistance;
                closestFigure = currentFigure;
            }
        }
        Vec3d normalizedDirection = new Vec3d(direction);
        normalizedDirection.normalize();
        direction = new Vec3d(normalizedDirection);
        direction.mul(minDistance);
        Vec3d endPoint = new Vec3d(point);
        endPoint.add(direction);
        return new RaymarchStep(closestFigure, minDistance, new Vec3d(point), endPoint, direction, normalizedDirection, distancesToFigureBorders);
    }
}
