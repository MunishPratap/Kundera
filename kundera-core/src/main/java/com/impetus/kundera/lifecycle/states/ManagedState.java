/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.kundera.lifecycle.states;


import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;

import com.impetus.kundera.client.Client;
import com.impetus.kundera.graph.Node;
import com.impetus.kundera.graph.NodeLink;
import com.impetus.kundera.graph.ObjectGraphBuilder;
import com.impetus.kundera.graph.NodeLink.LinkProperty;
import com.impetus.kundera.lifecycle.NodeStateContext;
import com.impetus.kundera.metadata.KunderaMetadataManager;
import com.impetus.kundera.metadata.model.EntityMetadata;
import com.impetus.kundera.metadata.model.Relation;
import com.impetus.kundera.persistence.context.PersistenceCache;

/**
 * @author amresh
 *
 */
public class ManagedState extends NodeState
{
    @Override
    public void initialize(NodeStateContext nodeStateContext)
    {
    }

    @Override
    public void handlePersist(NodeStateContext nodeStateContext)
    {
        //Ignored, entity remains in the same state
        //TODO: Cascade persist operation for related entities for whom cascade=ALL or PERSIST
    }   

    @Override
    public void handleRemove(NodeStateContext nodeStateContext)
    {
        // Managed ---> Removed
        nodeStateContext.setCurrentNodeState(new RemovedState());
        
        //Mark entity for removal in persistence context
        nodeStateContext.setDirty(true);        
        
        //Recurse remove operation for all related entities for whom cascade=ALL or REMOVE      
        Map<NodeLink, Node> children = nodeStateContext.getChildren();
        if(children != null) {       //Nothing to do for leaf nodes
            for(NodeLink nodeLink : children.keySet()) {
                List<CascadeType> cascadeTypes = (List<CascadeType>) nodeLink.getLinkProperty(LinkProperty.CASCADE);
                if(cascadeTypes.contains(CascadeType.REMOVE) || cascadeTypes.contains(CascadeType.ALL)) {
                    Node childNode = children.get(nodeLink);                
                    childNode.remove();
                }
            }
        }
    }

    @Override
    public void handleRefresh(NodeStateContext nodeStateContext)
    {
        //TODO: Refresh entity state from the database
        //TODO: Cascade refresh operation for all related entities for whom cascade=ALL or REFRESH
    }

    @Override
    public void handleMerge(NodeStateContext nodeStateContext)
    {
      //Ignored, entity remains in the same state
      //TODO: Cascade manage operation for all related entities for whom cascade=ALL or MERGE
    }
    
    @Override
    public void handleFind(NodeStateContext nodeStateContext)
    {
        //Fetch Node data from Client
        Client client = nodeStateContext.getClient();
        Class<?> nodeDataClass = nodeStateContext.getDataClass();
        EntityMetadata entityMetadata = KunderaMetadataManager.getEntityMetadata(nodeDataClass);
        String entityId = ObjectGraphBuilder.getEntityId(nodeStateContext.getNodeId());
        Object nodeData = client.find(nodeDataClass, entityMetadata, entityId, null);
        
        nodeStateContext.setData(nodeData);
        
        //This node is fresh and hence NOT dirty
        nodeStateContext.setDirty(false);
        
        //Store this node into persistence cache (with ManagedState)
        PersistenceCache.INSTANCE.getMainCache().addNodeToCache((Node)nodeStateContext);        
        
        //Node to remain in Managed state
        
        /**
         * Find all child nodes for this node 
         */
        //Iterate over relations and construct children nodes
        /*for(Relation relation : entityMetadata.getRelations()) {
            Node childNode = new No
        }*/
    }

    @Override
    public void handleClose(NodeStateContext nodeStateContext)
    {
        nodeStateContext.setCurrentNodeState(new DetachedState());
    }

    @Override
    public void handleClear(NodeStateContext nodeStateContext)
    {
    }

    @Override
    public void handleFlush(NodeStateContext nodeStateContext)
    {        
        //Entity state to remain as Managed     
        
        //Flush this node to database       
        Client client = nodeStateContext.getClient();
        client.persist((Node)nodeStateContext);
        
        //Since node is flushed, mark it as NOT dirty
        nodeStateContext.setDirty(false);
        
    }

    @Override
    public void handleLock(NodeStateContext nodeStateContext)
    {
    }

    @Override
    public void handleDetach(NodeStateContext nodeStateContext)
    {
    }

    @Override
    public void handleCommit(NodeStateContext nodeStateContext)
    {
        nodeStateContext.setCurrentNodeState(new DetachedState());
    }

    @Override
    public void handleRollback(NodeStateContext nodeStateContext)
    {
        //If persistence context is EXTENDED
        nodeStateContext.setCurrentNodeState(new TransientState());
        
        //If persistence context is TRANSACTIONAL
        //context.setCurrentEntityState(new DetachedState());
    }

    @Override
    public void handleGetReference(NodeStateContext nodeStateContext)
    {
    }

    @Override
    public void handleContains(NodeStateContext nodeStateContext)
    {
    }   
    
    
}