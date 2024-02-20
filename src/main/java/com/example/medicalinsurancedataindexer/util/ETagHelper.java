package com.example.medicalinsurancedataindexer.util;

import org.apache.commons.codec.digest.DigestUtils;

public class ETagHelper {
    public static String generateETag(String value) {
        return DigestUtils.md5Hex(value);
    }
}
