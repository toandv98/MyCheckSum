package com.toandv98.checksum.data.entities;

import com.toandv98.checksum.data.hash.HashType;

public class HashResult {
    private HashType type;
    private String result;

    public HashResult(HashType type, String result) {
        this.type = type;
        this.result = result;
    }

    public HashType getType() {
        return type;
    }

    public void setType(HashType type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
