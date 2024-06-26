/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.testingtools.ArchimateTestModel;


@SuppressWarnings("nls")
public class OutlineOpacityHandlerTests {
    
    private static OutlineOpacityHandler handler;
    
    @BeforeAll
    public static void runOnceBeforeAllTests() {
        handler = new OutlineOpacityHandler();
    }
    
    @Test
    public void testIsVersion() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        String[] oldVersionsDontConvert = { "2.3.0", "2.6.0", "3.0.0", "3.1.1", "4.0.0"};
        
        for(String version : oldVersionsDontConvert) {
            model.setVersion(version);
            assertFalse(handler.isVersion(model));
        }

        String[] oldVersionsDoConvert = { "4.0.1", "4.4.0"};
        
        for(String version : oldVersionsDoConvert) {
            model.setVersion(version);
            assertTrue(handler.isVersion(model));
        }
        
        String[] newVersions = { "4.4.1", "4.5.0", "4.6.0", "4.6.1", "4.6.2", "4.7.0", "4.7.1", "5.0.0"};
        
        for(String version : newVersions) {
            model.setVersion(version);
            assertFalse(handler.isVersion(model));
        }
    }

    @Test
    public void testSetDefaultOutlineOpacity() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IDiagramModelObject dmo = tm.createDiagramModelArchimateObjectAndAddToModel(IArchimateFactory.eINSTANCE.createBusinessActor());
        dmo.setAlpha(100);
        model.getDefaultDiagramModel().getChildren().add(dmo);

        handler.setDefaultOutlineOpacity(model);
        
        assertEquals(100, dmo.getAlpha());
        assertEquals(100, dmo.getLineAlpha());
    }
}
