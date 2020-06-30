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
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This is the default class for building arbitrary requests. It does nothing
 * else but changing the visibility method of the protected methods in
 * {@link AbstractRequest} to public and providing some static starter methods
 * for the build chain.
 * <p>
 * Either use the provided construtors or the static starter methods to get
 * going with building a request. The final method to kick off the request is
 * {@link go()}. This triggers the build of the request and uses springs
 * RestTemplate to send it.
 * <p>
 * See JavaDoc of {@link AbstractRequest} for more details.
 *
 * @author boesch
 * @see AbstractRequest
 * @param <T>
 */
public class Request<T> extends AbstractRequest<T> {

    /**
     * This constructor is protected so it can be used by derived child classes.
     * See corresponding default constructor in parent class.
     */
    protected Request() {
        super();
    }

    /**
     * Uses default values but sets the requests method an uri.
     *
     * @param method
     * @param uri
     */
    public Request(HttpMethod method, String uri) {
        super(method, uri);
    }

    /**
     * Uses default values but sets the requests method an uri.
     *
     * @param method
     * @param uri
     */
    public Request(HttpMethod method, URI uri) {
        super(method, uri);
    }

    /**
     * Uses default values but sets the requests method.
     *
     * @param method
     */
    public Request(HttpMethod method) {
        super(method);
    }

    /**
     * Creates a new instance of {@link Request} with the specified uri. The
     * default method GET is being used if nothing else is set later on.
     *
     * @param uri
     */
    public Request(String uri) {
        super(uri);
    }

    /**
     * Creates a new instance of {@link Request} with the specified uri. The
     * default method GET is being used if nothing else is set later on.
     *
     * @param uri
     */
    public Request(URI uri) {
        super(uri);
    }

    //<editor-fold defaultstate="collapsed" desc="Static factory methods for easy access.">
    /**
     * Creates a new Request with method GET and the given uri. Same as calling
     * {@code new }{@link Request}{@code<>(HttpMethod.GET, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> get(String uri) {
        return new Request<>(HttpMethod.GET, uri);
    }

    /**
     * Creates a new Request with method GET and the given uri. Same as calling
     * {@code new }{@link Request}{@code<>(HttpMethod.GET, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> get(URI uri) {
        return new Request<>(HttpMethod.GET, uri);
    }

    /**
     * Creates a new Request with method POST and the given uri. Same as calling
     * {@code new }{@link Request}{@code<>(HttpMethod.POST, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> post(String uri) {
        return new Request<>(HttpMethod.POST, uri);
    }

    /**
     * Creates a new Request with method POST and the given uri. Same as calling
     * {@code new }{@link Request}{@code<>(HttpMethod.POST, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> post(URI uri) {
        return new Request<>(HttpMethod.POST, uri);
    }

    /**
     * Creates a new Request with method PUT and the given uri. Same as calling
     * {@code new }{@link Request}{@code<>(HttpMethod.PUT, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> put(String uri) {
        return new Request<>(HttpMethod.PUT, uri);
    }

    /**
     * Creates a new Request with method PUT and the given uri. Same as calling
     * {@code new }{@link Request}{@code<>(HttpMethod.PUT, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> put(URI uri) {
        return new Request<>(HttpMethod.PUT, uri);
    }

    /**
     * Creates a new Request with method DELETE and the given uri. Same as
     * calling {@code new }{@link Request}{@code<>(HttpMethod.DELETE, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> delete(String uri) {
        return new Request<>(HttpMethod.DELETE, uri);
    }

    /**
     * Creates a new Request with method DELETE and the given uri. Same as
     * calling {@code new }{@link Request}{@code<>(HttpMethod.DELETE, uri)}.
     *
     * @param <X>
     * @param uri
     * @return
     */
    public static <X> Request<X> delete(URI uri) {
        return new Request<>(HttpMethod.DELETE, uri);
    }

    /**
     * Creates a new Request with the given method and uri. Same as calling
     * {@code new }{@link Request}{@code<>(method, uri)}.
     *
     * @param <X>
     * @param method
     * @param uri
     * @return
     */
    public static <X> Request<X> custom(HttpMethod method, String uri) {
        return new Request<>(method, uri);
    }

