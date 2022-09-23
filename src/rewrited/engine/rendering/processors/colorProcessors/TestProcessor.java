package rewrited.engine.rendering.processors.colorProcessors;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.ColorUtils;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.Renderer;

import java.awt.*;

public class TestProcessor extends ColorProcessorBase {
    @Override
    public Color process(Raymarcher.RaymarchResult raymarchResult, Renderer.RenderParameters rp, Color currentColor) {
        if (raymarchResult.steps.get(raymarchResult.steps.size() - 1).normalizedDirection.dot(new Vec3d(0, 0, 1)) > 0.999) {
            System.out.println("------------------------------");
            System.out.println(raymarchResult);
            System.out.println("------------------------------");
        }
        /*
        System.out.println("----------------------------------");
        System.out.println(raymarchResult.endPoint);
        System.out.println(raymarchResult.steps.get(raymarchResult.steps.size() - 1).startPoint);
        System.out.println(raymarchResult.steps.get(raymarchResult.steps.size() - 1).endPoint);
        System.out.println("----------------------------------");
        */
        Vec3d v = new Vec3d(raymarchResult.steps.get(0).startPoint);
        v.sub(raymarchResult.endPoint);

        return ColorUtils.multiply(Color.WHITE, v.length() / 10);
    }
}
