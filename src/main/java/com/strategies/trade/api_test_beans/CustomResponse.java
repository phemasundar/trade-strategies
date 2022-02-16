package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.strategies.trade.api_utils.RestApiUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
@Getter
@Setter
public class CustomResponse<T> {

    private int responseCode;
    private String response;
    private T responseObj;
    private List<T> responseObjList;
    private Class<T> type;

    public CustomResponse(HttpResponse httpResponse, Class<T> cls) throws IOException {

        type = cls;
        setResponse(httpResponse);
        setResponseCode(httpResponse);
        setResponseObj();
    }

    public CustomResponse(HttpResponse httpResponse) throws IOException {

        setResponse(httpResponse);
        setResponseCode(httpResponse);
    }

    public void setResponseCode(HttpResponse httpResponse) {
        this.responseCode = RestApiUtils.getResponseCode(httpResponse);
    }

    public void setResponse(HttpResponse httpResponse) throws IOException {
        this.response = RestApiUtils.getResponse(httpResponse);
    }

    public void setResponseObj() throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            if (mapper.readTree(response).isArray()) {
                responseObjList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    T t = mapper.readValue(jsonArray.get(i).toString(), type);
                    responseObjList.add(t);
                }
                //TODO: workAround
//                TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {};
//                responseObjList = mapper.readValue(response, typeReference);

            } else {
                this.responseObj = mapper.readValue(response, type);
            }
        } catch (JsonParseException | JSONException | MismatchedInputException e) {
            this.responseObj = null;
            this.responseObjList = null;
        }
    }
}
