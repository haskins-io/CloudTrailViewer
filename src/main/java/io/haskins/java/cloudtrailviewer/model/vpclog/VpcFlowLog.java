package io.haskins.java.cloudtrailviewer.model.vpclog;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import javafx.beans.property.SimpleStringProperty;
import org.apache.lucene.document.Document;

import java.util.regex.Matcher;

public class VpcFlowLog extends AwsData {

    // FlowLog data
    private SimpleStringProperty version = new SimpleStringProperty("");
    private SimpleStringProperty accountId = new SimpleStringProperty("");
    private SimpleStringProperty interfaceId = new SimpleStringProperty("");
    private SimpleStringProperty srcaddr = new SimpleStringProperty("");
    private SimpleStringProperty dstaddr = new SimpleStringProperty("");
    private SimpleStringProperty srcport = new SimpleStringProperty("");
    private SimpleStringProperty dstport = new SimpleStringProperty("");
    private SimpleStringProperty protocol = new SimpleStringProperty("");
    private SimpleStringProperty packets = new SimpleStringProperty("");
    private SimpleStringProperty bytes = new SimpleStringProperty("");
    private SimpleStringProperty start =new SimpleStringProperty("");
    private SimpleStringProperty end = new SimpleStringProperty("");
    private SimpleStringProperty action = new SimpleStringProperty("");
    private SimpleStringProperty logStatus = new SimpleStringProperty("");

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public String getAccountId() {
        return accountId.get();
    }

    public void setAccountId(String accountId) {
        this.accountId.set(accountId);
    }

    public String getInterfaceId() {
        return interfaceId.get();
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId.set(interfaceId);
    }

    public String getSrcaddr() {
        return srcaddr.get();
    }

    public void setSrcaddr(String srcaddr) {
        this.srcaddr.set(srcaddr);
    }

    public String getDstaddr() {
        return dstaddr.get();
    }

    public void setDstaddr(String dstaddr) {
        this.dstaddr.set(dstaddr);
    }

    public String getSrcport() {
        return srcport.get();
    }

    public void setSrcport(String srcport) {
        this.srcport.set(srcport);
    }

    public String getDstport() {
        return dstport.get();
    }

    public void setDstport(String dstport) {
        this.dstport.set(dstport);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public String getPackets() {
        return packets.get();
    }

    public void setPackets(String packets) {
        this.packets.set(packets);
    }

    public String getBytes() {
        return bytes.get();
    }

    public void setBytes(String bytes) {
        this.bytes.set(bytes);
    }

    public String getStart() {
        return start.get();
    }

    public void setStart(String start) {
        this.start.set(start);
    }

    public String getEnd() {
        return end.get();
    }

    public void setEnd(String end) {
        this.end.set(end);
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public String getLogStatus() {
        return logStatus.get();
    }

    public void setLogStatus(String logStatus) {
        this.logStatus.set(logStatus);
    }

    public VpcFlowLog withDocument(Document d) {

        setVersion(d.getField("version").stringValue());
        setAccountId(d.getField("accountId").stringValue());
        setInterfaceId(d.getField("interfaceId").stringValue());
        setSrcaddr(d.getField("srcaddr").stringValue());
        setDstaddr(d.getField("dstaddr").stringValue());
        setSrcport(d.getField("srcport").stringValue());
        setDstport(d.getField("dstport").stringValue());
        setProtocol(d.getField("protocol").stringValue());
        setPackets(d.getField("packets").stringValue());
        setBytes(d.getField("bytes").stringValue());
        setStart(d.getField("start").stringValue());
        setEnd(d.getField("end").stringValue());
        setAction(d.getField("action").stringValue());
        setLogStatus(d.getField("logStatus").stringValue());

        return this;

    }

}
