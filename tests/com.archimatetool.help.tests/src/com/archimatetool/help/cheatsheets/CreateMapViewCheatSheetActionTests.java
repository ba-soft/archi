/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.cheatsheets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.archimatetool.help.cheatsheets.CreateMapViewCheatSheetAction.NewMapViewCommand;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;


public class CreateMapViewCheatSheetActionTests {
    
    @Test
    public void testNewMapViewCommand() throws IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        IArchimateModel model = tm.loadModel();
        
        // Has 17 diagrams
        assertEquals(17, model.getDiagramModels().size());

        NewMapViewCommand cmd = new NewMapViewCommand(model);
        cmd.execute();
        
        assertNotNull(cmd.diagramModel);
        assertNotNull(cmd.diagramModel.eContainer());
        
        // Model now has 18 diagrams
        assertEquals(18, model.getDiagramModels().size());
        
        // New diagram has 17 children...
        assertEquals(17, cmd.diagramModel.getChildren().size());
        
        // ...all of which are IDiagramModelReference types with IDs
        for(IDiagramModelObject dmo : cmd.diagramModel.getChildren()) {
            assertTrue(dmo instanceof IDiagramModelReference);
            assertNotNull(dmo.getId());
        }
        
        // Undo
        cmd.undo();
        assertEquals(17, model.getDiagramModels().size());
        assertNull(cmd.diagramModel.eContainer());
    }
    
    
}
