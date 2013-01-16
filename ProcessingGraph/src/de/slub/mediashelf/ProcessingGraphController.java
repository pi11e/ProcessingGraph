package de.slub.mediashelf;

import java.util.ArrayList;

import processing.core.*;




@SuppressWarnings("serial")
public class ProcessingGraphController extends PApplet 
{
	int sizeX = 1024;
	int sizeY = 768;
	
	DirectedGraph g = null;
	int padding=30;

	public void setup()
	{
		size(sizeX,sizeY);
		frameRate(24);
		noLoop();
	}

	public void draw()
	{
		background(255);
		if(g==null)
		{
			fill(0);
			String s = "Click on this canvas, and pick one:";
			float tw = textWidth(s);
			text(s, (width-tw)/2, height/2 - 50);

			s = "Press \"t\" for a tree,";
			tw = textWidth(s);
			text(s, (width-tw)/2, height/2 - 20);

			s = "\"c\" for a circle graph,";
			tw = textWidth(s);
			text(s, (width-tw)/2, height/2);

			s = "and \"f\" for an FD graph";
			tw = textWidth(s);
			text(s, (width-tw)/2, height/2 + 20);
		}
		else
		{
			boolean done = g.reflow();
			g.draw();
			if(!done) { loop(); } else { noLoop(); }
		}
	}

	public void keyPressed()
	{
		// tree
		if(key=='t' || key==116) {
			makeTree();
			redraw(); }
		// circular graph
		else if(key=='c' || key==99) {
			makeGraph();
			g.setFlowAlgorithm(new CentralCircleFlowAlgorithm(this));
			redraw();  }
		// force-directed graph
		else if(key=='f' || key==102) {
			makeGraph();
			g.setFlowAlgorithm(new ForceDirectedFlowAlgorithm(this));
			redraw(); }
	}


	void makeGraph()
	{
		g = new DirectedGraph(this);
		
		// nodes (constructor is parent, label, x, y):
		Node start = new Node(this, "Start", width/2, height/2);
		g.addNode(start);
		
		String[] firstLevelLabels = new String[]{"Saxonica", "Handschriften", "Musik", "Technikgeschichte", "Extras"};
		Node[] firstLevelNodes = new Node[firstLevelLabels.length];
		for(int i = 0; i < firstLevelLabels.length; i++)
		{
			Node n = new Node(this, firstLevelLabels[i], width/2, height/2);
			g.addNode(n);
			// link all first level nodes to start node
			g.linkNodes(start, n);
			firstLevelNodes[i] = n;
		}
		

		for(Node firstLevelNode : firstLevelNodes)
		{
			// add 10 nodes to each first level node
			for(int j = 0; j < 10; j++)
			{
				Node secondLevelNode = new Node(this, String.valueOf(j), padding, padding);
				g.addNode(secondLevelNode);
				g.linkNodes(firstLevelNode, secondLevelNode);
			}	
		}
		
		
		
		
		// original code:
//		// define a graph
//		g = new DirectedGraph(this);
//
//		// define some nodes
//		Node n1 = new Node(this, "1",padding,padding);
//		Node n2 = new Node(this, "2",padding,height-padding);
//		Node n3 = new Node(this, "3",width-padding,height-padding);
//		Node n4 = new Node(this, "4",width-padding,padding);
//		Node n5 = new Node(this, "5",width-3*padding,height-2*padding);
//		Node n6 = new Node(this, "6",width-3*padding,2*padding);
//
//		// add nodes to graph
//		g.addNode(n1);
//		g.addNode(n2);
//		g.addNode(n3);
//		g.addNode(n4);
//		g.addNode(n5);
//		g.addNode(n6);
//
//		// link nodes
//		g.linkNodes(n1,n2);
//		g.linkNodes(n2,n3);
//		g.linkNodes(n3,n4);
//		g.linkNodes(n4,n1);
//		g.linkNodes(n1,n3);
//		g.linkNodes(n2,n4);
//		g.linkNodes(n5,n6);
//		g.linkNodes(n1,n6);
//		g.linkNodes(n2,n5);
	}

	void makeTree()
	{
		// define a root node
		Node root = new Node(this, "root",0,0);

		// define a Tree
		g = new Tree(this, root);

		// technically g is of type DirectedGraph, so build a Tree alias:
		Tree t = (Tree) g;

		// define some children
		Node ca = new Node(this, "a",0,0);
		Node caa = new Node(this, "aa",0,0);
		Node cab = new Node(this, "ab",0,0);
		Node cb = new Node(this, "b",0,0);
		Node cba = new Node(this, "ba",0,0);
		Node cbb = new Node(this, "bb",0,0);
		Node cbba = new Node(this, "bba",0,0);
		Node cbbb = new Node(this, "bbb",0,0);
		Node cbbba = new Node(this, "bbba",0,0);
		Node cbbbb = new Node(this, "bbbb",0,0);

		// add all nodes to tree
		t.addChild(root, ca);
		t.addChild(root, cb);
		t.addChild(ca, caa);
		t.addChild(ca, cab);
		t.addChild(cb, cba);
		t.addChild(cb, cbb);
		t.addChild(cbb, cbba);
		t.addChild(cbb, cbbb);
		t.addChild(cbbb, cbbba);
		t.addChild(cbbb, cbbbb);
	}

	/**
	 * Simmple graph layout system
	 * http://processingjs.nihongoresources.com/graphs
	 * (c) Mike "Pomax" Kamermans 2011
	 */

	// =============================================
	//	      Some universal helper functions
	// =============================================

	// universal helper function: get the angle (in radians) for a particular dx/dy
	float getDirection(double dx, double dy) {
		// quadrant offsets
		double d1 = 0.0;
		double d2 = PI/2.0;
		double d3 = PI;
		double d4 = 3.0*PI/2.0;
		// compute angle based on dx and dy values
		double angle = 0;
		float adx = abs((float)dx);
		float ady = abs((float)dy);
		// Vertical lines are one of two angles
		if(dx==0) { angle = (dy>=0? d2 : d4); }
		// Horizontal lines are also one of two angles
		else if(dy==0) { angle = (dx>=0? d1 : d3); }
		// The rest requires trigonometry (note: two use dx/dy and two use dy/dx!)
		else if(dx>0 && dy>0) { angle = d1 + atan(ady/adx); }		// direction: X+, Y+
		else if(dx<0 && dy>0) { angle = d2 + atan(adx/ady); }		// direction: X-, Y+
		else if(dx<0 && dy<0) { angle = d3 + atan(ady/adx); }		// direction: X-, Y-
		else if(dx>0 && dy<0) { angle = d4 + atan(adx/ady); }		// direction: X+, Y-
		// return directionality in positive radians
		return (float)(angle + 2*PI)%(2*PI); }

	// universal helper function: rotate a coordinate over (0,0) by [angle] radians
	int[] rotateCoordinate(float x, float y, float angle) {
		int[] rc = {0,0};
		rc[0] = (int)(x*cos(angle) - y*sin(angle));
		rc[1] = (int)(x*sin(angle) + y*cos(angle));
		return rc; }

	// universal helper function for Processing.js - 1.1 does not support ArrayList.addAll yet
	void addAll(ArrayList<Node> a, ArrayList<Node> b) { for(Node o: b) { a.add(o); }}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "de.slub.mediashelf.ProcessingGraphController" });
	}
}
