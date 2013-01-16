package de.slub.mediashelf;

import processing.core.PConstants;

/**
 * Simmple graph layout system
 * http://processingjs.nihongoresources.com/graphs
 * (c) Mike "Pomax" Kamermans 2011
 */

/**
 * Flow algorithm for drawing nodes in a circle
 */
class CircleFlowAlgorithm implements FlowAlgorithm
{
	private ProcessingGraphController parent;

	// draw all nodes in a big circle,
	// without trying to find the best
	// arrangement possible.

	public CircleFlowAlgorithm(ProcessingGraphController p)
	{
		parent = p;
	}

	public boolean reflow(DirectedGraph g)
	{
		float interval = 2*PConstants.PI / (float)g.size();
		int cx = parent.width/2;
		int cy = parent.height/2;
		float vl = cx - (2*g.getNode(0).r1) - 10;
		for(int a=0; a<g.size(); a++)
		{
			int[] nc = parent.rotateCoordinate(vl, 0, (float)a*interval);
			g.getNode(a).x = cx+nc[0];
			g.getNode(a).y = cy+nc[1];
		}
		return true;
	}
}