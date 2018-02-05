package yubo.effectivejava.v3;

public class Calzone extends Pizza {
    private final boolean sourceInside;

    private Calzone(final Builder builder) {
        super(builder);
        this.sourceInside = builder.sourceInside;
    }

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sourceInside = false;

        public Builder sourceInside() {
            this.sourceInside = true;
            return this;
        }

        @Override
        Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
