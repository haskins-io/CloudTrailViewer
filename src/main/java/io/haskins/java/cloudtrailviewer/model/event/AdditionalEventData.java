/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.model.event;

/**
 *
 * @author mark.haskins
 */
class AdditionalEventData {

    private String samlProviderArn;
    private String mobileVersion;
    private String loginTo;
    private String mFAUsed;
    private String vpcEndpointId;
    private String redirectTo;
    private String switchTo;

    /**
     * @return the SamlProviderArn
     */
    public String getSamlProviderArn() {
        return samlProviderArn;
    }

    /**
     * @param SamlProviderArn the SamlProviderArn to set
     */
    public void setSamlProviderArn(String SamlProviderArn) {
        this.samlProviderArn = SamlProviderArn;
    }

    /**
     * @return the MobileVersion
     */
    public String getMobileVersion() {
        return mobileVersion;
    }

    /**
     * @param MobileVersion the MobileVersion to set
     */
    public void setMobileVersion(String MobileVersion) {
        this.mobileVersion = MobileVersion;
    }

    /**
     * @return the LoginTo
     */
    public String getLoginTo() {
        return loginTo;
    }

    /**
     * @param LoginTo the LoginTo to set
     */
    public void setLoginTo(String LoginTo) {
        this.loginTo = LoginTo;
    }

    /**
     * @return the MFAUsed
     */
    public String getMFAUsed() {
        return mFAUsed;
    }

    /**
     * @param MFAUsed the MFAUsed to set
     */
    public void setMFAUsed(String MFAUsed) {
        this.mFAUsed = MFAUsed;
    }

    /**
     * @return the vpcEndpointId
     */
    public String getVpcEndpointId() {
        return vpcEndpointId;
    }

    /**
     * @param vpcEndpointId the vpcEndpointId to set
     */
    public void setVpcEndpointId(String vpcEndpointId) {
        this.vpcEndpointId = vpcEndpointId;
    }

    /**
     * @return the RedirectTo
     */
    public String getRedirectTo() {
        return redirectTo;
    }

    /**
     * @param RedirectTo the RedirectTo to set
     */
    public void setRedirectTo(String RedirectTo) {
        this.redirectTo = RedirectTo;
    }

    /**
     * @return the SwitchTo
     */
    public String getSwitchTo() {
        return switchTo;
    }

    /**
     * @param SwitchTo the SwitchTo to set
     */
    public void setSwitchTo(String SwitchTo) {
        this.switchTo = SwitchTo;
    } 
}
