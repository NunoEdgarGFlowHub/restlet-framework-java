/**
 * Copyright 2005-2010 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.ext.dataservices.internal.edm;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of entities and associations defined on a schema.
 * 
 * @author Thierry Boileau
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399557.aspx">Entity
 *      Containers (EDM)</a>
 */
public class EntityContainer extends NamedObject {
    /** The list of associations. */
    private List<AssociationSet> associations;

    /** Is this container the default one for a WCF Data Service? */
    private boolean defaultEntityContainer;

    /** The list of entities. */
    private List<EntitySet> entities;

    /** The container this container inherits from. */
    private EntityContainer extended;

    /**
     * Constructor.
     * 
     * @param name
     *            The name of this entity container.
     */
    public EntityContainer(String name) {
        super(name);
    }

    /**
     * Returns the list of associations.
     * 
     * @return The list of associations.
     */
    public List<AssociationSet> getAssociations() {
        if (associations == null) {
            associations = new ArrayList<AssociationSet>();
        }
        return associations;
    }

    /**
     * Returns the list of entities.
     * 
     * @return The list of entities.
     */
    public List<EntitySet> getEntities() {
        if (entities == null) {
            entities = new ArrayList<EntitySet>();
        }
        return entities;
    }

    /**
     * Returns the container this container inherits from.
     * 
     * @return The container this container inherits from.
     */
    public EntityContainer getExtended() {
        return extended;
    }

    /**
     * Return true if this container is the default one for a WCF data
     * service.
     * 
     * @return True if this container is the default one for a WCF data
     *         service.
     */
    public boolean isDefaultEntityContainer() {
        return defaultEntityContainer;
    }

    /**
     * Sets the list of associations.
     * 
     * @param associations
     *            The list of associations.
     */
    public void setAssociations(List<AssociationSet> associations) {
        this.associations = associations;
    }

    /**
     * Indicates if this container is the default one for a WCF data
     * service.
     * 
     * @param defaultEntityContainer
     *            True if this container is the default one for a WCF data
     *            service.
     */
    public void setDefaultEntityContainer(boolean defaultEntityContainer) {
        this.defaultEntityContainer = defaultEntityContainer;
    }

    /**
     * Sets the list of entities.
     * 
     * @param entities
     *            The list of entities.
     */
    public void setEntities(List<EntitySet> entities) {
        this.entities = entities;
    }

    /**
     * Sets the container this container inherits from.
     * 
     * @param extended
     *            The container this container inherits from.
     */
    public void setExtended(EntityContainer extended) {
        this.extended = extended;
    }

}
