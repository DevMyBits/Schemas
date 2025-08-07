/**
 * Créer le : vendredi 14 février 2025
 * Auteur : Yoann Meclot (DevMyBits)
 * E-mail : devmybits@gmail.com
 */
public class Schemas
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
        public Authority authority(String authority)
        {
            if (authority.contains(":") || authority.contains("/")) throw new IllegalArgumentException("Invalid input. Authority must not have specific characters ':' and '/'.");

            StringBuilder builder = new StringBuilder();
            builder.append("schemas://").append(authority);

            return new Authority(builder);
        }

        public static final class Authority
        {
            private final StringBuilder builder;

            public Authority append(String key, short value)
            {
                if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

                builder.append("/").append(key).append(":").append(value);
                return this;
            }

            public Authority append(String key, int value)
            {
                if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

                builder.append("/").append(key).append(":").append(value);
                return this;
            }

            public Authority append(String key, long value)
            {
                if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

                builder.append("/").append(key).append(":").append(value);
                return this;
            }

            public Authority append(String key, boolean value)
            {
                if (invalidInput(key)) throw error("Invalid input. The key must not have specific characters ':' and '/'.");

                builder.append("/").append(key).append(":").append(value);
                return this;
            }

            public Authority append(String key, String value)
            {
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

            Authority(StringBuilder builder)
            {
                this.builder = builder;
            }
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
