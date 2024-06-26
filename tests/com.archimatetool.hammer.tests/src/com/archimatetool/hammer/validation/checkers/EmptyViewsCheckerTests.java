/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.archimatetool.hammer.validation.issues.AdviceType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;


@SuppressWarnings("nls")
public class EmptyViewsCheckerTests {
    
    @Test
    public void testGetIssues() {
        List<IArchimateDiagramModel> views = new ArrayList<IArchimateDiagramModel>();
        
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setName("view");
        views.add(dm);
        
        EmptyViewsChecker checker = new EmptyViewsChecker(views);
        
        // Should be warning
        List<IIssue> issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertTrue(issues.get(0) instanceof AdviceType);
        assertSame(dm, issues.get(0).getObject());
        
        // Add something to view for no issues
        dm.getChildren().add(IArchimateFactory.eINSTANCE.createDiagramModelGroup());
        issues = checker.getIssues();
        assertEquals(0, issues.size());
    }
    
    
}
