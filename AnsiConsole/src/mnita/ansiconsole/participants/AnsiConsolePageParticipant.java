/*
 * Copyright (c) 2012-2022 Mihai Nita and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package mnita.ansiconsole.participants;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

import mnita.ansiconsole.AnsiConsoleActivator;

public class AnsiConsolePageParticipant implements IConsolePageParticipant {
    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }

    @Override
    public void activated() {
        // Nothing to do, but we are forced to implement it for IConsolePageParticipant
    }

    @Override
    public void deactivated() {
        // Nothing to do, but we are forced to implement it for IConsolePageParticipant
    }

    @Override
    public void dispose() {
        AnsiConsoleActivator.getDefault().removeViewerWithPageParticipant(this);
    }

    @Override
    public void init(IPageBookViewPage page, IConsole console) {
        if (page.getControl() instanceof StyledText) {
            StyledText viewer = (StyledText) page.getControl();
            IDocument document = getDocument(viewer);
            if (document == null) {
                return;
            }
            AnsiConsoleStyleListener myListener = new AnsiConsoleStyleListener(document);
            viewer.addLineStyleListener(myListener);
            AnsiConsoleActivator.getDefault().addViewer(viewer, this);
        }
    }

    // Find the document associated with the viewer
    static IDocument getDocument(StyledText viewer) {
        for (Listener listener : viewer.getListeners(ST.LineGetStyle)) {
            if (listener instanceof TypedListener) {
                Object evenListener = ((TypedListener) listener).getEventListener();
                if (evenListener instanceof ITextViewer) {
                    return ((ITextViewer) evenListener).getDocument();
                }
            }
        }
        return null;
    }
}
