/**
 * Created on Sep 8, 2005
 *
 * $Id: UserTxSessionHolder.java,v 1.1 2005/09/26 10:21:49 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.jackrabbit;

import javax.jcr.Session;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.jackrabbit.core.XASession;
import org.springmodules.jcr.SessionHolder;

/**
 * Extension of Session Holder which includes a UserTransaction which handles
 * the XASession returned by the JackRabbit repository implementation.
 * 
 * @author Costin Leau
 * 
 */
public class UserTxSessionHolder extends SessionHolder {

    private UserTransaction transaction;

    /**
     * @param session
     */
    public UserTxSessionHolder(Session session) {
        super(session);
    }

    /**
     * @return Returns the transaction.
     */
    public UserTransaction getTransaction() {
        return transaction;
    }

    /**
     * @see org.springmodules.jcr.SessionHolder#setSession(javax.jcr.Session)
     */
    public void setSession(Session session) {
        if (!(session instanceof XASession))
            throw new IllegalArgumentException("Session not of type XASession");

        transaction = new JackRabbitUserTransaction(session);
        super.setSession(session);
    }

    /**
     * @see org.springframework.transaction.support.ResourceHolderSupport#clear()
     */
    public void clear() {
        super.clear();
        transaction = null;
    }

}
