package com.toandv98.checksum.data.hash;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

public class HashSHA384 implements Hash {
    @Override
    public String hash(String input) {
        return new String(Hex.encodeHex(DigestUtils.sha384(input)));
    }

    @Override
    public String hash(InputStream inputStream) throws IOException {
        return new String(Hex.encodeHex(DigestUtils.sha384(inputStream)));
    }
}
