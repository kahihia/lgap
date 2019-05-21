package edu.lenzing.lgap.microservice.admin.util;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public final class PasswordEncryptor {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordEncryptor.class);

    public static String generateHashedPassword(final String password, final JsonObject options) {

        try {
            final byte[] initialBytes = password.getBytes(options.getString("encoding", "UTF-8"));
            final MessageDigest algorithm = MessageDigest.getInstance(options.getString("algorithm", "SHA-256"));
            algorithm.reset();
            algorithm.update(initialBytes);
            final byte[] hashedBytes = algorithm.digest ();
            final StringBuilder strBuilder = new StringBuilder ();
            for (final byte b : hashedBytes) {
                strBuilder.append (String.format ("%02X", b));
            }
            return strBuilder.toString ();
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Password encryption failed: unsupported encoding", e);
        } catch (final NoSuchAlgorithmException nsae) {
            LOG.error("Password encryption failed: hashing algorithm not supported", nsae);
        }

        return null;

    }

}
