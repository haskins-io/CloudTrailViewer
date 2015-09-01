/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.table.resource;

import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public class IamResource extends AbstractRequest implements Request {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("UploadServerCertificate")) {
            uploadServerCertificate(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateServerCertificate")) {
            updateServceCertificate(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("UpdateAssumeRolePolicy")) {
            updateAssumeRole(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("UpdateAccessKey")) {
            updateAccessKey(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("RemoveUserFromGroup")) {
            removeUserFromGroup(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("RemoveRoleFromInstanceProfile")) {
            removeRoleFromInstaceProfile(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("PutUserPolicy")) {
            putUserPolicy(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("PutRolePolicy")) {
            putRolePolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListUserPolicies")) {
            listUserPolicies(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListSSHPublicKeys")) {
            listPublicKeys(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListRolePolicies")) {
            listRolePolicies(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListInstanceProfilesForRole")) {
            listInstanceProfileForRole(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListGroupsForUser")) {
            listGroupForUser(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListGroupPolicies")) {
            listGroupPolicies(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListAttachedUserPolicies")) {
            listAttachedUserPolicies(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListAttachedRolePolicies")) {
            listAttachedRolePolicies(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListAttachedGroupPolicies")) {
            listAttachedGroupPolicies(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListAccessKeys")) {
            listAccessKeys(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetUserPolicy")) {
            getUserPolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetUser")) {
            getUser(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetRolePolicy")) {
            getRolePolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetRole")) {
            getRole(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetInstanceProfile")) {
            getInstanceProfile(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetGroupPolicy")) {
            getGroupPolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetGroup")) {
            getGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("AddRoleToInstanceProfile")) {
            addRoleToInstanceProfile(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteRolePolicy")) {
            deleteRolePolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteRole")) {
            deleteRole(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteInstanceProfile")) {
            deleteInstanceProfile(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteAccessKey")) {
            deleteAccessKey(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateRole")) {
            createRole(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateInstanceProfile")) {
            createInstanceProfile(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateInstanceExportTask")) {
            createInstanceExportTask(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateAccessKey")) {
            createAccessKey(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("AssumeRole")) {
            assumeRole(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("AddUserToGroup")) {
            addUserToGroup(event, resources);
            
        }
    }
    
    private void addUserToGroup(Event event, RequestInfo resources) {
        getTopLevelResource("Group", "groupName", event, resources); 
        getTopLevelParameter("User", "userName", event, resources); 
    }
    
    private void assumeRole(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleArn", event, resources); 
        getTopLevelResource("Session", "roleSessionName", event, resources); 
    }
    
    private void createAccessKey(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources); 
    }
    
    private void createInstanceExportTask(Event event, RequestInfo resources) {
        getTopLevelResource("Instance Id", "instanceId", event, resources);
    }
    
    private void createInstanceProfile(Event event, RequestInfo resources) {
        getTopLevelResource("Instance Profile", "instanceProfileName", event, resources);
    }
    
    private void createRole(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
    }
    
    private void deleteAccessKey(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources); 
    }
    
    private void deleteInstanceProfile(Event event, RequestInfo resources) {
        getTopLevelResource("Instance Profile", "instanceProfileName", event, resources);
    }
    
    private void deleteRole(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
    }
    
    private void deleteRolePolicy(Event event, RequestInfo resources) {
        getTopLevelParameter("Policy", "policyName", event, resources); 
        getTopLevelResource("Role", "roleName", event, resources); 
    }
    
    private void addRoleToInstanceProfile(Event event, RequestInfo resources) {
        getTopLevelResource("Instance Profile", "instanceProfileName", event, resources);
        getTopLevelParameter("Role", "roleName", event, resources);
    }
    
    private void getGroup(Event event, RequestInfo resources) {
        getTopLevelResource("Group", "groupName", event, resources);
    }
    
    private void getGroupPolicy(Event event, RequestInfo resources) {
        getTopLevelParameter("Policy", "policyName", event, resources); 
        getTopLevelResource("Group", "groupName", event, resources); 
    }
    
    private void getInstanceProfile(Event event, RequestInfo resources) {
        getTopLevelResource("Instance Profile", "instanceProfileName", event, resources);
    }
    
    private void getRole(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
    }
    
    private void getRolePolicy(Event event, RequestInfo resources) {
        getTopLevelParameter("Policy", "policyName", event, resources); 
        getTopLevelResource("Role", "roleName", event, resources); 
    }
    
    private void getUser(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources);
    }
    
    private void getUserPolicy(Event event, RequestInfo resources) {
        getTopLevelParameter("Policy", "policyName", event, resources); 
        getTopLevelResource("User", "userName", event, resources); 
    }
    
    private void listAccessKeys(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources);
    }
    
    private void listAttachedGroupPolicies(Event event, RequestInfo resources) {
        getTopLevelResource("Group", "groupName", event, resources);
    }
    
    private void listAttachedRolePolicies(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
    }
    
    private void listAttachedUserPolicies(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources);
    }
    
    private void listGroupPolicies(Event event, RequestInfo resources) {
        getTopLevelResource("Group", "groupName", event, resources);
    }
    
    private void listGroupForUser(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources);
    }
    
    private void listInstanceProfileForRole(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
    }
    
    private void listRolePolicies(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
    }
    
    private void listPublicKeys(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources);
    }
    
    private void listUserPolicies(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources);
    }
    
    private void putRolePolicy(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources);
        getTopLevelParameter("Policy", "policyName", event, resources);
    }
    
    private void putUserPolicy(Event event, RequestInfo resources) {
        getTopLevelParameter("Policy", "policyName", event, resources); 
        getTopLevelResource("User", "userName", event, resources); 
    }
    
    private void removeRoleFromInstaceProfile(Event event, RequestInfo resources) {
        getTopLevelResource("Instance Profile", "instanceProfileName", event, resources); 
        
        getTopLevelParameter("Role", "roleName", event, resources); 
    }
    
    private void removeUserFromGroup(Event event, RequestInfo resources) {
        getTopLevelResource("Group", "groupName", event, resources); 
        
        getTopLevelParameter("User", "userName", event, resources); 
    }
    
    private void uploadServerCertificate(Event event, RequestInfo resources) {
        getTopLevelResource("Certificate", "serverCertificateName", event, resources); 
        
        getTopLevelParameter("Certificate Body", "certificateBody", event, resources);
        getTopLevelParameter("Certificate Chain", "certificateChain", event, resources);
    }
    
    private void updateServceCertificate(Event event, RequestInfo resources) {
        getTopLevelResource("Certificate", "serverCertificateName", event, resources); 
        getTopLevelResource("New Certificate", "newServerCertificateName", event, resources); 
    }
    
    private void updateAssumeRole(Event event, RequestInfo resources) {
        getTopLevelResource("Role", "roleName", event, resources); 
        
        getTopLevelParameter("Policy", "policyDocument", event, resources);
    }
    
    private void updateAccessKey(Event event, RequestInfo resources) {
        getTopLevelResource("User", "userName", event, resources); 
        
        getTopLevelParameter("Status", "status", event, resources);
        getTopLevelParameter("Access Key", "accessKeyId", event, resources);
    }
}