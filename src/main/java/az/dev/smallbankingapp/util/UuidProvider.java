package az.dev.smallbankingapp.util;

import java.util.UUID;

public final class UuidProvider {

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    private UuidProvider() {
    }

}
