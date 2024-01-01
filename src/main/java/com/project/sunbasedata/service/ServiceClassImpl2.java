package com.project.sunbasedata.service;

import com.project.sunbasedata.model.Details;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Primary
@Service
public class ServiceClassImpl2 implements ServiceClass{

    @Autowired
    private WebClient webClient;
    @Override
    public String authenticationService(String username, String password) {

        JSONObject reqData = new JSONObject();
        reqData.put("login_id", username);
        reqData.put("password", password);

        String responseData = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(reqData.toString()) , String.class)
                .retrieve()
                .bodyToMono(String.class).block();

        JSONObject json = new JSONObject(responseData);
        return json.getString("access_token");
    }

    @Override
    public List<Details> getCustomerListService(String access_token) {
        try {
            URIBuilder builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
            builder.addParameter("cmd", "get_customer_list");
            Flux<Details> details= webClient.get()
                    .uri(builder.build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION , "Bearer "+ access_token)
                    .retrieve()
                    .bodyToFlux(Details.class);
//            details.subscribe(System.out::println);
            return details.collectList().block();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addCustomerService(Details details, String access_token) {
        try {
            URIBuilder builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
            builder.addParameter("cmd", "create");
            Mono<String> response =  webClient.post()
                    .uri(builder.build())
                    .header(HttpHeaders.AUTHORIZATION , "Bearer "+ access_token)
                    .body(Mono.just(details) , Details.class)
                    .retrieve()
                    .bodyToMono(String.class);
            System.out.println(Objects.requireNonNull(response.block()).trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String deleteCustomerService(String access_token, String uuid) {
        try {
            URIBuilder builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
            builder.addParameter("cmd", "delete");
            builder.addParameter("uuid" , uuid);

            Mono<String> response =  webClient.post()
                    .uri(builder.build())
                    .header(HttpHeaders.AUTHORIZATION , "Bearer "+ access_token)
                    .retrieve()
                    .bodyToMono(String.class);
            System.out.println(Objects.requireNonNull(response.block()).trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String updateCustomerService(String access_token, Details details) {
        try {
            URIBuilder builder = new URIBuilder("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp");
            builder.addParameter("cmd", "update");
            builder.addParameter("uuid", details.getUuid());
            Mono<String> response =  webClient.post()
                    .uri(builder.build())
                    .header(HttpHeaders.AUTHORIZATION , "Bearer "+ access_token)
                    .body(Mono.just(details) , Details.class)
                    .retrieve()
                    .bodyToMono(String.class);
            System.out.println(Objects.requireNonNull(response.block()).trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
