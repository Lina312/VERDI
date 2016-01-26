package anl.verdi.plot.gui;

import java.awt.ComponentOrientation;
//import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FontMetrics;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;

import java.awt.Color;

//import javax.swing.SwingConstants;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.RenderingExecutor;

import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Graphics2D;

// class forms the basis for the VERDI panel that displays the entire FastTilePlot
// title, subtitle1, subtitle2, axes, axis ticks and labels, footers, legend, JMapPane for geographic content
public class FastTilePlotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4854488557959521575L;
	// pull declaration of all other components from constructor to here as class-level data members
	private GridBagLayout gbl_contentPane;	// overall layout manager
	private GridBagConstraints c;
	private BoxLayout titlesLayout;
	private JMenuBar bar;				// bar at top of contentPane; plot-specific menu items
	private JToolBar toolBar;			// toolBar just below bar in contentPane; widgets for the
								// user to define what to plot (time step, etc.)
	private JPanel titlesPanel;			// titlesPanel just below toolBar in contentPane; title, subtitles 1 & 2
	private JPanel rangeAxisLabel;		// rangeAxisLabel at left side of contentPane; axis label for Range axis
	private JPanel rangeTickLabels;		// rangeTickLabels just to right of rangeAxisLabel in contentPane; tick marks and values
	private JPanel domainTickLabels;	// domainTickLabels just below chart in contentPane; tick marks and values
	private JPanel domainAxisLabel;		// domainAxisLabel just below domainTickLabels in contentPane; axis label for Domain axis
	private JPanel footersPanel;		// footersPanel below domainAxisLabel in contentPane; footers 1 & 2 & annotations legend
	private JPanel legendPanel;			// legendPanel at right side of contentPane; legend for the tile chart colors
	private JMapPane topMapPanel;		// primary chart (geospatial) for tile chart and associated shapefiles
	
	private final static int BORDER = 1;	// 1 blank row/column between outer line of outermost widget to first widget 
	private final static int BAR_HT = 1;
	private final static int TOOLBAR_HT = 3;
	private final static int TITLES_PANEL_HT = 8;
	private final static int RANGE_AXIS_LABEL_WIDTH = 4;
	private final static int RANGE_TICK_LABELS_WIDTH = 4;
	private final static int TOP_MAP_PANEL_WIDTH = 150;
	private final static int TOP_MAP_PANEL_HT = 100;
	private final static int DOMAIN_TICK_LABELS_HT = 4;
	private final static int DOMAIN_AXIS_LABEL_HT = 4;
	private final static int FOOTERS_PANEL_HT = 4;
	private final static int LEGEND_PANEL_WIDTH = 20;
	private final static int VMIN = 2;	// minimum vertical space between components in same panel
	
	private final static boolean RIGHT_TO_LEFT = false;

	private Font tFont, s1Font, s2Font;				// fonts for title, subtitle1, subtitle2
	private Color tColor, s1Color, s2Color;			// colors for title, subtitle1, subtitle2
	private String tString, s1String, s2String;		// strings to print for title, subtitle1, subtitle2
	
	private MapContent myMapContent;
	private RenderingExecutor myRenderingExecutor;
	private GTRenderer myGTRenderer;

	/**
	 * Create the overall frame and all of its components.
	 */
	public FastTilePlotPanel() {

		setBorder(new EmptyBorder(1, 1, 1, 1));
		
		gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0};
		gbl_contentPane.rowHeights = new int[]{0};
		gbl_contentPane.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{Double.MIN_VALUE};
		if (RIGHT_TO_LEFT) {
			setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		setLayout(gbl_contentPane);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;	//resize horizontally and vertically
		gbl_contentPane.setConstraints(this, c);

		// first define components that go across the top of the frame

		bar = new JMenuBar() {// bar menu strip at top of contentPane
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("JMenuBar.paintComponent");
			}
		};	
		// start upper-left corner of window, 1-cell height, lt. gray background
		bar.setBackground(Color.LIGHT_GRAY ); 
		c.gridx = BORDER;
		c.gridy = BORDER;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = BAR_HT;
		gbl_contentPane.setConstraints(bar, c);
		add(bar);

		toolBar = new JToolBar() {	// contains time step, layer, stats, animation widgets
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("JToolBar.paintComponent");
			}
		};	
		// start just below bar, 3 cell height, lt. gray background
		toolBar.setFloatable(false);
		toolBar.setBackground(Color.LIGHT_GRAY);
		c.gridx = BORDER;
		c.gridy = BORDER + BAR_HT;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = TOOLBAR_HT;
		gbl_contentPane.setConstraints(toolBar, c);
		add(toolBar);

		titlesPanel = new JPanel() {	// contains title, subtitle1, subtitle2
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g); 	// have to start with this 
				System.out.println("in paintComponent, ready to get graphics for titlesPanel");
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("in PaintComponent: g2 = " + g2.toString());
				int yTitle = 0;
				int ys1String = 0;
				int ys2String = 0;
				int width = titlesPanel.getWidth();		// says width = 0
				
				System.out.println("tFont = " + tFont + ", tColor = " + tColor + "tString = " + tString);
				System.out.println("width = " + width);
				
				if(tString != null && !tString.trim().isEmpty())	// draw title if not null or empty
				{
					System.out.println("tString not empty, proceeding, g = " + g2.toString());
					g2.setFont(tFont);
					g2.setColor(tColor);
					FontMetrics tMetrx = g2.getFontMetrics(tFont);
					int xTitle = width/2 - tMetrx.stringWidth(tString)/2;		// here attempt to center the string
					yTitle = VMIN + tMetrx.getHeight()/2; // tFont.getSize()/2;	// change from code in TilePlot.java
					g2.drawString(tString, xTitle, yTitle);
				}
				if(s1String != null && !s1String.trim().isEmpty())
				{
					System.out.println("s1String not empty, proceeding");
					g2.setFont(s1Font);
					g2.setColor(s1Color);
					FontMetrics s1Metrx = g2.getFontMetrics(s1Font);
					int xs1String = width/2 - s1Metrx.stringWidth(s1String)/2;	// here attempt to center the string
					ys1String = VMIN + yTitle + s1Metrx.getHeight()/2;
					g2.drawString(s1String, xs1String, ys1String);
				}
				if(s2String != null && !s2String.trim().isEmpty())
				{
					System.out.println("s2String not empty, proceeding");
					g2.setFont(s2Font);
					g2.setColor(s2Color);
					FontMetrics s2Metrx = g2.getFontMetrics(s2Font);
					int xs2String = width/2 - s2Metrx.stringWidth(s2String)/2;	// here attempt to center the string
					ys2String = VMIN + yTitle + ys1String + s2Metrx.getHeight()/2;
					g2.drawString(s2String, xs2String, ys2String);
				}
				width = titlesPanel.getWidth();
				System.out.println("width of titlesPanel now = " + width);
				
			}
		};
		// start just below toolBar, 8 cell height, white background
		titlesLayout = new BoxLayout(titlesPanel,BoxLayout.Y_AXIS);		// declare BoxLayout for this JPanel
		titlesPanel.setLayout(titlesLayout);
		titlesPanel.setBackground(Color.WHITE);
		titlesPanel.setPreferredSize(new Dimension(100,100));
		c.fill = GridBagConstraints.BOTH;	// adjust this object both horizontally and vertically
		c.gridx = BORDER;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = TITLES_PANEL_HT;
		gbl_contentPane.setConstraints(titlesPanel, c);
		add(titlesPanel);

		// next, define components for left side
		rangeAxisLabel = new JPanel() {	// contains range axis title
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("rangeAxisLabel paintComponent");
			}
		};	
		// go vertically from just above top of domain tick labels object to just under titlesPanel
		rangeAxisLabel.setBackground(Color.WHITE);
		c.gridx = BORDER;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT;
		c.gridwidth = RANGE_AXIS_LABEL_WIDTH;
		c.gridheight = GridBagConstraints.REMAINDER;
		gbl_contentPane.setConstraints(rangeAxisLabel, c);
		add(rangeAxisLabel);

		rangeTickLabels = new JPanel() {	// contains range axis, ticks, and labels
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("rangeTickLabels paintComponent");
			}
		};	
		// go vertically just to right of rangeAxisLabel
		c.gridx = BORDER + RANGE_AXIS_LABEL_WIDTH;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT;
		c.gridwidth = RANGE_TICK_LABELS_WIDTH;
		c.gridheight = GridBagConstraints.REMAINDER;
		gbl_contentPane.setConstraints(rangeTickLabels, c);
		add(rangeTickLabels);

		// next, define components for across bottom
		domainTickLabels = new JPanel() {	// contains domain axis, ticks, and labels
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("domainTickLabels paintComponent");
			}
		};	
		// go horizontally just above domainAxisLabel from right edge of rangeTickLabels
		// to left edge of legendPanel
		c.gridx = BORDER + RANGE_AXIS_LABEL_WIDTH + RANGE_TICK_LABELS_WIDTH;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT + TOP_MAP_PANEL_HT;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = DOMAIN_TICK_LABELS_HT;
		gbl_contentPane.setConstraints(domainTickLabels, c);
		add(domainTickLabels);

		domainAxisLabel = new JPanel() {	// contains domain axis title
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("domainAxisLabel paintComponent");
			}
		};	
		// go horizontally just above footersPanel from right edge of rangeTickLabels 
		// to left edge of legendPanel
		c.gridx = BORDER + RANGE_AXIS_LABEL_WIDTH + RANGE_TICK_LABELS_WIDTH;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT + TOP_MAP_PANEL_HT + DOMAIN_TICK_LABELS_HT;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = DOMAIN_AXIS_LABEL_HT;
		gbl_contentPane.setConstraints(domainAxisLabel, c);
		add(domainAxisLabel);

		footersPanel = new JPanel() {	// contains up to 3 optional footers
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("footersPanel paintComponent");
			}
		};	
		// go horizontally across the entire very bottom of contentPane
		c.gridx = BORDER;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT + TOP_MAP_PANEL_HT + DOMAIN_TICK_LABELS_HT + DOMAIN_AXIS_LABEL_HT;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = FOOTERS_PANEL_HT;
		gbl_contentPane.setConstraints(footersPanel, c);
		add(footersPanel);

		// then, define legend (right side)
		legendPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("legendPanel paintComponent");
			}
		};	
		legendPanel.setBorder(new LineBorder(new Color(0, 0, 0)));		// black BORDER around legend area
		// go vertically just above footersPanel to just below titlesPanel
		c.gridx = BORDER + RANGE_AXIS_LABEL_WIDTH + RANGE_TICK_LABELS_WIDTH + TOP_MAP_PANEL_WIDTH;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT;
		c.gridwidth = LEGEND_PANEL_WIDTH;
		c.gridheight = GridBagConstraints.REMAINDER;
		gbl_contentPane.setConstraints(legendPanel, c);
		add(legendPanel);

		// finally, JMapPane as large, central component
		topMapPanel = new JMapPane() {	// large center of overall contentPane
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				System.out.println("topMapPanel paintComponent");
			}
		};	
		// expand both horizontally and vertically
		// holds geographic data for raster layer (tile plot) and vector layer(s) (geographic boundaries)
		c.gridx = BORDER + RANGE_AXIS_LABEL_WIDTH + RANGE_TICK_LABELS_WIDTH;
		c.gridy = BORDER + BAR_HT + TOOLBAR_HT + TITLES_PANEL_HT;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 1.0;
		gbl_contentPane.setConstraints(topMapPanel, c);
		add(topMapPanel);
		System.out.println("all done with constructing FastTilePlotPanel");
		System.out.println("titlesPanel = " + titlesPanel.toString());
	}
	
	/**
	 * Set a premade JMenuBar object
	 * @param aBar	the JMenuBar object that was already fully populated
	 */
	public void setBar(JMenuBar aBar)
	{
		bar = aBar;
	}
	
	/**
	 * Set a premade JToolBar
	 * @param aToolBar	the JToolBar object that was already fully populated
	 */
	public void setToolBar(JToolBar aToolBar)
	{
		toolBar = aToolBar;
	}
	
	/**
	 * Return the JToolBar
	 */
	public JToolBar getToolBar()
	{
		return toolBar;
	}
	
	public JPanel getTitlesPanel()
	{
		return titlesPanel;
	}
	
	/**
	 * Return the JMapPane container for the tile chart and the shapefiles 
	 * @return the topMapPanel object
	 */
	public JMapPane getMap()
	{
		return topMapPanel;
	}
	
	/** 
	 * Set information for the titlesPanel (title, subtitle1, subtitle2; all optional)
	 * NOTE: t* for title, s1* for subtitle 1, s2* for subtitle 2
	 * Called by gov.epa.emvl.TilePlot
	 */
	public void setTitlesPanel(Font tFont, Color tColor, String tString,
			Font s1Font, Color s1Color, String s1String,
			Font s2Font, Color s2Color, String s2String)
	{
	    this.tFont = tFont;
	    this.tColor = tColor;
	    this.tString = tString;
	    
	    this.s1Font = s1Font;
	    this.s1Color = s1Color;
	    this.s1String = s1String;
	    
	    this.s2Font = s2Font;
	    this.s2Color = s2Color;
	    this.s2String = s2String;
	}
	
