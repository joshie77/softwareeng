package simori.Tests.GuiTests;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simori.AudioFeedbackSystem;
import simori.MIDISoundSystem;
import simori.MatrixModel;
import simori.ModeController;
import simori.SimoriGui.KeyboardMapping;
import simori.SwingGui.GuiProperties;
import simori.Exceptions.SimoriNonFatalException;
import simori.Modes.Mode;
import simori.Modes.PerformanceMode;
import simori.Modes.QwertyKeyboard;
import simori.SwingGui.GuiProperties;

/**
 * Class that tests SimoriJFrame
 * It uses the MockSimoriJFrame to access the normally private/protected fields
 * @author Jurek
 */
public class TestSimoriJFrame {
	MockSimoriJFrame jframe;
	KeyboardMapping mapping;
	private static final Color TESTCOLOUR = GuiProperties.CIRCLE_GREYED;
	
	/**
	 * @author Jurek
	 * @throws Exception
	 */
	@Before
	public void setUp() {
		mapping = new QwertyKeyboard((byte)16, (byte)16);
		jframe = new MockSimoriJFrame(mapping);
		
	}

	/**
	 * @author Jurek
	 * @throws Exception
	 */
	@After
	public void tearDown() {
		jframe = null;
		mapping = null;
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testSetGrid() {
		boolean[][] grid = new boolean[16][16];
		grid[5][5] = true;
		jframe.setGrid(grid);
		
		//check that only 5,5 LED got changed
		assertTrue(jframe.getLedPanel().getLed((byte)5, (byte)5).getIlluminated());
		assertFalse(jframe.getLedPanel().getLed((byte)0, (byte)0).getIlluminated());
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testClearGrid() {
		boolean[][] grid = new boolean[16][16];
		grid[5][5] = true;
		jframe.setGrid(grid);
		jframe.clearGrid();
		assertFalse(jframe.getLedPanel().getLed((byte)5, (byte)5).getIlluminated());
	}
	
	/**
	 * Since both tests would be the same (as there is no other way
	 * of getting or settings the text field), and the methods in question
	 * are simply an assignment and a return, this unit test tests
	 * both setText and getText at the same time.
	 * @author Jurek
	 */
	@Test
	public void testSetAndGetText() {
		jframe.setText("test");
		assertEquals(jframe.getText(), "test");
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testSwitchOn() {
		jframe.switchOn();
		//bottom buttons

		assertThat(TESTCOLOUR, not(jframe.getBottomBar().getButton(0).getFillColour()));
		//left buttons
		assertThat(TESTCOLOUR, not(jframe.getLeftBar().getButton(0).getFillColour()));
		assertThat(TESTCOLOUR, not(jframe.getLeftBar().getButton(1).getFillColour()));
		assertThat(TESTCOLOUR, not(jframe.getLeftBar().getButton(2).getFillColour()));
		assertThat(TESTCOLOUR, not(jframe.getLeftBar().getButton(3).getFillColour()));
		//right buttons
		assertThat(TESTCOLOUR, not(jframe.getRightBar().getButton(0).getFillColour()));
		assertThat(TESTCOLOUR, not(jframe.getRightBar().getButton(1).getFillColour()));
		assertThat(TESTCOLOUR, not(jframe.getRightBar().getButton(2).getFillColour()));
		assertThat(TESTCOLOUR, not(jframe.getRightBar().getButton(3).getFillColour()));

	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testSwitchOff() {
		jframe.switchOn();
		jframe.switchOff();
		//bottom buttons

		assertEquals(TESTCOLOUR, jframe.getBottomBar().getButton(0).getFillColour());
		//left buttons
		assertEquals(TESTCOLOUR, jframe.getLeftBar().getButton(0).getFillColour());
		assertEquals(TESTCOLOUR, jframe.getLeftBar().getButton(1).getFillColour());
		assertEquals(TESTCOLOUR, jframe.getLeftBar().getButton(2).getFillColour());
		assertEquals(TESTCOLOUR, jframe.getLeftBar().getButton(3).getFillColour());
		//right buttons
		assertEquals(TESTCOLOUR, jframe.getRightBar().getButton(0).getFillColour());
		assertEquals(TESTCOLOUR, jframe.getRightBar().getButton(1).getFillColour());
		assertEquals(TESTCOLOUR, jframe.getRightBar().getButton(2).getFillColour());
		assertEquals(TESTCOLOUR, jframe.getRightBar().getButton(3).getFillColour());

	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testSetAndGetGridButtonListener() {
		try {
			MatrixModel model = new MatrixModel(16, 16);
			MIDISoundSystem midi = new MIDISoundSystem(false);
			AudioFeedbackSystem audio = new AudioFeedbackSystem(midi, model);
			ModeController mc = new ModeController(jframe, model, audio, 20160);
			mc.setComponentsToPowerToggle(model);
			Mode mode = new PerformanceMode(mc);
			jframe.setGridButtonListener(mode);
			assertEquals(mode, jframe.getGridButtonListener());
			mc.setOn(false, false);
		} catch (SimoriNonFatalException e) {
			fail();
		}
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testSetAndGetFunctionButtonListener() {
		try {
			MatrixModel model = new MatrixModel(16, 16);
			MIDISoundSystem midi = new MIDISoundSystem(false);
			AudioFeedbackSystem audio = new AudioFeedbackSystem(midi, model);
			ModeController mc = new ModeController(jframe, model, audio, 20160);
			mc.setComponentsToPowerToggle(model);
			Mode mode = new PerformanceMode(mc);
			jframe.setFunctionButtonListener(mode);
			assertEquals(mode, jframe.getFunctionButtonListener());
			mc.setOn(false, false);
		} catch (SimoriNonFatalException e) {
			fail();
		}
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testSetKeyboardShown() {
		jframe.setKeyboardShown(true);
		String keyboardShownTrue = jframe.getGridPanel().getComponent(0).toString();
		jframe.setKeyboardShown(false);
		String keyboardShownFalse = jframe.getGridPanel().getComponent(0).toString();
		assertThat(keyboardShownTrue, not(keyboardShownFalse));
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testGetKeyboardMapping() {
		assertEquals(mapping, jframe.getKeyboardMapping());
	}

	/**
	 * @author Jurek
	 */
	@Test
	public void testMouseDraggedInvalid() {
		Point p = jframe.getLocation();
		jframe.mouseDragged(new MouseEvent(jframe, 0, 0, 0, -5, -5, 1, false));
		assertEquals(new Point(p.x, p.y), jframe.getLocation());
	}

	/**
	 * @author Jurek
	 */
	@Test
	public void testMouseDraggedValid() {
		Point p = jframe.getLocation();
		jframe.mouseDragged(new MouseEvent(jframe, 0, 0, 0, 50, 50, 1, false));
		assertEquals(new Point(p.x+50, p.y+50), jframe.getLocation());
	}

	/**
	 * @author Jurek
	 */
	@Test
	public void testMouseMovedNotValid() {
		Point p = jframe.getTopBar().getLocation();
		jframe.mouseMoved(new MouseEvent(jframe, 0, 0, 0, p.x-5, p.y-5, 1, false));
		assertFalse(jframe.getCouldDragBefore());
		
	}
	
	/**
	 * @author Jurek
	 */
	@Test
	public void testMouseValid() {
		Point p = jframe.getTopBar().getLocation();
		jframe.mouseMoved(new MouseEvent(jframe, 0, 0, 0, p.x, p.y, 1, false));
		assertTrue(jframe.getCouldDragBefore());
	}	
}

