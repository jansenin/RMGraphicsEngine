package rewrited.Utils;

import com.sun.javafx.geom.Vec3d;

import java.awt.*;
import static rewrited.Utils.Utils.*;

public class ColorUtils {
    public static Color multiply(Color c, double m) {
        return new Color(
                clamp(c.getRed() * (float)m / 255f, 0, 1),
                clamp(c.getGreen() * (float)m / 255f, 0, 1),
                clamp(c.getBlue() * (float)m / 255f, 0, 1));
    }

    public static Color average(Color a, Color b) {
        return new Color(
                (a.getRed() + b.getRed()) / 2,
                (a.getGreen() + b.getGreen()) / 2,
                (a.getBlue() + b.getBlue()) / 2);
    }

    public static Color addColors(Color a, Color b) {
        float aRed = a.getRed() / 255f;
        float aGreen = a.getGreen() / 255f;
        float aBlue = a.getBlue() / 255f;

        float bRed = b.getRed() / 255f;
        float bGreen = b.getGreen() / 255f;
        float bBlue = b.getBlue() / 255f;

        return new Color(
                clamp((1 - aRed) * bRed + aRed, 0, 1),
                clamp((1 - aGreen) * bGreen + aGreen, 0, 1),
                clamp((1 - aBlue) * bBlue + aBlue, 0, 1));
    }

    public static Color multiply(Color a, Color b) {
        return new Color(
                clamp((a.getRed() / 255f) * (b.getRed() / 255f), 0, 1),
                clamp((a.getGreen() / 255f) * (b.getGreen() / 255f), 0, 1),
                clamp((a.getBlue() / 255f) * (b.getBlue() / 255f), 0, 1));
    }

    public static Color outOfMaxComponents(Color a, Color b) {
        return new Color(
                Math.max(a.getRed(), b.getRed()),
                Math.max(a.getGreen(), b.getGreen()),
                Math.max(a.getBlue(), b.getBlue())
        );
    }

    public static Color colorFn1(Vec3d p) {
        return new Color(
                Utils.clamp((float)Utils.mod((Math.sin(p.x)-2*Math.sin(2*p.y)+0.1*Math.sin(3*p.z)+2*Math.sin(4*p.x) / 16), 1), 0, 1),
                Utils.clamp((float)Utils.mod((Math.sin(p.y)-2*Math.sin(2*p.z)+0.1*Math.sin(3*p.x)+2*Math.sin(4*p.y) /  8), 1), 0, 1),
                Utils.clamp((float)Utils.mod((Math.sin(p.z)-2*Math.sin(2*p.x)+0.1*Math.sin(3*p.y)+2*Math.sin(4*p.z) /  4), 1), 0, 1)
        );
    }

    public static Color colorFn2(Vec3d p) {
        return new Color(
                ((int)(p.length()*p.length()*p.length() * 1000)) % 256,
                ((int)(p.length()*p.length() * 2000)) % 256,
                ((int)(p.length() * 4000)) % 256
        );
    }

    public static Color colorFn3(Vec3d p) {
        return new Color(
                ((int)(p.length()*p.length() * 1000)) % 256,
                ((int)(p.length() * 2000)) % 256,
                ((int)(p.length() * 4000)) % 256
        );
    }

    public static Color colorFn4(Vec3d point) {
        return new Color(
                ((int)(Math.sqrt(point.x*point.x + point.y*point.y) * 1000)) % 256,
                ((int)(Math.sqrt(point.x*point.x + point.z*point.z) * 1000)) % 256,
                ((int)(Math.sqrt(point.y*point.y + point.z*point.z) * 1000)) % 256
        );
    }

    public static Color colorFn5(Vec3d point) {
        return new Color(
                ((int)(Math.abs(point.x * 1000))) % 256,
                ((int)(Math.abs(point.y * 1000))) % 256,
                ((int)(Math.abs(point.z * 1000))) % 256
        );
    }
}
