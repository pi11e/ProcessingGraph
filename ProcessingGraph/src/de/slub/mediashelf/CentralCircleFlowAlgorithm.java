package de.slub.mediashelf;

import processing.core.PConstants;


/**
 * Flow algorithm for drawing nodes in a circle with one central node
 */
class CentralCircleFlowAlgorithm implements FlowAlgorithm
{
	private ProcessingGraphController parent;

	// Works just like the CircleFlowAlgorithm with the exception that
	// the first node of a graph should be in the center of the computed node

	public CentralCircleFlowAlgorithm(ProcessingGraphController p)
	{
		parent = p;
	}

	public boolean reflow(DirectedGraph g)
	{
		float interval = 2*PConstants.PI / (float)g.size();
		
		
		int cx = parent.width/2;
		int cy = parent.height/2;
		
		float vl = cx - (2*g.getNode(0).r1) - 10;
		for(int a=1; a<g.size(); a++)
		{
			int[] nc = parent.rotateCoordinate(vl, 0, (float)a*interval);
			g.getNode(a).x = cx+nc[0]/2;
			g.getNode(a).y = cy+nc[1]/2;
		}
		return true;
	}
}