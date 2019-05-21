package edu.lenzing.lgap.microservice.common.util;

import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class JsonUtil {

    public static String exceptionStackTraceToString(final Throwable throwable) {
        try (final StringWriter sw = new StringWriter(); final PrintWriter pw = new PrintWriter(sw, true)) {
            throwable.printStackTrace(pw);
            return sw.getBuffer().toString();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return throwable.getMessage();
    }

    public static Map<String, JsonObject> separateJsonContentByPrefix(final JsonObject json) {

        final Map<String, JsonObject> map = new HashMap<>();

        json.stream().forEach(entry -> {

            final String[] key = entry.getKey().split("\\.");

            final String prefix = key[0];
            final String name = key[1];

            JsonObject obj = map.get(prefix);

            if (obj == null) {
                obj = new JsonObject();
                map.put(prefix, obj);
            }

            obj.put(name, entry.getValue());

        });

        return map;

    }

}