//	@Override
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g); 	// have to start with this
//		System.out.println("in master paintComponent");
//	}
	
	public MapContent getMapContent()
	{
		return myMapContent;
	}
	
	public RenderingExecutor getRenderingExecutor()
	{
		return myRenderingExecutor;
	}
	
	public GTRenderer getRenderer()
	{
		return myGTRenderer;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame mainFrame = new JFrame("Main window");
				mainFrame.setPreferredSize(new Dimension(500, 500));
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				FastTilePlotPanel frame = new FastTilePlotPanel();	// called constructor for FastTilePlotPanel
				try {
					System.out.println("FastTilePlotPanel frame = " + frame);
//					frame.setVisible(true);
//					System.out.println("made frame visible, ready to call setTitlesPanel");
					frame.setTitlesPanel(new Font(Font.DIALOG, 1, 15),Color.BLACK,"Here is my TITLE string",
							new Font(Font.SANS_SERIF, 1, 24), Color.BLUE, "Here is a blue subtitle1 string",
							new Font(Font.SERIF,2,33), Color.RED, "Here is a red subtitle2 STRING");
					System.out.println("back from setTitlesPanel");
				} catch (Exception e) {
					System.out.println("here I caught an exception");
					e.printStackTrace();
				}
				mainFrame.add(frame);
				
				mainFrame.pack();
				mainFrame.setVisible(true);
			}
		});
	}
}
