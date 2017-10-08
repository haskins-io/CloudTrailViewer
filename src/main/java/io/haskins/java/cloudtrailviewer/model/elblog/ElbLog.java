package io.haskins.java.cloudtrailviewer.model.elblog;

import io.haskins.java.cloudtrailviewer.model.AwsData;

public class ElbLog extends AwsData {

    private String timestamp;
    private String elb;
    private String clientAddressPort;
    private String backendAddressPort;
    private String processingTime;
    private String responseTime;
    private String elbStatusCode;
    private String backendStatusCode;
    private String receivedBytes;
    private String sentByes;
    private String request;
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

    public String getClientAddressPort() {
        return clientAddressPort;
    }

    public void setClientAddressPort(String clientAddressPort) {
        this.clientAddressPort = clientAddressPort;
    }

    public String getBackendAddressPort() {
        return backendAddressPort;
    }

    public void setBackendAddressPort(String backendAddressPort) {
        this.backendAddressPort = backendAddressPort;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
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
}
