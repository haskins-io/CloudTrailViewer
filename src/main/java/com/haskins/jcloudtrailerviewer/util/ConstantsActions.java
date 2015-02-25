/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.util;

/**
 *
 * @author mark.haskins
 */
public class ConstantsActions {

    public static final String[] ACTIONS_IAM = {
        "AddClientIDToOpenIDConnectProvider",
        "AddRoleToInstanceProfile",
        "AddUserToGroup",
        "AttachGroupPolicy",
        "AttachRolePolicy",
        "AttachUserPolicy",
        "ChangePassword",
        "CreateAccessKey",
        "CreateAccountAlias",
        "CreateGroup",
        "CreateInstanceProfile",
        "CreateLoginProfile",
        "CreateOpenIDConnectProvider",
        "CreatePolicy",
        "CreatePolicyVersion",
        "CreateRole",
        "CreateSAMLProvider",
        "CreateUser",
        "CreateVirtualMFADevice",
        "DeactivateMFADevice",
        "DeleteAccessKey",
        "DeleteAccountAlias",
        "DeleteAccountPasswordPolicy",
        "DeleteGroup",
        "DeleteGroupPolicy",
        "DeleteInstanceProfile",
        "DeleteLoginProfile",
        "DeleteOpenIDConnectProvider",
        "DeletePolicy",
        "DeletePolicyVersion",
        "DeleteRole",
        "DeleteRolePolicy",
        "DeleteSAMLProvider",
        "DeleteServerCertificate",
        "DeleteSigningCertificate",
        "DeleteUser",
        "DeleteUserPolicy",
        "DeleteVirtualMFADevice",
        "DetachGroupPolicy",
        "DetachRolePolicy",
        "DetachUserPolicy",
        "EnableMFADevice",
        "GenerateCredentialReport",
        "PutGroupPolicy",
        "PutRolePolicy",
        "PutUserPolicy",
        "RemoveClientIDFromOpenIDConnectProvider",
        "RemoveRoleFromInstanceProfile",
        "RemoveUserFromGroup",
        "ResyncMFADevice",
        "SetDefaultPolicyVersion",
        "UpdateAccessKey",
        "UpdateAccountPasswordPolicy",
        "UpdateAssumeRolePolicy",
        "UpdateGroup",
        "UpdateLoginProfile",
        "UpdateOpenIDConnectProviderThumbprint",
        "UpdateSAMLProvider",
        "UpdateServerCertificate",
        "UpdateSigningCertificate",
        "UpdateUser",
        "UploadServerCertificate",
        "UploadSigningCertificate"
    };

    public static final String[] ACTIONS_NETWORK = {
        "AcceptVpcPeeringConnection",
        "AllocateAddress",
        "AssociateAddress",
        "AssociateDhcpOptions",
        "AssociateRouteTable",
        "AttachClassicLinkVpc",
        "AttachInternetGateway",
        "AttachNetworkInterface",
        "AttachVpnGateway",
        "AuthorizeSecurityGroupEgress",
        "AuthorizeSecurityGroupIngress",
        "CreateCustomerGateway",
        "CreateDhcpOptions",
        "CreateInternetGateway",
        "CreateNetworkAcl",
        "CreateNetworkAclEntry",
        "CreateNetworkInterface",
        "CreateRoute",
        "CreateRouteTable",
        "CreateSecurityGroup",
        "CreateSubnet",
        "CreateVpc",
        "CreateVpcPeeringConnection",
        "CreateVpnConnection",
        "CreateVpnConnectionRoute",
        "CreateVpnGateway",
        "DeleteCustomerGateway",
        "DeleteDhcpOptions",
        "DeleteInternetGateway",
        "DeleteNetworkAcl",
        "DeleteNetworkAclEntry",
        "DeleteNetworkInterface",
        "DeleteRoute",
        "DeleteRouteTable",
        "DeleteSecurityGroup",
        "DeleteSubnet",
        "DeleteVpc",
        "DeleteVpcPeeringConnection",
        "DeleteVpnConnection",
        "DeleteVpnConnectionRoute",
        "DeleteVpnGateway",
        "DetachClassicLinkVpc",
        "DetachInternetGateway",
        "DetachNetworkInterface",
        "DetachVpnGateway",
        "DisableVgwRoutePropagation",
        "DisableVpcClassicLink",
        "DisassociateAddress",
        "DisassociateRouteTable",
        "EnableVgwRoutePropagation",
        "EnableVolumeIO",
        "EnableVpcClassicLink",
        "GetConsoleOutput",
        "GetPasswordData",
        "ModifyNetworkInterfaceAttribute",
        "ModifySubnetAttribute",
        "ModifyVpcAttribute",
        "RejectVpcPeeringConnection",
        "ReleaseAddress",
        "ReplaceNetworkAclAssociation",
        "ReplaceNetworkAclEntry",
        "ReplaceRoute",
        "ReplaceRouteTableAssociation",
        "ResetNetworkInterfaceAttribute",
        "RevokeSecurityGroupEgress",
        "RevokeSecurityGroupIngress"
    };
}
