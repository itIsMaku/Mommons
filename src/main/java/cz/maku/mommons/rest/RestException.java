package cz.maku.mommons.rest;

import com.google.common.annotations.Beta;

@Beta
public class RestException extends Exception {

    public RestException(String message) {
        super(message);
    }

}
