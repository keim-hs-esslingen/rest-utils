/*
 * MIT License
 * 
 * Copyright (c) 2020 Hochschule Esslingen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 */
package de.hsesslingen.keim.restutils;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class is a builder class for building requests. It makes use of Springs
 * RestTemplate for this purpose but provides a builder style API for creating
 * requests.
 * <p>
 * This class uses a {@code new RestTemplate()} by default for making requests.
 * If you want to configure an own default RestTemplate, see futher down in this
 * javadoc.
 * <p>
 * Although {@link AbstractRequest} provides everything needed to build the
 * requests, this class is still abstract. The reason for this is, that all
 * methods are set to {@code protected} instead of public, makings its API
 * rather useless if used directly. To use this class, use {@link Request}
 * instead, which is nothing else but a child class of {@link AbstractRequest}
 * that publishes all provided methods using {@code public} and delegates them
 * to {@link AbstractRequest}.
 * <p>
 * Why did I make the methods <i>protected</i> in the first place? Well actually
 * they weren't in the first place. I made them protected later on, to extend
 * the possible use cases of this class. Instead of using {@link Request} to
 * build all-pupose requests, you can now make own child classes of
 * {@link AbstractRequest} and decide on your own, which parts of the building
 * API should be published or not.
 * <p>
 * If desired you could even create a specific class for each kind of request
 * that you want to make and provide new methods that collect those params
 * needed for just your request. On the inside they are put to the correct place
 * of the request by using the internal API. This hides away the specific
 * structure of the HTTP request to the caller of the request function and
 * simplifies concentration on business logic. This is just an idea. Of course,
 * use this class as it fits your use case best.
 * <p>
 * <h2>How to configure custom default RestTemplate</h2>
 * You have two options:
 * <ol>
 * <li>Use the static {@link
 * AbstractRequest#setDefaultRestTemplate(RestTemplate)} method to set the
 * default RestTemplate.</li>
 * <li>Derive a child class from this class, publish the desired API methods by
 * delegating them to {@code super} and making them {@code public} and <u>return
 * a different RestTemplate in {@code getRestTemplate()}</u>.</li>
 * </ol>
 *
 * @author boesch
 * @param <T> The kind of object that is expected to be returned by the called
 * REST-API. When creating an instance of this class (or of {@link Request} or
 * any derived child class), do not try to pass this type param to the
 * constructor. This will not work. Instead use the
 * {@link expect(java.lang.Class)} and
 * {@link expect(org.springframework.core.ParameterizedTypeReference)} methods
 * to define the expected return type.
 */
public abstract class AbstractRequest<T> {

    /**
     * Will be initialized in getRestTemplate() as soon as this method is called
     * the first time.
     */
    private static RestTemplate restTemplate;

    /**
     * Can be used to replace the default RestTemplate with a custom one.
     *
     * @param restTemplate
     */
    public static void setDefaultRestTemplate(RestTemplate restTemplate) {
        AbstractRequest.restTemplate = restTemplate;
    }

    /**
     * Uses default values only.
     * <ul>
     * <li>Method = GET</li>
     * <li>Content Type = application/json</li>
     * <li>URI encode charset = UTF-8 (encoding enabled by default)</li>
     * </ul>
     */
    protected AbstractRequest() {
    }

    /**
     * Uses default values and sets the requests method an uri.
     *
     * @param method
     * @param uri
     */
    protected AbstractRequest(HttpMethod method, String uri) {
        this.uriBuilder.uri(URI.create(uri));
        this.method = method;
    }

    /**
     * Uses default values and sets the requests method an uri.
     *
     * @param method
     * @param uri
     */
    protected AbstractRequest(HttpMethod method, URI uri) {
        this.uriBuilder.uri(uri);
        this.method = method;
    }

    /**
     * Uses default values and sets the requests method.
     *
     * @param method
     */
    protected AbstractRequest(HttpMethod method) {
        this.method = method;
    }

    /**
     * This creates a new AbstractRequest object with the specified URI and the
     * default http method GET.
     *
     * @param uri
     */
    protected AbstractRequest(String uri) {
        this.uriBuilder.uri(URI.create(uri));
    }

    /**
     * This creates a new AbstractRequest object with the specified URI and the
     * default http method GET.
     *
     * @param uri
     */
    protected AbstractRequest(URI uri) {
        this.uriBuilder.uri(uri);
    }

    /**
     * Returns the default RestTemplate. this method can be overridden by child
     * classes to provide different rest templates.
     *
     * @return
     */
    protected RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        return restTemplate;
    }

    private UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
    private Charset uriEncodeCharset = Charset.forName("UTF-8");
    private boolean encodeComponents = true;

    private HttpMethod method = HttpMethod.GET;
    private HttpEntity entity;
    private Object body;
    private String contentType = MediaType.APPLICATION_JSON_VALUE;
    private final HttpHeaders headers = new HttpHeaders();

    private Object[] uriVariables;
    private Map<String, ?> uriVariablesMap;

    private Class responseTypeClass = Object.class;
    private ParameterizedTypeReference responseTypeReference;

    /**
     * Triggers the build of this request and sends it using the RestTemplate
     * returned by {@link getRestTemplate()}.
     * <p>
     * It begins with merging the given {@link body}, {@link entity} and
     * {@link headers}. In this process, the headers provided directly using the
     * given methods are merged together with the headers already contained in
     * {@link entity}, and either the body contained in the entity of the body
     * provided directly (if any) will be used for the request. The body in the
     * entity is favored above the directly given body, even if it is null.
     * <p>
     * Actually to make this clear: If you provide a body using
     * {@link body(java.lang.Object)} any entity set is discarded to null.
     * Subsequently as soon as you provide and entity using
     * {@link entity(org.springframework.http.HttpEntity)} any body provided
     * before will be discarded too. This is done to simplify the building
     * process of the request.
     * <p>
     * After merging these request parts, the headers are checked for the
     * presence of a Content-Type value. If there is no value provided, it will
     * add the one given in {@link contentType}. This value is set to
     * {@code application/json} by default and can be changed using
     * {@link contentType(java.lang.String)}. If you want to omit this header,
     * put {@code null} into this method.
     * <p>
     * After setting the Content-Type header, the URI is generated by calling
     * the {@link buildUri()} method. This method can be called from outside as
     * well to retrieve the URI that is going to be used in this request (e.g.
     * for logging purposes).
     * <p>
     * After generating the URI, the request is sent using the URI, the
     * configured method, the newly built entity containing headers and body and
     * the expected response type.
     *
     * @return
     */
    protected ResponseEntity<T> go() {
        // Merge headers and set final body...
        Object requestBody;

        if (entity != null) {
            headers.addAll(entity.getHeaders());
            requestBody = entity.getBody();
        } else {
            requestBody = body;
        }

        // If we are going to send a body, check for header "Content-Type":
        if (requestBody != null) {
            // Search existing headers for "Content-Type" key:
            boolean isContentTypeSet = headers.keySet()
                    .stream()
                    .anyMatch(k -> k.equals(HttpHeaders.CONTENT_TYPE));

            // If there isn't any Content-Type header yet, set the one defined in the class variable.
            // This can be null as well, but it will only be null, if the user set it to be so.
            if (!isContentTypeSet && contentType != null) {
                // Add default header that sets JSON as content type.
                headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            }
        }

        // Create final request entity with final body and headers...
        HttpEntity requestEntity = new HttpEntity(requestBody, headers);

        // Build uri...
        URI uri = buildUri();

        // Make request...
        if (responseTypeReference != null) {
            return getRestTemplate().exchange(uri, method, requestEntity, responseTypeReference);
        } else {
            return getRestTemplate().exchange(uri, method, requestEntity, responseTypeClass);
        }
    }

    /**
     * Used by {@link go()} to build the configured URI from the provided values
     * that are relevant for this task. {@link buildUri()} can also be used from
     * outside to retrieve the URI for e.g. logging purposes.
     *
     * @return
     */
    protected URI buildUri() {
        if (uriBuilder == null) {
            uriBuilder = UriComponentsBuilder.newInstance();
        }

        if (uriVariablesMap != null) {
            return uriBuilder.build(uriVariablesMap);
        }

        if (uriVariables != null) {
            return uriBuilder.build(uriVariables);
        }

        UriComponentsBuilder builder = uriBuilder;

        if (uriEncodeCharset != null) {
            builder = uriBuilder.encode(uriEncodeCharset);
        }

        return builder.build(!encodeComponents).toUri();
    }

    /**
     * Makes this request being encoded with the given Charset. The charset is
     * by default set to UTF-8.
     *
     * @param charset
     * @return
     */
    protected AbstractRequest<T> uriEncodeCharset(Charset charset) {
        this.uriEncodeCharset = charset;
        return this;
    }

    /**
     * Sets an internal flag, that prevents the internal UriComponentsBuilder
     * from enconding its URI parts in {@link buildUri()}.
     *
     * @return
     */
    protected AbstractRequest<T> preventUriEncoding() {
        this.encodeComponents = false;
        return this;
    }

    /**
     * Sets the method of this request.
     *
     * @param method
     * @return
     */
    protected AbstractRequest<T> method(HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * Sets the URI of this request.
     *
     * @param uri
     * @return
     */
    protected AbstractRequest<T> uri(String uri) {
        this.uriBuilder.uri(URI.create(uri));
        return this;
    }

    /**
     * Sets the URI of this request.
     *
     * @param uri
     * @return
     */
    protected AbstractRequest<T> uri(URI uri) {
        this.uriBuilder.uri(uri);
        return this;
    }

    /**
     * Sets the entity to be sent with this request. Clears {@link body}.
     *
     * @param entity
     * @return
     */
    protected AbstractRequest<T> entity(HttpEntity entity) {
        this.entity = entity;
        this.body = null;
        return this;
    }

    /**
     * Sets the body to be sent with this request. Clears {@link entity}.
     *
     * @param body
     * @return
     */
    protected AbstractRequest<T> body(Object body) {
        this.body = body;
        this.entity = null;
        return this;
    }

    /**
     * Sets headers to be sent with this request. They will be merged together
     * with the headers from {@link entity} if there are any.
     *
     * @param headersToAdd
     * @return
     */
    protected AbstractRequest<T> headers(Map<String, String> headersToAdd) {
        for (String key : headersToAdd.keySet()) {
            this.headers.add(key, headersToAdd.get(key));
        }

        return this;
    }

    /**
     * Sets headers to be sent with this request. They will be merged together
     * with the headers from {@link entity} if there are any.
     *
     * @param key
     * @param values
     * @return
     */
    protected AbstractRequest<T> headers(String key, List<? extends String> values) {
        this.headers.addAll(key, values);
        return this;
    }

    /**
     * Sets headers to be sent with this request. They will be merged together
     * with the headers from {@link entity} if there are any.
     *
     * @param key
     * @param value
     * @return
     */
    protected AbstractRequest<T> header(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    /**
     * Sets the content type of the request to the specified value. If no type
     * is specified, which includes settings the content type using the
     * {@code header/s}-methods, a default content type of
     * {@code MediaType.APPLICATION_JSON_UTF8_VALUE} will be used.
     * <p>
     * If you want to omit the content type header, call this function with
     * {@code null}.
     * <p>
     * For more content types look at the static values in class
     * {@code MediaType}.
     *
     * @param contentType
     * @return
     */
    protected AbstractRequest<T> contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Add the given query string to the query of the internal uri builder. URI
     * variables may be used.
     *
     * @param query
     * @return
     */
    protected AbstractRequest<T> query(String query) {
        uriBuilder.query(query);
        return this;
    }

    /**
     * Add the given query string to the query of the internal uri builder.URI
     * variables may be used.
     *
     * @param key
     * @param value
     * @return
     */
    protected AbstractRequest<T> query(String key, Object value) {
        uriBuilder.queryParam(key, value);
        return this;
    }

    /**
     * Add the given query string to the query of the internal uri builder.URI
     * variables may be used.
     *
     * @param key
     * @param values
     * @return
     */
    protected AbstractRequest<T> query(String key, Object... values) {
        uriBuilder.queryParam(key, values);
        return this;
    }

    /**
     * Add the given query string to the query of the internal uri builder.URI
     * variables may be used.
     *
     * @param params
     * @return
     */
    protected AbstractRequest<T> query(MultiValueMap<String, String> params) {
        uriBuilder.queryParams(params);
        return this;
    }

    /**
     * Set the uri variables that shall be expanded upon sending the request.
     *
     * @param uriVariables
     * @return
     */
    protected AbstractRequest<T> uriVariables(Object... uriVariables) {
        this.uriVariables = uriVariables;
        return this;
    }

    /**
     * Set the uri variables that shall be expanded upon sending the request.
     *
     * @param uriVariables
     * @return
     */
    protected AbstractRequest<T> uriVariables(Map<String, ?> uriVariables) {
        this.uriVariablesMap = uriVariables;
        return this;
    }

    /**
     * Sets the expected return type of this request. This allows Spring to
     * automatically parse the returned result.
     *
     * @param <R>
     * @param responseTypeClass
     * @return
     */
    protected <R> AbstractRequest<R> expect(Class<R> responseTypeClass) {
        this.responseTypeClass = responseTypeClass;
        return (AbstractRequest<R>) this; // Casting to more specific type. T usually will be "Object".
    }

    /**
     * Sets the expected return type of this request. This allows Spring to
     * automatically parse the returned result.
     *
     * @param <R>
     * @param responseTypeReference
     * @return
     */
    protected <R> AbstractRequest<R> expect(ParameterizedTypeReference<R> responseTypeReference) {
        this.responseTypeReference = responseTypeReference;
        return (AbstractRequest<R>) this; // Casting to more specific type. T usually will be "Object".
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Returns the internally used {@link uriBuilder}.
     *
     * @return
     */
    protected UriComponentsBuilder uriBuilder() {
        return uriBuilder;
    }

    /**
     * Returns the internally stored {@link method}.
     *
     * @return
     */
    protected HttpMethod method() {
        return method;
    }

    /**
     * Returns the internally stored {@link entity}.
     *
     * @return
     */
    protected HttpEntity entity() {
        return entity;
    }

    /**
     * Returns the internally stored {@link body}.
     *
     * @return
     */
    protected Object body() {
        return body;
    }

    /**
     * Returns the internally stored {@link contentType}.
     *
     * @return
     */
    protected String contentType() {
        return contentType;
    }

    /**
     * Returns the internally stored {@link headers}.
     *
     * @return
     */
    protected HttpHeaders headers() {
        return headers;
    }

    /**
     * Returns the internally stored {@link uriVariables}.
     *
     * @return
     */
    protected Object[] uriVariables() {
        return uriVariables;
    }

    /**
     * Returns the internally stored {@link uriVariablesMap}.
     *
     * @return
     */
    protected Map<String, ?> uriVariablesMap() {
        return uriVariablesMap;
    }

    /**
     * Returns the internally stored {@link responseTypeClass}.
     *
     * @return
     */
    protected Class<T> responseTypeClass() {
        return responseTypeClass;
    }

    /**
     * Returns the internally stored {@link responseTypeReference}.
     *
     * @return
     */
    protected ParameterizedTypeReference<T> responseTypeReference() {
        return responseTypeReference;
    }
    //</editor-fold>
}
