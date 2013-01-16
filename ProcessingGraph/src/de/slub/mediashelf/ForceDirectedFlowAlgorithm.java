package de.slub.mediashelf;

import java.util.ArrayList;

/**
 * Simmple graph layout system
 * http://processingjs.nihongoresources.com/graphs
 * (c) Mike "Pomax" Kamermans 2011
 */

/**
 * Flow algorithm that positions nodes by 
 * prentending the links are elastic. This
 * is a multiple-step algorithm, and has 
 * to be run several times before it's "done".
 */
class ForceDirectedFlowAlgorithm implements FlowAlgorithm
{
	float min_size = 80.0f;
	float elasticity = 200.0f;
	void setElasticity(float e) { elasticity = e; }

	float repulsion = 4.0f;
	private ProcessingGraphController parent;
	void setRepulsion(float r) { repulsion = r; }

	// this is actually a simplified force
	// directed algorithm, taking into account
	// only incoming links.

	ForceDirectedFlowAlgorithm(ProcessingGraphController p)
	{
		parent = p;
	}

	public boolean reflow(DirectedGraph g)
	{
		ArrayList<Node> nodes = g.getNodes();
		int reset = 0;
		for(Node n: nodes)
		{
			ArrayList<Node> incoming = n.getIncomingLinks();
			ArrayList<Node> outgoing = n.getOutgoingLinks();
			// compute the total push force acting on this node
			int dx = 0;
			int dy = 0;
			for(Node ni: incoming) {
				dx += (ni.x-n.x);
				dy += (ni.y-n.y); }
			float len = ProcessingGraphController.sqrt(dx*dx + dy*dy);
			float angle = parent.getDirection(dx, dy);
			int[] motion = parent.rotateCoordinate(0.9f*repulsion,0.0f,angle);
			// move node
			int px = n.x;
			int py = n.y;
			n.x += motion[0];
			n.y += motion[1];
			if(n.x<0) { n.x=0; } else if(n.x>parent.width) { n.x=parent.width; }
			if(n.y<0) { n.y=0; } else if(n.y>parent.height) { n.y=parent.height; }
			// undo repositioning if elasticity is violated
			float shortest = n.getShortestLinkLength();
			if(shortest<min_size || shortest>elasticity*2) {
				reset++;
				n.x=px; n.y=py; }
		}
		return reset==nodes.size();
	}
}