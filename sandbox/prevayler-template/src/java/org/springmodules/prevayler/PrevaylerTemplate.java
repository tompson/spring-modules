package org.springmodules.prevayler;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springmodules.prevayler.callback.DeleteByEntityClassCallback;
import org.springmodules.prevayler.callback.GetByClassCallback;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.callback.SaveCallback;
import org.springmodules.prevayler.callback.DeleteByEntityCallback;
import org.springmodules.prevayler.callback.GetByIdCallback;
import org.springmodules.prevayler.callback.UpdateCallback;
import org.springmodules.prevayler.support.PrevaylerOperationException;
import org.springmodules.prevayler.system.callback.MergeCallback;
import org.springmodules.prevayler.system.callback.SystemCallback;

/**
 * <p>The prevayler template simplifies the creation and use of a prevalent system.</p>
 * <p>A prevalent system is a transparent persistence system based on object serialization.</p>
 * <p>For a prevalent system to work correctly, its business objects must follow two very simple rules. They must be:
 * <ul>
 * <li>Serializable - At any point in time, the system might want to persist an object to disk or other non-volatile media.</li>
 * <li>Deterministic - Given some input, the business object's methods must always return the same output.</li>
 * </ul>
 * If these two rules are satisfied, your business objects can be transparently persisted using a prevalent system implementation like Prevayler.
 * </p>
 * <p>The prevayler template provides a simple, common, abstraction over a prevalent system based on Prevayler.</p>
 * <p>The prevayler template provides common data access methods to use for managing the persistence of your business objects. Under the hood,
 * it uses a {@link org.springmodules.prevayler.system.PrevalentSystem} implementation working as a standard Data Access Object, coordinated
 * by a {@link PersistenceManager}.</p>
 * <p>For obtaining transparent persistence using the {@link PrevaylerTemplate} you just need to configure it by injecting
 * the {@link PersistenceManager} to use.<br>
 * You don't have to implement anything special. Your business objects just need to satisfy the rules above, plus the following:
 * <ul>
 * <li>Every business object must have an <i>id</i> property field, either directly declared or inherited: it is automatically managed by the prevalent system through the
 * {@link org.springmodules.prevayler.id.IdGenerationStrategy} and {@link org.springmodules.prevayler.id.IdNameResolutionStrategy} objects, so
 * <b>you don't have to care about it</b>.</li>
 * </ul>
 * Please note that the prevayler template is <b>best configured and used</b> through a Spring application context.
 * </p>
 * <p>Take a look at other classes javadocs for additional details.</p>
 * <p>
 * <b>Managing persistent objects.</b><br>
 * Every persistent business object you want to modify must be first retrieved from the prevalent system
 * through the template data access methods.<br>
 * You have to pass fresh (newly created) business objects instances only when doing a save: the other methods require you
 * to pass persistent object instances previously retrieved from the prevalent system.<br>
 * Otherwise, you could get unexpected behaviours.
 * </p>
 * <p>
 * <b>Managing transactions.</b><br>
 *  TODO : Talk about it.
 * </p>
 * <p>
 * <b>Cascade persistence.</b><br>
 *  TODO : Talk about it.
 * </p>
 *
 * @author Sergio Bossa
 */
public class PrevaylerTemplate extends PrevaylerAccessor implements PrevaylerOperations {
    
    public Object save(Object entity) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            PrevaylerCallback callback = new SaveCallback(entity);
            Object saved = session.execute(callback);
            if (entity != saved) {
                session.execute(new MergeCallback(saved, entity));
            }
            return saved;
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public Object update(Object entity) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            PrevaylerCallback callback = new UpdateCallback(entity);
            Object saved = session.execute(callback);
            if (entity != saved) {
                session.execute(new MergeCallback(saved, entity));
            }
            return saved;
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public void delete(Object entity) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            PrevaylerCallback callback = new DeleteByEntityCallback(entity);
            session.execute(callback);
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public void delete(Class entityClass) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            PrevaylerCallback callback = new DeleteByEntityClassCallback(entityClass);
            session.execute(callback);
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public Object get(Class entityClass, Object id) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            PrevaylerCallback callback = new GetByIdCallback(entityClass, id);
            return session.execute(callback);
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public List get(Class entityClass) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            PrevaylerCallback callback = new GetByClassCallback(entityClass);
            return (List) session.execute(callback);
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public Object execute(PrevaylerCallback callback) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            return session.execute(callback);
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    public Object execute(SystemCallback callback) {
        Session session = null;
        try {
            session = PersistenceManagerUtils.getSession(this.getPersistenceManager(), true);
            return session.execute(callback);
        } catch(DataAccessException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new PrevaylerOperationException("Exception occured while executing Prevayler operation: " + ex.getMessage(), ex);
        } finally {
            this.closeSession(session);
        }
    }
    
    protected void closeSession(Session session) {
        if (session != null && ! PersistenceManagerUtils.isBound(this.getPersistenceManager(), session)) {
            this.getPersistenceManager().commitTransaction(session);
        }
    }
}
