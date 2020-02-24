package com.toandv98.checksum.data.hash;

public class HashFactory {

    public static Hash getHash(HashType type) {
        switch (type) {
            case SHA1:
                return new HashSHA1();
            case SHA256:
                return new HashSHA256();
            case SHA384:
                return new HashSHA384();
            case SHA512:
                return new HashSHA512();
            default:
                return new HashMD5();
        }
    }

}
