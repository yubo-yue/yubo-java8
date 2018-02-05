package yubo.effectivejava.v3;

import java.util.EnumSet;
import java.util.Set;

public class Text {
    public static enum Style {
        BOLD, ITALIC, UNDERLINE
    }

    public void applyStyle(Set<Style> styles) {
        for (Style style : styles) {
            System.out.printf("Stype %s applied%n", style);
        }
    }

    public static void main(String[] args) {
        new Text().applyStyle(EnumSet.of(Style.BOLD, Style.UNDERLINE));
        new Text().applyStyle(EnumSet.of(Style.BOLD));
    }
}
