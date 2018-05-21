package com.smartstudy.counselor_t.ossServer;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.smartstudy.counselor_t.entity.TokenBean;

/**
 * @author yqy
 * @date on 2018/5/21
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class STSGetter extends OSSFederationCredentialProvider {
    private OSSFederationToken ossFederationToken;
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String expiration;
    private String bucket;
    private String endpoint;
    private String key;


    public STSGetter(TokenBean bean) {
        this.accessKeyId = bean.getAccessKeyId();
        this.accessKeySecret = bean.getAccessKeySecret();
        this.securityToken = bean.getSecurityToken();
        this.expiration = bean.getExpiration();
    }


    public OSSFederationToken getOssFederationToken() {
        return ossFederationToken;
    }

    public void setOssFederationToken(OSSFederationToken ossFederationToken) {
        this.ossFederationToken = ossFederationToken;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public OSSFederationToken getFederationToken() {
        return new OSSFederationToken(accessKeyId, accessKeySecret, securityToken, expiration);
    }
}

