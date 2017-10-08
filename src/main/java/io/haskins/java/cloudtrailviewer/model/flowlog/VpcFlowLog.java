package io.haskins.java.cloudtrailviewer.model.flowlog;

import io.haskins.java.cloudtrailviewer.model.AwsData;

import java.util.regex.Matcher;

public class VpcFlowLog extends AwsData {

    // FlowLog data
    private String version = "";
    private long accountId = 0;
    private String interfaceId = "";
    private String srcaddr = "";
    private String dstaddr = "";
    private int srcport = 0;
    private int dstport = 0;
    private int protocol = 0;
    private int packets = 0;
    private int bytes = 0;
    private long start =0;
    private long end = 0;
    private String action = "";
    private String logStatus = "";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getSrcaddr() {
        return srcaddr;
    }

    public void setSrcaddr(String srcaddr) {
        this.srcaddr = srcaddr;
    }

    public String getDstaddr() {
        return dstaddr;
    }

    public void setDstaddr(String dstaddr) {
        this.dstaddr = dstaddr;
    }

    public int getSrcport() {
        return srcport;
    }

    public void setSrcport(int srcport) {
        this.srcport = srcport;
    }

    public int getDstport() {
        return dstport;
    }

    public void setDstport(int dstport) {
        this.dstport = dstport;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public int getPackets() {
        return packets;
    }

    public void setPackets(int packets) {
        this.packets = packets;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public void populateFromRegex(Matcher matcher) {

        setVersion(matcher.group(1));
        setAccountId(Long.parseLong(matcher.group(2)));
        setInterfaceId(matcher.group(3));
        setSrcaddr(matcher.group(4));
        setDstaddr(matcher.group(5));
        setSrcport(Integer.parseInt(matcher.group(6)));
        setDstport(Integer.parseInt(matcher.group(7)));
        setProtocol(Integer.parseInt(matcher.group(8)));
        setPackets(Integer.parseInt(matcher.group(9)));
        setBytes(Integer.parseInt(matcher.group(10)));
        setStart(Long.parseLong(matcher.group(11)));
        setEnd(Long.parseLong(matcher.group(12)));
        setAction(matcher.group(13));
        setLogStatus(matcher.group(14));
    }

}
