package simori;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class FunctionButtonBar extends JPanel {
	
	private Button[] buttons;
	
	public FunctionButtonBar(boolean vertical,
			OnPressListenerMaker maker, FunctionButton... fbs) {
		int axis = vertical ? BoxLayout.PAGE_AXIS : BoxLayout.LINE_AXIS;
		setBackground(SimoriGui.BACKGROUND);
		if (fbs == null) return;
		BoxLayout layout = new BoxLayout(this, axis);
		setLayout(layout);
		addButtons(fbs, maker);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateSize();
			}
		});
	}
	
	private void addButtons(FunctionButton[] fbs,
			OnPressListenerMaker maker) {
		buttons = new Button[fbs.length];
		add(Box.createGlue());
		add(Box.createGlue());
		for (int i = 0; i < fbs.length; i++) {
			if (fbs[i] == null) continue;
			buttons[i] = makeButtonFor(fbs[i], maker);
			add(buttons[i]);
			add(Box.createGlue());
		}
		add(Box.createGlue());
	}
	
	private Button makeButtonFor(FunctionButton fb,
			OnPressListenerMaker maker) {
		Button b = new Button();
		b.setText(fb.buttonName());
		b.setToolTipText(fb.toolTip());
		b.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		b.setOnPressListener(maker.getListener(fb));
		return b;
	}
	
	private void updateSize() {
		float min = Math.min(getWidth(), getHeight());
		int ratio = (int) (min * 5f / 6f);
		Dimension bSize = new Dimension(ratio, ratio);
		for (Button b : buttons) {
			b.setPreferredSize(bSize);
			b.setMaximumSize(bSize);
			b.setMinimumSize(bSize);
		}
	}
}
