package com.project.sunbasedata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sunbasedata.model.Details;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class ServiceClassImpl implements ServiceClass{
    @Override
    public String authenticationService(String username, String password) {
        CloseableHttpClient client = HttpClients.custom().build();

        HttpPost request = new HttpPost();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            request.setURI(builder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        JSONObject reqData = new JSONObject();
        reqData.put("login_id", username);
        reqData.put("password", password);

        StringEntity httpentity = new StringEntity(reqData.toString() , "utf-8");
        httpentity.setContentEncoding("UTF-8");
        httpentity.setContentType("application/json");

        request.setEntity(httpentity);

        CloseableHttpResponse httpresponse = null;
        try {
            httpresponse = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseData = null;
        try {
            responseData = EntityUtils.toString(httpresponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject json = new JSONObject(responseData);

        return json.getString("access_token");
    }

    @Override
    public List<Details> getCustomerListService(String access_token) {
        CloseableHttpClient client = HttpClients.custom().build();

        HttpGet request = new HttpGet();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.addParameter("cmd", "get_customer_list");
        try {
            request.setURI(builder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        CloseableHttpResponse httpresponse = null;
        try {
            httpresponse = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Details> list = null;
        try {
            list = mapper.readValue(EntityUtils.toString(httpresponse.getEntity()).trim() , new TypeReference<List<Details>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public String addCustomerService(Details details, String access_token) {
        CloseableHttpClient client = HttpClients.custom().build();

        HttpPost request = new HttpPost();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.addParameter("cmd", "create");
        try {
            request.setURI(builder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        String reqBody;
        try {
             reqBody= mapper.writeValueAsString(details);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        StringEntity httpentity = null;
        try {
            httpentity = new StringEntity(reqBody);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpentity.setContentEncoding("UTF-8");
        httpentity.setContentType("application/json");

        // request.addHeader( "Authorization" , access_token );
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);
        request.setEntity(httpentity);

        CloseableHttpResponse httpresponse = null;
        try {
            httpresponse = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseData = null;
        try {
            responseData = EntityUtils.toString(httpresponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(httpresponse.getStatusLine().getStatusCode());
        return responseData.trim();
    }

    @Override
    public String deleteCustomerService(String access_token, String uuid) {
        CloseableHttpClient client = HttpClients.custom().build();

        HttpPost request = new HttpPost();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.addParameter("cmd", "delete");
        builder.addParameter("uuid" , uuid);
        try {
            request.setURI(builder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        CloseableHttpResponse httpresponse = null;
        try {
            httpresponse = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseData = null;
        try {
            responseData = EntityUtils.toString(httpresponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(httpresponse.getStatusLine().getStatusCode());
        System.out.println(responseData.trim());
        return responseData.trim();
    }

    @Override
    public String updateCustomerService(String access_token, Details details) {
        CloseableHttpClient client = HttpClients.custom().build();

        HttpPost request = new HttpPost();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        builder.addParameter("cmd", "update");
        builder.addParameter("uuid", details.getUuid());
        try {
            request.setURI(builder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        String reqBody;
        try {
            reqBody= mapper.writeValueAsString(details);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        StringEntity httpentity = null;
        try {
            httpentity = new StringEntity(reqBody);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpentity.setContentEncoding("UTF-8");
        httpentity.setContentType("application/json");
        request.setEntity(httpentity);

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ access_token);

        CloseableHttpResponse httpresponse = null;
        try {
            httpresponse = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseData = null;
        try {
            responseData = EntityUtils.toString(httpresponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(httpresponse.getStatusLine().getStatusCode());
        System.out.println(responseData.trim());
        return null;
    }
}
