package com.example.backend.service.impl;

import java.util.Date;
import java.util.logging.Logger;
import java.text.ParseException;

import com.example.backend.exception.ValidateException;
import com.example.backend.service.AuthService;
import com.example.backend.viewModel.DecodeModel;
import com.example.backend.viewModel.LoginModel;
import com.example.backend.viewModel.TokenModel;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    static Logger logger = Logger.getLogger("AuthServiceImpl");

    @Value("${token.key}")
    byte[] key;

    @Override
    public TokenModel encode(String accountId, Long loginTime) {

        // Create the header
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);

        // Set the plain text
        Payload payload = new Payload(
                "{" + "\"accountId\":" + "\"" + accountId + "\"" + ",\"loginTime\":" + "\"" + loginTime + "\"" + "}");

        // Create the JWE object and encrypt it
        JWEObject jweObject = new JWEObject(header, payload);
        try {
            jweObject.encrypt(new DirectEncrypter(key));

            // Serialise to compact JOSE form...
            TokenModel result = new TokenModel();

            result.setToken(jweObject.serialize());

            return result;
        } catch (KeyLengthException e) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "KeyLengthException: " + e.toString(), null);
        } catch (JOSEException e) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "JOSEException: " + e.toString(), null);
        }
    }

    @Override
    public DecodeModel decode(String token) {

        try {
            DecodeModel result = new DecodeModel();

            // // Parse into JWE object
            JWEObject jweObject = JWEObject.parse(token);
            // Decrypt
            jweObject.decrypt(new DirectDecrypter(key));
            // Get the plain text
            Payload payload = jweObject.getPayload();

            JSONObject jsonObject = (JSONObject) payload.toJSONObject();

            result.setAccountId(jsonObject.get("accountId").toString());
            return result;
        } catch (ParseException e) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "ParseException: " + e.toString(), null);
        } catch (KeyLengthException e) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "KeyLengthException: " + e.toString(), null);
        } catch (JOSEException e) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "JOSEException: " + e.toString(), null);
        }
    }
}
