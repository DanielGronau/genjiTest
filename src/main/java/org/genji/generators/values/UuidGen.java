package org.genji.generators.values;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

public class UuidGen implements Generator<UUID> {

    public static final UuidGen INSTANCE = new UuidGen();

    private UuidGen() {
    }

    @Override
    public Stream<UUID> generate(Random random, TypeInfo typeInfo) {
        return Stream.iterate(randomUUID(random), uuid -> UUID.randomUUID());
    }

    private static UUID randomUUID(Random random) {
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        randomBytes[6]  &= 0x0f;
        randomBytes[6]  |= 0x40;
        randomBytes[8]  &= 0x3f;
        randomBytes[8]  |= 0x80;
        return uuidFrom(randomBytes);
    }

    private static UUID uuidFrom(byte[] data) {
        long msb = 0;
        long lsb = 0;
        for (int i=0; i<8; i++) {
            msb = (msb << 8) | (data[i] & 0xff);
        }
        for (int i=8; i<16; i++) {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }
}
