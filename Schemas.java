/**
 * Créer le : vendredi 14 février 2025
 * Auteur : Yoann Meclot (DevMyBits)
 * E-mail : yoannmeclot@hotmail.com
 */
public final class Schemas
{
    private final Evaluator<String, Segment> segments = new Evaluators<>();

    private final String schemas;

    private String authority;

    public Schemas(String schemas)
    {
        if (schemas == null) throw new NullPointerException("Schemas is null");

        int i = schemas.indexOf(':');
        if (schemas.charAt(i + 1) != '/') throw new NullPointerException("Invalid schemas !");

        this.schemas = schemas;
        parse(i);
    }

    @Override
    public String toString()
    {
        return schemas;
    }

    public String getAuthority()
    {
        return authority;
    }

    public String getSegmentValue(String key)
    {
        Segment segment = segments.evaluate(key);
        if (segment == null) return null;
        return segment.value;
    }

    private void parse(int index)
    {
        int length = schemas.length();
        if (length > index + 2 && schemas.charAt(index + 1) == '/' && schemas.charAt(index + 2) == '/')
        {
            String uri = schemas.substring(index + 3);
            int i = uri.indexOf('/');

            authority = uri.substring(0, i);

            String data = uri.substring(i + 1);
            int dl = data.length();

            int count = 0;
            while (true) {
                int e = data.indexOf(':');
                int sl = data.indexOf('/');
                if (sl < 0) sl = data.length();

                String key = data.substring(0, e);
                String value = data.substring(++e, sl);

                segments.push(new Segment(key, value));

                count += ++sl;
                if (count >= dl) break;
                data = data.substring(sl);
            }
            return;
        }
        throw new IllegalArgumentException("Invalid schemas !");
    }

    public static final class Builder
    {
        private StringBuilder builder;

        public Builder authority(String authority)
        {
            if (builder != null) throw new NullPointerException("You don't redefine authority !");
            if (invalidInput(authority)) throw error("Invalid input. Authority must not have specific characters ':' and '/'.");

            builder = new StringBuilder();
            builder.append("schemas://").append(authority);

            return this;
        }

        public Builder append(String key, short value)
        {
            if (builder == null) throw new NullPointerException("Define authority before !");
            if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

            builder.append("/").append(key).append(":").append(value);
            return this;
        }

        public Builder append(String key, int value)
        {
            if (builder == null) throw new NullPointerException("Define authority before !");
            if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

            builder.append("/").append(key).append(":").append(value);
            return this;
        }

        public Builder append(String key, long value)
        {
            if (builder == null) throw new NullPointerException("Define authority before !");
            if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

            builder.append("/").append(key).append(":").append(value);
            return this;
        }

        public Builder append(String key, boolean value)
        {
            if (builder == null) throw new NullPointerException("Define authority before !");
            if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

            builder.append("/").append(key).append(":").append(value);
            return this;
        }

        public Builder append(String key, String value)
        {
            if (builder == null) throw new NullPointerException("Define authority before !");
            if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

            builder.append("/").append(key).append(":").append(value);
            return this;
        }

        public Schemas build()
        {
            return new Schemas(builder.toString());
        }

        private boolean invalidInput(String text)
        {
            return text.contains(":") || text.contains("/");
        }

        private IllegalArgumentException error(String message)
        {
            return new IllegalArgumentException(message);
        }
    }

    private record Segment(String key, String value) implements Evaluator.Evaluable<String>
    {
        @Override
        public boolean toEvaluate(String key)
        {
            return this.key.equals(key);
        }
    }
}
