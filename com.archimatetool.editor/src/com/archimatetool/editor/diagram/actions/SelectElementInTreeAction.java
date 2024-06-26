/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelReference;


/**
 * Action to select the current element in the Tree
 * 
 * @author Phillip Beauvoir
 */
public class SelectElementInTreeAction extends SelectionAction {

    public static final String ID = "SelectElementInTreeAction"; //$NON-NLS-1$

    public SelectElementInTreeAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.SelectElementInTreeAction_0);
        setToolTipText(Messages.SelectElementInTreeAction_1);
        setId(ID);
        
        // Register for key binding
        setActionDefinitionId("com.archimatetool.editor.selectInModelTree"); //$NON-NLS-1$
        IHandlerService service = part.getSite().getService(IHandlerService.class);
        service.activateHandler(getActionDefinitionId(), new ActionHandler(this));
    }

    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        List<Object> elements = new ArrayList<Object>();
        
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModel) {
                    elements.add(model);
                }
                else if(model instanceof IDiagramModelReference) {
                    elements.add(((IDiagramModelReference)model).getReferencedModel());
                }
                else if(model instanceof IDiagramModelArchimateComponent) {
                    elements.add(((IDiagramModelArchimateComponent)model).getArchimateConcept());
                }
            }
        }
        
        ITreeModelView view = (ITreeModelView)ViewManager.showViewPart(ITreeModelView.ID, true);
        if(view != null) {
            view.getViewer().setSelection(new StructuredSelection(elements), true);
        }
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> list = getSelectedObjects();
        
        if(list.isEmpty()) {
            return false;
        }
        
        for(Object object : list) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModel || model instanceof IDiagramModelReference ||
                        model instanceof IDiagramModelArchimateConnection || model instanceof IDiagramModelArchimateObject) {
                    return true;
                }
            }
        }
        
        return false;
    }

}
