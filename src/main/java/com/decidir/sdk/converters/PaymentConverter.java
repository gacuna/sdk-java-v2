package com.decidir.sdk.converters;

import com.decidir.sdk.exceptions.DecidirException;
import com.decidir.sdk.exceptions.responses.PaymentException;
import com.decidir.sdk.exceptions.responses.ResponseException;
import retrofit2.Response;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.decidir.sdk.exceptions.DecidirError;
import com.decidir.sdk.dto.DecidirResponse;
import com.decidir.sdk.dto.payments.PaymentResponse;
/**
 * Created by biandra on 08/07/16.
 */
public class PaymentConverter extends DecidirConverter {
    public static final int HTTP_402 = 402;

    public <A> DecidirResponse<A>  convert(Response<A> response, A body) {
        DecidirResponse<A> dResponse = new DecidirResponse();
        dResponse.setStatus(response.code());
        dResponse.setResult(body);
        dResponse.setMessage(response.message());
        return dResponse;
    }

    public DecidirResponse<DecidirError> convertError(Response response) throws IOException {
        DecidirError decidirError = super.convert(response.errorBody().bytes(), DecidirError.class);
        return this.convert(response,decidirError);
    }

    public <A,E extends RuntimeException & ResponseException> DecidirResponse<A> convertOrThrowSpecError(Response<A> response, Class specError, Class responseErr)
            throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (response.isSuccessful()) {
            return this.convert(response, response.body());
        } else {
            if (response.code() == HTTP_402){
                Constructor<E> ctor = specError.getConstructor(int.class, String.class, responseErr);
                throw ctor.newInstance(response.code(), response.message(), super.convert(response.errorBody().bytes(), responseErr));
            } else {
                DecidirResponse<DecidirError> error = this.convertError(response);
                throw DecidirException.wrap(error.getStatus(), error.getMessage(), error.getResult());
            }
        }
    }

}
