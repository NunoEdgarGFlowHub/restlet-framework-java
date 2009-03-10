/**
 * Copyright 2005-2009 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of the following open
 * source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.sun.com/cddl/cddl.html
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine/.
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.engine.local;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 * Local entity based on an entry in a Zip archive.
 * 
 * @author Remi Dewitte <remi@gide.net>
 */
public class ZipEntryEntity extends Entity {

    /** The Zip file. */
    protected final ZipFile zipFile;

    /** The Zip entry. */
    protected final ZipEntry entry;

    /**
     * Constructor.
     * 
     * @param zipFile
     *            The Zip file.
     * @param entryName
     *            The Zip entry name.
     */
    public ZipEntryEntity(ZipFile zipFile, String entryName) {
        this.zipFile = zipFile;
        ZipEntry entry = zipFile.getEntry(entryName);
        if (entry == null)
            this.entry = new ZipEntry(entryName);
        else {
            // Checking we don't have a directory
            ZipEntry entryDir = zipFile.getEntry(entryName + "/");
            if (entryDir != null)
                this.entry = entryDir;
            else
                this.entry = entry;
        }
    }

    /**
     * Constructor.
     * 
     * @param zipFile
     *            The Zip file.
     * @param entry
     *            The Zip entry
     */
    public ZipEntryEntity(ZipFile zipFile, ZipEntry entry) {
        this.zipFile = zipFile;
        this.entry = entry;
    }

    @Override
    public boolean exists() {
        if ("".equals(getName()))
            return true;
        else {
            // ZipEntry re = zipFile.getEntry(entry.getName());
            // return re != null;
            return entry.getSize() != -1;
        }
    }

    @Override
    public List<Entity> getChildren() {
        List<Entity> result = null;

        if (isDirectory()) {
            result = new ArrayList<Entity>();
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String n = entry.getName();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (e.getName().startsWith(n)
                        && e.getName().length() != n.length())
                    result.add(new ZipEntryEntity(zipFile, e));
            }
        }

        return result;
    }

    @Override
    public String getName() {
        return entry.getName();
    }

    @Override
    public Entity getParent() {
        if ("".equals(entry.getName()))
            return null;
        else {
            String n = entry.getName();
            String pn = n.substring(0, n.lastIndexOf('/') + 1);
            return new ZipEntryEntity(zipFile, zipFile.getEntry(pn));
        }
    }

    @Override
    public Representation getRepresentation(MediaType defaultMediaType,
            int timeToLive) {
        return new ZipEntryRepresentation(defaultMediaType, zipFile, entry);
    }

    @Override
    public boolean isDirectory() {
        if ("".equals(entry.getName()))
            return true;
        return entry.isDirectory();
    }

    @Override
    public boolean isNormal() {
        return !entry.isDirectory();
    }

}