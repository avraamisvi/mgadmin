package br.org.isvi.mgadmin.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeUtil {

	public interface ConfigEditorConditional {
		boolean canEdit(TreeItem item);
	}
	
	public static void configEditorCol(final Tree treeUserMode, Shell shell, final int col, final ConfigEditorConditional cond) {
		final TreeItem [] lastItem = new TreeItem [1];
		final TreeEditor editor = new TreeEditor (treeUserMode);
		final Color black = shell.getDisplay().getSystemColor (SWT.COLOR_BLACK);
		
		treeUserMode.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				final TreeItem item = (TreeItem) event.item;
				
				if(cond!= null) {
					if(!cond.canEdit(item)) {
						return;
					}
				}
				
				if (item != null && item == lastItem [0]) {
					boolean showBorder = true;
					final Composite composite = new Composite (treeUserMode, SWT.NONE);
					if (showBorder) composite.setBackground (black);
					final Text text = new Text (composite, SWT.NONE);
					final int inset = showBorder ? 1 : 0;
					composite.addListener (SWT.Resize, new Listener () {
						public void handleEvent (Event e) {
							Rectangle rect = composite.getClientArea ();
							text.setBounds (rect.x + inset, rect.y + inset, rect.width - inset * 2, rect.height - inset * 2);
						}
					});
					Listener textListener = new Listener () {
						public void handleEvent (final Event e) {
							switch (e.type) {
								case SWT.FocusOut:
									item.setText (col, text.getText ());
									composite.dispose ();
									break;
								case SWT.Verify:
									String newText = text.getText ();
									String leftText = newText.substring (0, e.start);
									String rightText = newText.substring (e.end, newText.length ());
									GC gc = new GC (text);
									Point size = gc.textExtent (leftText + e.text + rightText);
									gc.dispose ();
									size = text.computeSize (size.x, SWT.DEFAULT);
									editor.horizontalAlignment = SWT.LEFT;
									Rectangle itemRect = item.getBounds (), rect = treeUserMode.getClientArea ();
									editor.minimumWidth = Math.max (size.x, itemRect.width) + inset * 2;
									int left = itemRect.x, right = rect.x + rect.width;
									editor.minimumWidth = Math.min (editor.minimumWidth, right - left);
									editor.minimumHeight = size.y + inset * 2;
									editor.layout ();
									break;
								case SWT.Traverse:
									switch (e.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText (col, text.getText ());
											//FALL THROUGH
										case SWT.TRAVERSE_ESCAPE:
											composite.dispose ();
											e.doit = false;
									}
									break;
							}
						}
					};
					text.addListener (SWT.FocusOut, textListener);
					text.addListener (SWT.Traverse, textListener);
					text.addListener (SWT.Verify, textListener);
					editor.setEditor (composite, item);
					editor.setColumn(col);
					text.setText (item.getText (col));
					text.selectAll ();
					text.setFocus ();
				}
				lastItem [0] = item;
			}});
			
	}
}
