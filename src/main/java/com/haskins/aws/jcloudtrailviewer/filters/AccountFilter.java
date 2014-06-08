package com.haskins.aws.jcloudtrailviewer.filters;

import com.haskins.aws.jcloudtrailviewer.models.Event;

/**
 *
 * @author mark
 */
public class AccountFilter extends AbstractEventFilter {
    
    private String account;
    
    public void setAccount(String account) {
        
        this.account = account;
        filterChanged();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract implementations
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (account == null || account.trim().length() == 0) {
            
            passesFilter = true;
            
        } else {
            
            String needle = event.getUserIdentity().getAccountId();

            if (needle != null && needle.equalsIgnoreCase(this.account)) {
                passesFilter = true;
            }
        }
        

        
        return passesFilter;
    }
}
