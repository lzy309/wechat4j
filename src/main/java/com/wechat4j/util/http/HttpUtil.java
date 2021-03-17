package com.wechat4j.util.http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具类
 *
 * @author lizhaoyang
 */
public final class HttpUtil {

    private static final Integer SUCCESS_STATUS_CODE = 200;

    private static final String HTTP_CONTENT_TYPE = "Content-Type";

    private final CloseableHttpClient httpClient;

    private final HttpMethodEnum method;

    private final String url;

    private final Map<String, Serializable> params;

    private final Map<String, String> headers;

    private HttpUtil(CloseableHttpClient httpClient, HttpMethodEnum method,
                     String url, Map<String, Serializable> params, Map<String, String> headers) {
        this.httpClient = httpClient;
        this.method = method;
        this.url = url;
        this.params = params;
        this.headers = headers;
    }

    /**
     * 发送网络请求
     *
     * @param httpClient HTTP请求客户端
     * @param method     请求方式 GET|POST
     * @param url        请求地址
     * @param params     请求参数
     * @param headers    请求头
     * @return
     * @throws HttpException
     */
    public static HttpResult request(CloseableHttpClient httpClient, HttpMethodEnum method, String url,
                                     Map<String, Serializable> params, Map<String, String> headers) throws HttpException {
        HttpUtil httpUtil = new HttpUtil(httpClient, method, url, params, headers);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpUtil.doRequest();
            if (ObjectUtil.isEmpty(httpResponse)) {
                throw new HttpException("request failure httpResponse is empty");
            }
            return httpUtil.resolveHttpResult(httpResponse);
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        } finally {
            httpUtil.closeHttpResponse(httpResponse);
        }
    }

    /**
     * 发送GET请求
     *
     * @param httpClient HTTP请求客户端
     * @param url        请求地址
     * @param params     请求参数
     * @param headers    请求头
     * @return
     * @throws HttpException
     */
    public static HttpResult get(CloseableHttpClient httpClient, String url, Map<String, Serializable> params,
                                 Map<String, String> headers) throws HttpException {
        return request(httpClient, HttpMethodEnum.GET, url, params, headers);
    }

    /**
     * 发送POST请求
     *
     * @param httpClient  HTTP请求客户端
     * @param url         请求地址
     * @param params      请求参数
     * @param headers     请求头
     * @return
     * @throws HttpException
     */
    public static HttpResult post(CloseableHttpClient httpClient, String url, Map<String, Serializable> params,
                                  Map<String, String> headers) throws HttpException {
        return request(httpClient, HttpMethodEnum.POST, url, params, headers);
    }

    /**
     * 获取HTTP请求客户端
     *
     * @param timeout 超时时间
     * @return
     */
    public static CloseableHttpClient getHttpClient(int timeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout).build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 关闭HTTP客户端
     *
     * @param httpClient
     * @throws HttpException
     */
    public static void closeHttpClient(CloseableHttpClient httpClient) throws HttpException {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new HttpException(e.getMessage());
            }
        }
    }

    private CloseableHttpResponse doRequest() throws HttpException, IOException, URISyntaxException {
        int methodsType = method.getType();

        if (methodsType == HttpMethodEnum.GET.getType()) {
            return this.doGet();
        } else if (methodsType == HttpMethodEnum.POST.getType()) {
            return this.doPost();
        } else {
            throw new MethodNotSupportedException("unsupported method: " + method.getMessage() + ", method must be GET or POST");
        }
    }

    private CloseableHttpResponse doGet() throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);

        if (!ObjectUtil.isEmpty(params)) {
            params.forEach((paramKey, paramValue) -> {
                uriBuilder.setParameter(paramKey, ObjectUtil.isEmpty(paramValue) ? StrUtil.EMPTY : paramValue.toString());
            });
        }

        HttpGet httpGet = new HttpGet(uriBuilder.build());

        if (!ObjectUtil.isEmpty(headers)) {
            headers.forEach(httpGet::addHeader);
        }

        return httpClient.execute(httpGet);
    }

    private CloseableHttpResponse doPost() throws IOException, HttpException {
        String[] contentType = {StrUtil.EMPTY};

        HttpPost httpPost = new HttpPost(url);
        if (!ObjectUtil.isEmpty(headers)) {
            headers.forEach((headerKey, headerValue) -> {
                if (headerKey.equalsIgnoreCase(HTTP_CONTENT_TYPE)) {
                    contentType[0] = headerValue;
                }
                httpPost.addHeader(headerKey, headerValue);
            });
        }

        if (StrUtil.EMPTY.equals(contentType[0])) {
            throw new HttpException("Content-Type header can not to be empty");
        }

        HttpEntity httpEntity;
        if (contentType[0].equals(HttpContentTypeEnum.FORM_URL_ENCODED.getContent())) {
            httpEntity = buildUrlEncodedFormEntity(params);
        } else if (contentType[0].equals(HttpContentTypeEnum.JSON.getContent())) {
            httpEntity = buildJsonEntity(params);
        } else {
            throw new HttpException("unsupported Content-Type header: "
                    + contentType[0] + ", Content-Type must be application/x-www-form-urlencoded or application/json");
        }

        if (!ObjectUtil.isEmpty(httpEntity)) {
            httpPost.setEntity(httpEntity);
        }

        return httpClient.execute(httpPost);
    }

    /**
     * 构造JSON请求体
     *
     * @param params 请求参数
     * @return 请求体
     */
    private HttpEntity buildJsonEntity(Map<String, Serializable> params) {
        String jsonStr = JSON.toJSONString(params);
        return new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
    }

    /**
     * 构造x-www-form-urlencoded请求体
     *
     * @param params 请求参数
     * @return 请求体
     */
    private HttpEntity buildUrlEncodedFormEntity(Map<String, Serializable> params) {
        List<NameValuePair> parameters = Lists.newArrayListWithCapacity(params.keySet().size());
        params.forEach((paramKey, paramValue) -> {
            BasicNameValuePair pair = new BasicNameValuePair(paramKey, ObjectUtil.isEmpty(paramValue) ? StrUtil.EMPTY : paramValue.toString());
            parameters.add(pair);
        });
        return new UrlEncodedFormEntity(parameters, Consts.UTF_8);
    }

    /**
     * 解析请求响应结果
     *
     * @param httpResponse
     * @return
     * @throws HttpException
     */
    private HttpResult resolveHttpResult(HttpResponse httpResponse) throws HttpException {
        HttpResult result = new HttpResult();

        HttpEntity httpEntity = httpResponse.getEntity();
        if (!ObjectUtil.isEmpty(httpEntity)) {
            try {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                String responseStr = EntityUtils.toString(httpEntity, Consts.UTF_8);
                result.setResponseContent(responseStr);
                result.setStatusCode(statusCode);
                result.setStatus(statusCode == SUCCESS_STATUS_CODE);
            } catch (IOException e) {
                throw new HttpException(e.getMessage());
            } finally {
                closeHttpEntity(httpEntity);
            }
        }

        return result;
    }

    private void closeHttpResponse(CloseableHttpResponse httpResponse) throws HttpException {
        if (httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                throw new HttpException(e.getMessage());
            }
        }
    }

    private void closeHttpEntity(HttpEntity httpEntity) throws HttpException {
        if (httpEntity != null) {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                throw new HttpException(e.getMessage());
            }
        }
    }
}
