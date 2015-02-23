package com.haskins.jcloudtrailerviewer.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author mark.haskins
 */
public class AdditionalEventData {

    @JsonProperty("SamlProviderArn")
    private String SamlProviderArn;
    
    @JsonProperty("MobileVersion")
    private String MobileVersion;
        
    @JsonProperty("LoginTo")
    private String LoginTo;
            
    @JsonProperty("MFAUsed")
    private String MFAUsed;

    /**
     * @return the SamlProviderArn
     */
    public String getSamlProviderArn() {
        return SamlProviderArn;
    }

    /**
     * @param SamlProviderArn the SamlProviderArn to set
     */
    public void setSamlProviderArn(String SamlProviderArn) {
        this.SamlProviderArn = SamlProviderArn;
    }

    /**
     * @return the MobileVersion
     */
    public String getMobileVersion() {
        return MobileVersion;
    }

    /**
     * @param MobileVersion the MobileVersion to set
     */
    public void setMobileVersion(String MobileVersion) {
        this.MobileVersion = MobileVersion;
    }

    /**
     * @return the LoginTo
     */
    public String getLoginTo() {
        return LoginTo;
    }

    /**
     * @param LoginTo the LoginTo to set
     */
    public void setLoginTo(String LoginTo) {
        this.LoginTo = LoginTo;
    }

    /**
     * @return the MFAUsed
     */
    public String getMFAUsed() {
        return MFAUsed;
    }

    /**
     * @param MFAUsed the MFAUsed to set
     */
    public void setMFAUsed(String MFAUsed) {
        this.MFAUsed = MFAUsed;
    }
}
