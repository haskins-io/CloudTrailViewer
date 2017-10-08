package io.haskins.java.cloudtrailviewer.model.vpclog;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import javafx.beans.property.SimpleStringProperty;

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

    public void populateFromRegex(Matcher matcher) {

        setVersion(matcher.group(2));
        setAccountId(matcher.group(3));
        setInterfaceId(matcher.group(4));
        setSrcaddr(matcher.group(5));
        setDstaddr(matcher.group(6));
        setSrcport(matcher.group(7));
        setDstport(matcher.group(8));
        setProtocol(matcher.group(9));
        setPackets(matcher.group(10));
        setBytes(matcher.group(11));
        setStart(matcher.group(12));
        setEnd(matcher.group(13));
        setAction(matcher.group(14));
        setLogStatus(matcher.group(15));
    }

}
