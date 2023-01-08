package com.iexceed.uiframework.rest;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

/**
 * Wrapper class for Rest Assured API Test Automation
  * @author Ankita */

public class RestAssuredWrapper {
    private static RequestSpecification request;
    private static Logger LOGGER = LogManager.getLogger(RestAssuredWrapper.class);

    /**Sets Base URI  and BasePath Before Starting test, we should set the RestAssured baseURI
     *
     * @param baseURI pass the baseURI
     * @param basePathTerm pass the basePath
     */

    public static void setBaseURI(String baseURI, String basePathTerm) {
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePathTerm;
    }

    /**
     * Sets Base URI
     * @param baseURI pass the baseURI
     */
    public static void setBaseURI(String baseURI) {
        RestAssured.given().baseUri(baseURI);

    }

    /**
     * Sets Base Path
     * @param basePathTerm pass the basePath
     */

    public static void setBasePath(String basePathTerm) {
        RestAssured.given().basePath(basePathTerm);

    }
    /**
     * Reset Base URI**** After test, we should reset the RestAssured baseURI
     */
    public static void resetBaseUri() {
        RestAssured.reset();
    }
    /**
     *  Reset Base Path**** After test, we should reset the RestAssured basePath
     */
    public static void resetBasePath() {
        RestAssured.reset();
    }


    /**
     * Sets Content Type
     *  We should set the content type to json or xml before starting test
     * @param contentType can be application/json or text/plain
     */

    public static void setContentType(String contentType) {
        RestAssured.given().contentType(contentType);

    }

    /**
     * We send "path" as parameter to Rest Assured and "get" method returns the RestAPI response
     * @param path pss the path
     * @return method returns the RestAPI response
     */
    public static Response getResponse(String path) {
        return RestAssured.get(path);
    }

    /**
     * JsonPath is to Json what xpath is to XML, extract the part of a given document
     * @param res pass the response of API
     * @return return part of a given document
     */
    public static JsonPath getJsonPath(Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }

    /**
     * Get request with json content type
     * @param path pass the path param
     * @return response of an API
     */
    public static Response getRequestWithJsonContentType(String path) {
        LOGGER.info("Rest EndPoint for GET Request is" + RestAssured.baseURI + RestAssured.basePath + path + "\n");
        Response response = RestAssured.given().when().log().all().get(path);
        return response;
    }

    /**
     * validate the status code
     * @param response pass the response of API
     * @param statusCode  pass the status code as int
     */
    public static void StatusCodeTest(Response response, int statusCode) {
        LOGGER.info("#### Validating Status Code #####" + "\n");
        response.then().statusCode(statusCode);

    }

    /**
     * Assert Json Response Contains String
     * @param response pass the response of API
     * @param substring pass substring to assert
     */
    public static void assertJsonResponseWithContainsString(Response response, String substring) {
        LOGGER.info("#### Validating Contains Substring #####" + "\n");
        response.then().body(containsString(substring));
    }

    /**
     * Assert Json Response with non null value
     * @param response pass the response of API
     * @param key pass the key as string
     */
    public static void assertJsonResponseWithNonNullValue(Response response, String key) {
        LOGGER.info("#### Validating Contains Non null  #####" + "\n");
        response.then().body("$", hasKey(key));
    }

    /**
     * Assert Json Response with equal to string
     * @param response pass the response of API
     * @param jsonKey  pass the key as string
     * @param jsonValue pass the value as string
     */
    public static void assertJsonResponseWithEqualToString(Response response, String jsonKey, String jsonValue) {
        LOGGER.info("#### Validating With EqualToString  #####" + "\n");
        response.then().body(jsonKey, equalTo(jsonValue));
    }
    /**
     * Assert Json Response with less to string
     * @param response pass the response of API
     * @param jsonKey  pass the key as string
     * @param jsonValue pass the value as string
     */
    public static void assertJsonResponseWithLessThan(Response response, String jsonKey, String jsonValue) {
        LOGGER.info("#### Validating LessThan  #####" + "\n");
        response.then().body(jsonKey, lessThan(jsonValue));
    }
    /**
     * Assert Json Response with has the item
     * @param response pass the response of API
     * @param jsonKey  pass the key as string
     * @param jsonValue pass the value as string
     */
    public static void assertJsonResponseWithHasItemsString(Response response, String jsonKey, String jsonValue) {
        LOGGER.info("#### Validating Has Item String  #####" + "\n");
        response.then().body(jsonKey, hasItem(jsonValue));

    }

