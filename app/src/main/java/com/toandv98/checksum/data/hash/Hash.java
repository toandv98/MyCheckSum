package com.toandv98.checksum.data.hash;

import java.io.IOException;
import java.io.InputStream;

public interface Hash {
    String hash(String input);

    String hash(InputStream inputStream) throws IOException;
}
