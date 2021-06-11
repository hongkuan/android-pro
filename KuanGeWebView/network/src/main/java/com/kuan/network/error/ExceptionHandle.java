package com.kuan.network.error;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * Created by hongkuan on 2021-06-08 0008.
 */
public class ExceptionHandle {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

    public static class ResponseException extends Exception {
        public int code;
        public String message;

        public ResponseException(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static class ServerException extends RuntimeException {
        public int code;
        public String message;
    }

    public static ResponseException handleException(Throwable throwable) {
        ResponseException result = null;
        if (throwable instanceof HttpException) {
            HttpException httpEX = (HttpException) throwable;
            result = new ResponseException(httpEX, ERROR.HTTP_ERROR);
            switch (httpEX.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    result.message = "网络错误";
                    break;
            }
        } else if (throwable instanceof ServerException) {
            ServerException serverError = (ServerException) throwable;
            result = new ResponseException(serverError, serverError.code);
            result.message = serverError.message;
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            result = new ResponseException(throwable, ERROR.PARSE_ERROR);
            result.message = "解析错误";
        } else if (throwable instanceof ConnectException ||
                    throwable instanceof UnknownHostException) {
            result = new ResponseException(throwable, ERROR.NETWORK_ERROR);
            result.message = "连接失败";
        } else if (throwable instanceof SSLHandshakeException) {
            result = new ResponseException(throwable, ERROR.SSL_ERROR);
            result.message = "证书验证失败";
        } else if (throwable instanceof ConnectTimeoutException
                || throwable instanceof SocketTimeoutException) {
            result = new ResponseException(throwable, ERROR.TIMEOUT_ERROR);
            result.message = "连接超时";
        } else {
            result = new ResponseException(throwable, ERROR.UNKNOWN);
            result.message = "未知异常";
        }

        return result;
    }
}
