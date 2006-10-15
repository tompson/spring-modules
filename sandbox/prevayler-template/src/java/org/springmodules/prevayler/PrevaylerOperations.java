package org.springmodules.prevayler;

import java.util.List;

/**
 * This interface specifies all data access/management methods each prevayler template should provide.
 *
 * @author Sergio Bossa
 */
public interface PrevaylerOperations {
    
    /**
     * Save a new entity object and returns it.
     * 
     * @param newEntity The entity object to save.
     * @return The saved object.
     */
    public Object save(Object newEntity);

    /**
     * Update e given entity object: the object must be already saved.
     *
     * @param newEntity The entity object to update.
     * @return The updated object.
     */
    public Object update(Object entity);
    
    /**
     * Delete e given entity object: the object must be already saved.
     *
     * @param newEntity The entity object to delete.
     */
    public void delete(Object entity);
    
    /**
     * Delete all objects whose class is the same as or a subclass of the given class.
     *
     * @param entityClass The (super-)class of the objects to delete.
     */
    public void delete(Class entityClass);

    /**
     * Get the object whose class is the same as or a subclass of the given class, and whose id is equal to the given id.
     *
     * @param entityClass The (super-)class of the objects to retrieve.
     * @param id The id of the object to retrieve.
     * @return The retrieved object, or null if no object is found.
     */
    public Object get(Class entityClass, Object id);
    
    /**
     * Get all objects whose class is the same as or a subclass of the given class.
     *
     * @param entityClass The (super-)class of the objects to retrieve.
     * @return A list of objects.
     */
    public List get(Class entityClass);
    
    /**
     * Execute the code contained in the given {@link PrevaylerCallback} into a Prevayler transaction.
     *
     * @param callback The callback to execute.
     * @return The transaction result, if any.
     */
    public Object execute(PrevaylerCallback callback);
}
