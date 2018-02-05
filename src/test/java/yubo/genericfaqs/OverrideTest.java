package yubo.genericfaqs;

/**
 * The compiler decides whether a subtype method overrides or overloads
 * a supertype method when it compiles the generic subtype, independently
 * of any instantiation of the generic subtype.
 */
public class OverrideTest {

    private static class Box<T> {
        private T theThing;

        public Box(T t) {
            this.theThing = t;
        }

        public void reset(T t) {
            this.theThing = t;
        }
    }

    private static class WordBox<S extends CharSequence> extends Box<String> {
        public WordBox(S t) {
            super(t.toString().toLowerCase());
        }

        public void reset(S t) {
            super.reset(t.toString().toLowerCase());
        }
    }

    public static void main(String[] args) {
        WordBox<String> city = new WordBox<>("Beijing");
        //city.reset("Shanghai"); //error.
    }

}
