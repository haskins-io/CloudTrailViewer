package io.haskins.java.cloudtrailviewer.model.elblog;

import io.haskins.java.cloudtrailviewer.model.AwsData;

import java.util.regex.Matcher;

public class ElbLog extends AwsData {

    private String timestamp;
    private String elb;
    private String clientAddress;
    private String clientPort;
    private String backendAddress;
    private String backendPort;
    private String requestProcessingTime;
    private String backendProcessingTime;
    private String responseProcessingTime;
    private String elbStatusCode;
    private String backendStatusCode;
    private String receivedBytes;
    private String sentByes;
    private String request;
    private String url;
    private String userAgent;
    private String sslCipher;
    private String sslProtocol;

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getElb() {
        return elb;
    }
    public void setElb(String elb) {
        this.elb = elb;
    }

    public String getClientAddress() {
        return clientAddress;
    }
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getBackendAddress() {
        return backendAddress;
    }
    public void setBackendAddress(String backendAddress) {
        this.backendAddress = backendAddress;
    }

    public String getBackendProcessingTime() {
        return backendProcessingTime;
    }
    public void setBackendProcessingTime(String backendProcessingTime) {
        this.backendProcessingTime = backendProcessingTime;
    }

    public String getResponseProcessingTime() {
        return responseProcessingTime;
    }
    public void setResponseProcessingTime(String responseProcessingTime) {
        this.responseProcessingTime = responseProcessingTime;
    }

    public String getElbStatusCode() {
        return elbStatusCode;
    }
    public void setElbStatusCode(String elbStatusCode) {
        this.elbStatusCode = elbStatusCode;
    }

    public String getBackendStatusCode() {
        return backendStatusCode;
    }
    public void setBackendStatusCode(String backendStatusCode) {
        this.backendStatusCode = backendStatusCode;
    }

    public String getReceivedBytes() {
        return receivedBytes;
    }
    public void setReceivedBytes(String receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public String getSentByes() {
        return sentByes;
    }
    public void setSentByes(String sentByes) {
        this.sentByes = sentByes;
    }

    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }

    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSslCipher() {
        return sslCipher;
    }
    public void setSslCipher(String sslCipher) {
        this.sslCipher = sslCipher;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }
    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public void populateFromRegex(Matcher matcher) {

        this.setTimestamp(matcher.group(1));
        this.setElb(matcher.group(2));
        this.setClientAddress(matcher.group(3));
        this.setClientPort(matcher.group(4));
        this.setBackendAddress(matcher.group(5));
        this.setBackendPort(matcher.group(6));
        this.setRequestProcessingTime(matcher.group(7));
        this.setBackendProcessingTime(matcher.group(8));
        this.setResponseProcessingTime(matcher.group(9));
        this.setElbStatusCode(matcher.group(10));
        this.setBackendStatusCode(matcher.group(11));
        this.setReceivedBytes(matcher.group(12));
        this.setSentByes(matcher.group(13));
        this.setRequest(matcher.group(14));
        this.setUrl(matcher.group(15));
        this.setUserAgent(matcher.group(17));
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getBackendPort() {
        return backendPort;
    }

    public void setBackendPort(String backendPort) {
        this.backendPort = backendPort;
    }

    public String getRequestProcessingTime() {
        return requestProcessingTime;
    }

    public void setRequestProcessingTime(String requestProcessingTime) {
        this.requestProcessingTime = requestProcessingTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
