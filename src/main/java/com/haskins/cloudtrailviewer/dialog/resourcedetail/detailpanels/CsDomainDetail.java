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
package com.haskins.cloudtrailviewer.dialog.resourcedetail.detailpanels;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearchClient;
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch;
import com.amazonaws.services.cloudsearchv2.model.DescribeDomainsRequest;
import com.amazonaws.services.cloudsearchv2.model.DescribeDomainsResult;
import com.amazonaws.services.cloudsearchv2.model.DomainStatus;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class CsDomainDetail extends AbstractDetail {

    public CsDomainDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonCloudSearch client = new AmazonCloudSearchClient(credentials);
            client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeDomainsRequest request = new DescribeDomainsRequest();
            request.setDomainNames(Arrays.asList(detailRequest.getResourceName()));
            
            DescribeDomainsResult result = client.describeDomains(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
        }

        return response;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }
    
    private void buildUI(DescribeDomainsResult detail) {
        
        if (!detail.getDomainStatusList().isEmpty()) {
            
            List<DomainStatus> domains = detail.getDomainStatusList();
            DomainStatus domain = domains.get(0);
            
            if (domain.getARN() != null) { primaryTableModel.addRow(new Object[]{"Arn", domain.getARN()}); }
            if (domain.getCreated() != null) { primaryTableModel.addRow(new Object[]{"Created", domain.getCreated()}); }
            if (domain.getDeleted() != null) { primaryTableModel.addRow(new Object[]{"Deleted", domain.getDeleted()}); }
            if (domain.getDocService() != null) { primaryTableModel.addRow(new Object[]{"Document Service", domain.getDocService().getEndpoint()}); }
            if (domain.getDomainId() != null) { primaryTableModel.addRow(new Object[]{"Domain Id", domain.getDomainId()}); }
            if (domain.getDomainName()!= null) { primaryTableModel.addRow(new Object[]{"Domain Name", domain.getDomainName()}); }
            if (domain.getProcessing()!= null) { primaryTableModel.addRow(new Object[]{"Processing", domain.getProcessing()}); }
            if (domain.getRequiresIndexDocuments()!= null) { primaryTableModel.addRow(new Object[]{"Requires Index Document", domain.getProcessing()}); }
            if (domain.getSearchInstanceCount()!= null) { primaryTableModel.addRow(new Object[]{"Search Status Cound", domain.getSearchInstanceCount()}); }
            if (domain.getSearchInstanceType()!= null) { primaryTableModel.addRow(new Object[]{"Search Instance Type", domain.getSearchInstanceType()}); }
            if (domain.getSearchPartitionCount()!= null) { primaryTableModel.addRow(new Object[]{"Search Partition Count", domain.getSearchPartitionCount()}); }
            if (domain.getSearchService()!= null) { primaryTableModel.addRow(new Object[]{"Search Service", domain.getSearchService().getEndpoint()}); }
        }
        
    }
    
}
