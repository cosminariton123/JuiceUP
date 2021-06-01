package com.example.juiceup;

import android.os.StrictMode;

import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class CriptographyService {

    public CriptographyService(){

    }

    public String hash(String to_be_hashed, String saltb64_string){

        String hash_return = "";

        try {
            byte[] to_be_hashed_bytes = to_be_hashed.getBytes("UTF-8");
            byte[] salt_bytes = b64string_to_bytes(saltb64_string);

            byte[] to_be_hashed_with_salt_bytes = new byte[to_be_hashed_bytes.length + salt_bytes.length];

            int contor = 0;
            for (byte elem:
                 to_be_hashed_bytes) {
                to_be_hashed_with_salt_bytes[contor] = elem;
                contor++;
            }


            for (byte elem:
                 salt_bytes) {
                to_be_hashed_with_salt_bytes[contor] = elem;
                contor++;
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashbytes = digest.digest(to_be_hashed_with_salt_bytes);

            hash_return = bytes_to_b64string(hashbytes);
            return hash_return;
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return hash_return;
    }


    public String get_random_salt(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[30];

        random.nextBytes(bytes);

        String salt;
        salt = bytes_to_b64string(bytes);
        return salt;
    }

    public String bytes_to_b64string (byte[] hashbytes){

        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(hashbytes);
    }

    public byte[] b64string_to_bytes (String hash64){

        Decoder decoder = Base64.getDecoder();
        return decoder.decode(hash64);
    }
}
