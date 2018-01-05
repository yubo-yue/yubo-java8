package yubo.unsafe.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;


/**
 * This sample showcases the basic field layout.
 * you can see a few notable things here:
 * 1, how much the object header consumes
 * 2, how fields are laid out
 * 3, how the external alignment beefs up the object size
 *
 * Source copied from http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/.
 * Only for learning purpose.
 *
 */
public class JOLSample_01_Basic {

    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(A.class).toPrintable());
    }

    public static class A {
        boolean f;
    }
}