    /**
     * Post the request with Json content type
     * @param path
     * @param postMap
     * @param headerMap Request headers passed to post Request
     * @return
     */
    public static Response postRequestWithJsonContentType(String path, Map<Object, Object> postMap, Map<String, String> headerMap) {
        LOGGER.info("Post Request" + postMap + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        Response response = RestAssured.given().headers(headerMap).body(postMap).log().all().when().post(path);
        return response;
    }

    public static Response postRequestWithJsonContentType(String path, Object postMap, Map<String, String> headerMap) {
        LOGGER.info("Post Request" + postMap + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        Response response = RestAssured.given().contentType("application/json").headers(headerMap).body(postMap).log().all().when().post(path);
        return response;
    }

    public static Response postRequestWithJsonContentType(String postRequest) {
        LOGGER.info("Post Request" + postRequest + "\n");
        Response response = RestAssured.given().body(postRequest).when().post();
        return response;
    }

    public static Response getRequestWithQueryParamAndHeaders(String path, Map<String, String> headerMap, Map<String, String> QueryParamMap) {
        LOGGER.info("Firing Get Request");
        LOGGER.info("Request headers passed to GET Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to GET Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).log().all().when().get(path);
        return response;
    }

    public static Response getRequestWithPathParamAndHeaders(String path, Map<String, String> headerMap, Map<String, String> pathParam) {
        LOGGER.info("Firing Get Request");
        LOGGER.info("Request headers passed to GET Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to GET Request are" + pathParam + "\n");
        Response response = RestAssured.given().headers(headerMap).when().get(path, pathParam);
        return response;
    }

    public static Response postRequestWithQueryParamAndHeaders(String path, Map<Object, Object> postMap, Map<String, String> headerMap, Map<String, String> QueryParamMap) {
        LOGGER.info("Firing post Request " + postMap + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to post Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).body(postMap).when().post(path);
        return response;
    }

    public static Response postRequestWithPathParamAndHeaders(String path, Object postMap, Map<String, String> headerMap, String pathParam) {
        LOGGER.info("Firing post Request " + postMap + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        LOGGER.info("Path Params passed to post Request are" + pathParam + "\n");
        Response response = RestAssured.given().headers(headerMap).body(postMap).when().post(path, pathParam);
        return response;
    }

    public static Response postRequestWithQueryParamAndHeaders(String path, String postRequest, Map<String, String> headerMap, Map<String, String> QueryParamMap) {
        LOGGER.info("Firing post Request " + postRequest + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to post Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).body(postRequest).when().post(path);
        return response;
    }


    public static Response postRequestWithPathParamAndHeaders(String path, String postRequest, Map<String, String> headerMap, Map<String, String> pathParamMap) {
        LOGGER.info("Firing post Request " + postRequest + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to post Request are" + pathParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).pathParams(pathParamMap).body(postRequest).when().post(path);
        return response;
    }

    public static Response postRequestWithMultipartPathParamAndHeaders(String path, String filename, String filePath, Map<String, String> headerMap, Map<String, String> pathParamMap) {
        LOGGER.info("Firing post Request " + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        LOGGER.info("Path Params passed to post Request are" + pathParamMap + "\n");
        Response response = RestAssured.given().multiPart(filename, new File(filePath)).headers(headerMap).pathParam(path, pathParamMap).when().post(path, pathParamMap);
        return response;
    }

    public static Response postRequestWithContentTypeMultiPart(String path, Object postMap, Map<String, String> headerMap, String filePath, String pathParam) {
        LOGGER.info("Firing post Request " + "\n");
        LOGGER.info("Request headers passed to post Request are" + headerMap + "\n");
        LOGGER.info("Path Params passed to post Request are" + pathParam + "\n");
        Response response = RestAssured.given().headers(headerMap).multiPart("file", new File(filePath), "text/csv").when().post(path, pathParam);
        return response;
    }

    /**
     * Delete request with Post
     **/
    public static Response deleteRequestWithQueryParamAndHeadersWithPost(String path, Map<Object, Object> postMap, Map<String, String> headerMap, Map<String, String> QueryParamMap) {
        LOGGER.info("Firing delete Request " + postMap + "\n");
        LOGGER.info("Request headers passed to delete Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to delete Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).body(postMap).log().all().when().delete(path);
        return response;
    }

    public static Response deleteRequestWithPostAsString(String path, String postRequest, Map<String, String> headerMap, Map<String, String> QueryParamMap) {
        LOGGER.info("Firing delete Request " + postRequest + "\n");
        LOGGER.info("Request headers passed to delete Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to delete Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).body(postRequest).log().all().when().delete(path);
        return response;
    }

    public static Response deleteRequestWithPathParamAndHeadersWithPost(String path, Map<Object, Object> postMap, Map<String, String> headerMap, Map<String, String> pathParamMap) {
        LOGGER.info("Firing delete Request " + postMap + "\n");
        LOGGER.info("Request headers passed to delete Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to delete Request are" + pathParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).pathParams(pathParamMap).body(postMap).log().all().when().delete(path);
        return response;
    }

    public static Response deleteRequestWithQueryParamAndHeaders(String path, Map<String, String> headerMap, Map<String, String> QueryParamMap) {

        LOGGER.info("Request headers passed to delete Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to delete Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).log().all().when().delete(path);
        return response;
    }

    public static Response deleteRequestWithPathParamAndHeaders(String path, Map<String, String> headerMap, Map<String, String> pathParamMap) {
        LOGGER.info("Request headers passed to delete Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to delete Request are" + pathParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).pathParams(pathParamMap).log().all().when().delete(path);
        return response;
    }

    public static Response putRequestWithQueryParamAndHeaders(String path, String putRequest, Map<String, String> headerMap, Map<String, String> QueryParamMap) {
        LOGGER.info("Firing put Request " + putRequest + "\n");
        LOGGER.info("Request headers passed to put Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to put Request are" + QueryParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).params(QueryParamMap).body(putRequest).when().put(path);
        return response;
    }


    public static Response putRequestWithPathParamAndHeaders(String path, String putRequest, Map<String, String> headerMap, Map<String, String> pathParamMap) {
        LOGGER.info("Firing put Request " + putRequest + "\n");
        LOGGER.info("Request headers passed to put Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to put Request are" + pathParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).pathParams(pathParamMap).body(putRequest).when().put(path);
        return response;
    }
    public static Response putRequestWithObjPathParamAndHeaders(String path, Object putRequest, Map<String, String> headerMap, Map<String, String> pathParamMap) {
        LOGGER.info("Firing put Request " + putRequest + "\n");
        LOGGER.info("Request headers passed to put Request are" + headerMap + "\n");
        LOGGER.info("Query Params passed to put Request are" + pathParamMap + "\n");
        Response response = RestAssured.given().headers(headerMap).pathParams(pathParamMap).body(putRequest).log().all().when().put(path);
        return response;
    }

    public static Headers getHeaders(Response respose) {
        Headers headers = respose.getHeaders();
        return headers;

    }

    public static boolean hasHeaderInResponse(Response response, String headerName) {
        boolean flag = response.getHeaders().hasHeaderWithName(headerName);
        return flag;

    }

    public static void matchesSchemaJsonInResponse(Response response, String locationJsonPath) {
        LOGGER.info("#####Validating Json Schema In Classpath ########");
        response.then().body(matchesJsonSchemaInClasspath(locationJsonPath));

    }

    /**
     * Generates the unique UUID
     * @return returns random uuid generated
     */

    public static UUID generateUniqueUUID() {
        LOGGER.info("#####Generating unique ID ########");
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public String getResponseValue(Response response, String key) {
        return response.body().jsonPath().get(key);
    }

    /**
     *  Generates the unique email id
     * @return genearted unique email id
     */
    public static String generateUniqueEmail() {
        LOGGER.info("#####Generating unique EmailID ########");
        String ename = RandomStringUtils.randomAlphabetic(10);
        String domain = "i-exceed.com";
        return ename + "@" + domain;
    }
}