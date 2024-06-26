/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Bring Forward Action
 * Simply brings the child forward in order by one position
 * 
 * @author Phillip Beauvoir
 */
public class BringForwardAction extends SelectionAction {
    
    public static final String ID = "com.archimatetool.editor.bringForward"; //$NON-NLS-1$
    public static final String TEXT = Messages.BringForwardAction_0;
    
    public BringForwardAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        // Register for key binding
        setActionDefinitionId(ID);
        
        /*
         * Set the selection provider to the viewer and not the global selection provider so that
         * viewer and selection concur.
         * See SelectionAction#update()
         */
        setSelectionProvider(part.getAdapter(GraphicalViewer.class));
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> selected = getSelectedObjects();
        
        // Quick checks
        if(selected.isEmpty()) {
            return false;
        }
        
        for(Object object : selected) {
            if(!(object instanceof EditPart)) {
                return false;
            }
        }

        Command command = createCommand(selected);
        if(command == null) {
            return false;
        }
        return command.canExecute();
    }

    @Override
    public void run() {
        execute(createCommand(getSelectedObjects()));
    }
    
    private Command createCommand(List<?> selection) {
        GraphicalViewer viewer = getWorkbenchPart().getAdapter(GraphicalViewer.class);
        
        CompoundCommand result = new CompoundCommand(Messages.BringForwardAction_0);
        
        for(Object object : selection) {
            if(object instanceof GraphicalEditPart) {
                GraphicalEditPart editPart = (GraphicalEditPart)object;
                Object model = editPart.getModel();
                
                // This can happen if we do things wrong
                if(viewer != editPart.getViewer()) {
                    System.err.println("Wrong selection for viewer in " + getClass()); //$NON-NLS-1$
                }
                
                // Locked
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                
                if(model instanceof IDiagramModelObject) {
                    IDiagramModelObject diagramObject = (IDiagramModelObject)model;
                    IDiagramModelContainer parent = (IDiagramModelContainer)diagramObject.eContainer();
                    
                    /*
                     * Parent can be null when objects are selected (with marquee tool) and transferred from one container
                     * to another and the Diagram Editor updates the enablement state of Actions.
                     */
                    if(parent == null) {
                        continue;
                    }
                    
                    List<IDiagramModelObject> modelChildren = parent.getChildren();
                    int originalPos = modelChildren.indexOf(diagramObject);

                    if(originalPos < modelChildren.size() - 1) {
                        result.add(new BringForwardCommand(parent, originalPos));
                    }
                }
            }
        }

        return result.unwrap();
    }
    
    private static class BringForwardCommand extends Command {
        private IDiagramModelContainer fParent;
        private int fOldPos;
        
        public BringForwardCommand(IDiagramModelContainer parent, int oldPos) {
            fParent = parent;
            fOldPos = oldPos;
            setLabel(Messages.BringForwardAction_0);
        }

        @Override
        public boolean canExecute() {
            return fParent != null && fOldPos < fParent.getChildren().size() - 1;
        }
        
        @Override
        public void execute() {
            fParent.getChildren().move(fOldPos + 1, fOldPos);
        }
        
        @Override
        public void undo() {
            fParent.getChildren().move(fOldPos, fOldPos + 1);
        }
        
        @Override
        public void dispose() {
            fParent = null;
        }
    }
}
