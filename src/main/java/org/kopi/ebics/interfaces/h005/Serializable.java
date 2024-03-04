package org.kopi.ebics.interfaces.h005;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The `Serializable` is an element that can be stored in a disk support,
 * files or databases. The save process can be launched via the method
 * [Serializable.serialize]
 *
 */
public interface Serializable {

    /**
     * Writes all persistable attributes to the given stream.
     * @param oos the given stream.
     * @throws IOException save process failed
     */
    void serialize(ObjectOutputStream oos) throws IOException;

    /**
     * Returns the save name of this savable object.
     * @return the save name
     */
    String saveName();
}
