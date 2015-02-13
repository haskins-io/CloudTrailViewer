package com.github.githublemming.jcloudtrailerviewer.filter;

import com.github.githublemming.jcloudtrailerviewer.model.Event;

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
