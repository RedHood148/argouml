/* $Id: CrInvalidBranch.java 17849 2010-01-12 19:50:34Z linus $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
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

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a Branch (i.e. Choice or Junction)
 * state has the wrong number of transitions.
 * Implements constraint [5] and [6] on PseudoState in the UML
 * Semantics v1.3, p. 2-140:
 *
 * [5] A junction vertex must have at least one incoming and
 * one outgoing transition.
 * (self.kind = #junction) implies
 *     ((self.incoming->size >= 1) and (self.outgoing->size >= 1))
 *
 * [6] A choice vertex must have at least one incoming and
 * one outgoing transition.
 * (self.kind = #choice) implies
 *     ((self.incoming->size >= 1) and (self.outgoing->size >= 1))
 *
 * Well-formedness rule [7] and [8] for PseudoState. See page 138 of UML 1.4
 * Semantics. OMG document UML 1.4.2 formal/04-07-02.
 * 
 * @author jrobbins
 */
public class CrInvalidBranch extends CrUML {

    /**
     * The constructor.
     */
    public CrInvalidBranch() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.STATE_MACHINES);
	addTrigger("incoming");
    }


    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAPseudostate(dm))) {
	    return NO_PROBLEM;
	}
	Object k = Model.getFacade().getKind(dm);
	if ((!Model.getFacade().equalsPseudostateKind(k,
	        Model.getPseudostateKind().getChoice()))
            && (!Model.getFacade().equalsPseudostateKind(k,
                    Model.getPseudostateKind().getJunction()))) {
	    return NO_PROBLEM;
	}
	Collection outgoing = Model.getFacade().getOutgoings(dm);
	Collection incoming = Model.getFacade().getIncomings(dm);
	int nOutgoing = outgoing == null ? 0 : outgoing.size();
	int nIncoming = incoming == null ? 0 : incoming.size();
	if (nIncoming < 1) {
	    return PROBLEM_FOUND;
	}
	if (nOutgoing < 1) {
	    return PROBLEM_FOUND;
	}
	return NO_PROBLEM;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedDesignMaterials()
     */
    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getPseudostate());
        return ret;
    }
    
}

