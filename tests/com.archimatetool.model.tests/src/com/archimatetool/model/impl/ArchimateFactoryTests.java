/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IProperty;



@SuppressWarnings("nls")
public class ArchimateFactoryTests {
    
    @Test
    public void createBounds_WithParamaters() {
        IBounds bounds = IArchimateFactory.eINSTANCE.createBounds(1, 2, 3, 4);
        assertNotNull(bounds);
        assertEquals(1, bounds.getX());
        assertEquals(2, bounds.getY());
        assertEquals(3, bounds.getWidth());
        assertEquals(4, bounds.getHeight());
    }
    

    @Test
    public void createProperty_WithParamaters() {
        IProperty p = IArchimateFactory.eINSTANCE.createProperty("key", "value");
        assertNotNull(p);
        assertEquals("key", p.getKey());
        assertEquals("value", p.getValue());
    }
}