    /**
     * Creates a new Request with the given method and uri. Same as calling
     * {@code new }{@link Request}{@code<>(method, uri)}.
     *
     * @param <X>
     * @param method
     * @param uri
     * @return
     */
    public static <X> Request<X> custom(HttpMethod method, URI uri) {
        return new Request<>(method, uri);
    }
    //</editor-fold>

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#expect(org.springframework.core.ParameterizedTypeReference) p}
     * and returns the result.Please read the parent methods javadoc for
     * details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public <R> Request<R> expect(ParameterizedTypeReference<R> responseTypeReference) {
        return (Request<R>) super.expect(responseTypeReference);
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#expect(java.lang.Class)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public <R> Request<R> expect(Class<R> responseTypeClass) {
        return (Request<R>) super.expect(responseTypeClass);
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uriVariables(java.util.Map)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> uriVariables(Map<String, ?> uriVariables) {
        super.uriVariables(uriVariables);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uriVariables(java.lang.Object...)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> uriVariables(Object... uriVariables) {
        super.uriVariables(uriVariables);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#query(org.springframework.util.MultiValueMap)} and
     * returns the result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> query(MultiValueMap<String, String> params) {
        super.query(params);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#query(java.lang.String, java.lang.Object...)} and
     * returns the result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> query(String key, Object... values) {
        super.query(key, values);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#query(java.lang.String, java.lang.Object)} and
     * returns the result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> query(String key, Object value) {
        super.query(key, value);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#query(java.lang.String)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> query(String query) {
        super.query(query);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#contentType(java.lang.String)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> contentType(String contentType) {
        super.contentType(contentType);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#header(java.lang.String, java.lang.String) } and
     * returns the result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> header(String key, String value) {
        super.header(key, value);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#headers(java.lang.String, java.util.List)} and
     * returns the result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> headers(String key, List<? extends String> values) {
        super.headers(key, values);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#headers(java.util.Map)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> headers(Map<String, String> headersToAdd) {
        super.headers(headersToAdd);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#body(java.lang.Object)} and returns the
     * result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> body(Object body) {
        super.body(body);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#entity(org.springframework.http.HttpEntity)} and
     * returns the result.Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> entity(HttpEntity entity) {
        super.entity(entity);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#method(org.springframework.http.HttpMethod)} and
     * returns the result. Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> method(HttpMethod method) {
        super.method(method);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uri(java.net.URI)} and returns the result. Please
     * read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> uri(URI uri) {
        super.uri(uri);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uri(java.lang.String)} and returns the result.
     * Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Request<T> uri(String uri) {
        super.uri(uri);
        return this;
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uriEncodeCharset(java.nio.charset.Charset)} and
     * returns the result. Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public AbstractRequest<T> uriEncodeCharset(Charset charset) {
        return super.uriEncodeCharset(charset);
    }

    /**
     * Delegates the call to the parent method {@link AbstractRequest#go()} and
     * returns the result. Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public ResponseEntity<T> go() {
        return super.go();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#getRestTemplate()} and returns the result. Please
     * read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public RestTemplate getRestTemplate() {
        return super.getRestTemplate();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#responseTypeReference()} and returns the result.
     * Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public ParameterizedTypeReference<T> responseTypeReference() {
        return super.responseTypeReference();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#responseTypeClass()} and returns the result.
     * Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Class<T> responseTypeClass() {
        return super.responseTypeClass();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uriVariablesMap()} and returns the result. Please
     * read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Map<String, ?> uriVariablesMap() {
        return super.uriVariablesMap();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uriVariables()} and returns the result. Please
     * read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Object[] uriVariables() {
        return super.uriVariables();
    }

    /**
     * Delegates the call to the parent method {@link AbstractRequest#headers()}
     * and returns the result. Please read the parent methods javadoc for
     * details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public HttpHeaders headers() {
        return super.headers();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#contentType()} and returns the result. Please read
     * the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public String contentType() {
        return super.contentType();
    }

    /**
     * Delegates the call to the parent method {@link AbstractRequest#body()}
     * and returns the result. Please read the parent methods javadoc for
     * details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public Object body() {
        return super.body();
    }

    /**
     * Delegates the call to the parent method {@link AbstractRequest#entity()}
     * and returns the result. Please read the parent methods javadoc for
     * details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public HttpEntity entity() {
        return super.entity();
    }

    /**
     * Delegates the call to the parent method {@link AbstractRequest#method()}
     * and returns the result. Please read the parent methods javadoc for
     * details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public HttpMethod method() {
        return super.method();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#uriBuilder()} and returns the result. Please read
     * the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public UriComponentsBuilder uriBuilder() {
        return super.uriBuilder();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#preventUriEncoding()} and returns the result.
     * Please read the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public AbstractRequest<T> preventUriEncoding() {
        return super.preventUriEncoding();
    }

    /**
     * Delegates the call to the parent method
     * {@link AbstractRequest#buildUri()} and returns the result. Please read
     * the parent methods javadoc for details.
     *
     * @return The result value of calling this method in
     * {@link AbstractRequest}.
     */
    @Override
    public URI buildUri() {
        return super.buildUri();
    }

}
