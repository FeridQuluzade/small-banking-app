package az.dev.smallbankingapp.util;

import java.util.UUID;

public final class UuidProvider {

    private UuidProvider() {
    }

    public static String generate() {
        return UUID.randomUUID().toString();
    }

}
