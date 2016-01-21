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

package com.haskins.cloudtrailviewer.model.event;

/**
 *
 * @author mark.haskins
 */
public class AdditionalEventData {

    private String SamlProviderArn;
    private String MobileVersion;
    private String LoginTo;
    private String MFAUsed;
    private String VpcEndpointId;
    private String RedirectTo;
    private String SwitchTo;

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

    /**
     * @return the vpcEndpointId
     */
    public String getVpcEndpointId() {
        return VpcEndpointId;
    }

    /**
     * @param vpcEndpointId the vpcEndpointId to set
     */
    public void setVpcEndpointId(String vpcEndpointId) {
        this.VpcEndpointId = vpcEndpointId;
    }

    /**
     * @return the RedirectTo
     */
    public String getRedirectTo() {
        return RedirectTo;
    }

    /**
     * @param RedirectTo the RedirectTo to set
     */
    public void setRedirectTo(String RedirectTo) {
        this.RedirectTo = RedirectTo;
    }

    /**
     * @return the SwitchTo
     */
    public String getSwitchTo() {
        return SwitchTo;
    }

    /**
     * @param SwitchTo the SwitchTo to set
     */
    public void setSwitchTo(String SwitchTo) {
        this.SwitchTo = SwitchTo;
    } 
}
