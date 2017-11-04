package io.haskins.java.cloudtrailviewer.model.elblog;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import org.apache.lucene.document.Document;

public class ElbLog extends AwsData {

    public static final String TYPE = "elblogs";

    private String eventTime;
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

    public String getEventTime() {
        return eventTime;
    }
    public void setEventTime(String timestamp) {
        this.eventTime = timestamp;
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

    public ElbLog withDocument(Document d) {

        this.setEventTime(d.getField("eventTime").stringValue());
        this.setElb(d.getField("elb").stringValue());
        this.setClientAddress(d.getField("clientAddress").stringValue());
        this.setClientPort(d.getField("clientPort").stringValue());
        this.setBackendAddress(d.getField("backendAddress").stringValue());
        this.setBackendPort(d.getField("backendPort").stringValue());
        this.setRequestProcessingTime(d.getField("requestProcessingTime").stringValue());
        this.setBackendProcessingTime(d.getField("backendProcessingTime").stringValue());
        this.setResponseProcessingTime(d.getField("responseProcessingTime").stringValue());
        this.setElbStatusCode(d.getField("elbStatusCode").stringValue());
        this.setBackendStatusCode(d.getField("backendStatusCode").stringValue());
        this.setReceivedBytes(d.getField("receivedBytes").stringValue());
        this.setSentByes(d.getField("sentByes").stringValue());
        this.setRequest(d.getField("request").stringValue());
        this.setUrl(d.getField("url").stringValue());
        this.setUserAgent(d.getField("userAgent").stringValue());

        return this;
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
