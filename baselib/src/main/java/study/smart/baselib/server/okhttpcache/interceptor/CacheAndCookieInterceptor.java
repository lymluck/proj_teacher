package study.smart.baselib.server.okhttpcache.interceptor;

import study.smart.baselib.server.okhttpcache.strategy.CacheNetworkStrategy;
import study.smart.baselib.server.okhttpcache.strategy.CacheStrategy;
import study.smart.baselib.server.okhttpcache.strategy.NetworkCacheStrategy;
import study.smart.baselib.server.okhttpcache.strategy.NetworkStrategy;
import study.smart.baselib.server.okhttpcache.strategy.RequestStrategy;
import study.smart.baselib.utils.ConstantUtils;
import study.smart.baselib.utils.ParameterUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * 缓存数据拦截器
 * Created by louis on 17/3/22.
 */
public class CacheAndCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        RequestStrategy requestStrategy = new RequestStrategy();
        String cacheTypeHeader = chain.request().headers().get(ConstantUtils.REQUEST_CACHE_TYPE_HEAD);
        if (cacheTypeHeader != null) {
            int cacheType = Integer.parseInt(cacheTypeHeader);
            switch (cacheType) {
                case ParameterUtils.ONLY_CACHED:
                    requestStrategy.setBaseRequestStrategy(new CacheStrategy());
                    break;
                case ParameterUtils.ONLY_NETWORK:
                    requestStrategy.setBaseRequestStrategy(new NetworkStrategy());
                    break;
                case ParameterUtils.CACHED_ELSE_NETWORK:
                    requestStrategy.setBaseRequestStrategy(new CacheNetworkStrategy());
                    break;
                case ParameterUtils.NETWORK_ELSE_CACHED:
                    requestStrategy.setBaseRequestStrategy(new NetworkCacheStrategy());
                    break;
                default:
                    break;
            }
        }
        return requestStrategy.request(chain);
    }
}
