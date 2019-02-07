/*
 * Copyright 2018 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apicurio.hub.core.editing.operationprocessors;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.apicurio.hub.core.editing.IEditingSession;
import io.apicurio.hub.core.editing.ISessionContext;
import io.apicurio.hub.core.editing.sessionbeans.BaseOperation;
import io.apicurio.hub.core.editing.sessionbeans.JoinLeaveOperation;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
@Singleton
public class LeaveProcessor implements IOperationProcessor {

    private static Logger logger = LoggerFactory.getLogger(LeaveProcessor.class);

    /**
     * @see io.apicurio.hub.core.editing.operationprocessors.IOperationProcessor#process(io.apicurio.hub.core.editing.IEditingSession, io.apicurio.hub.core.editing.ISessionContext, io.apicurio.hub.core.editing.sessionbeans.BaseOperation)
     */
    @Override
    public void process(IEditingSession editingSession, ISessionContext session, BaseOperation bo) {
        JoinLeaveOperation lOp = (JoinLeaveOperation) bo;
        logger.debug("Received leave operation ", lOp);
        if (bo.getSource() == BaseOperation.SourceEnum.LOCAL) {
            throw new UnsupportedOperationException("Did not expect a local command: " + lOp);
        } else {
            processRemote(editingSession, session, lOp);
        }
    }

    private void processRemote(IEditingSession editingSession, ISessionContext session, JoinLeaveOperation undo) {
        editingSession.sendToAllSessions(session, undo);
        logger.debug("Remote join sent to local clients.");
    }

    /**
     * @see io.apicurio.hub.core.editing.operationprocessors.IOperationProcessor#getOperationName()
     */
    @Override
    public String getOperationName() {
        return "leave";
    }

    /**
     * @see io.apicurio.hub.core.editing.operationprocessors.IOperationProcessor#unmarshallClass()
     */
    @Override
    public Class<? extends BaseOperation> unmarshallClass() {
        return JoinLeaveOperation.class;
    }
}