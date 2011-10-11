/* $Id: PropPanelComment.java 18588 2010-07-28 21:30:25Z bobtarling $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;

/**
 * Proppanel for comments (notes). <p>
 *
 * In UML 1.4 and beyond, the Comment has a "body"
 * attribute to contain the comment string, although 
 * some UML tools continue to use the name attribute.
 * @deprecated in 0.31.2 by Bob Tarling  This is replaced by the XML property
 * panels module
 */
@Deprecated
public class PropPanelComment extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -8781239511498017147L;

    /**
     * Construct a property panel for a Comment.
     */
    public PropPanelComment() {
        super("label.comment", lookupIcon("Comment"));

        addField(Translator.localize("label.name"),
                getNameTextField());

        UMLMutableLinkedList umll = new UMLMutableLinkedList(
                new UMLCommentAnnotatedElementListModel(), null, null);
        umll.setDeleteAction(new ActionDeleteAnnotatedElement());
        addField(Translator.localize("label.annotated-elements"),
            new JScrollPane(umll));

        addSeparator();

        UMLTextArea2 text = new UMLTextArea2(new UMLCommentBodyDocument());
        text.setLineWrap(true);
        text.setRows(5);
        JScrollPane pane = new JScrollPane(text);
        addField(Translator.localize("label.comment.body"), pane);

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }
}
@UmlModelMutator
class UMLCommentBodyDocument extends UMLPlainTextDocument {
    
    /**
     * Constructor for UMLCommentBodyDocument.
     */
    public UMLCommentBodyDocument() {
        super("body"); 
        /*
         * TODO: This is probably not the right location
         * for switching off the "filterNewlines".
         * The setting gets lost after selecting a different
         * ModelElement in the diagram.
         * BTW, see how it is used in
         * javax.swing.text.PlainDocument.
         * See issue 1812.
         */
        putProperty("filterNewlines", Boolean.FALSE);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#setProperty(java.lang.String)
     */
    protected void setProperty(String text) {
        Model.getCoreHelper().setBody(getTarget(), text);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#getProperty()
     */
    protected String getProperty() {
        return (String) Model.getFacade().getBody(getTarget());
    }
    
}

@UmlModelMutator
class ActionDeleteAnnotatedElement extends AbstractActionRemoveElement {
    /**
     * Constructor.
     */
    public ActionDeleteAnnotatedElement() {
        super(Translator.localize("menu.popup.remove"));
    }

    /*
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        super.actionPerformed(arg0);
        Model.getCoreHelper().removeAnnotatedElement(
                getTarget(), getObjectToRemove());
    }
    
}
