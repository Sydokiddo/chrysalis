package net.sydokiddo.chrysalis.util.technical;

import com.mojang.serialization.Codec;

public class CodecUtils {

    public static <Type> Codec<Type> merge(Codec<? extends Type> codec1, Codec<? extends Type> codec2) {
        return Codec.either(codec1, codec2).flatComapMap(
            either -> {
                if (either.left().isPresent()) return either.left().get();
                if (either.right().isPresent()) return either.right().get();
                return null;
            }, null
        );
    }
}