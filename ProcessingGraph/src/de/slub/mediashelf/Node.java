package de.slub.mediashelf;

import java.util.ArrayList;

import processing.core.PApplet;



public class Node 
{
	private ProcessingGraphController parent;

	ArrayList<Node> inlinks = new ArrayList<Node>();
	ArrayList<Node> outlinks = new ArrayList<Node>();;
	String label;

	Node(ProcessingGraphController p, String _label, int _x, int _y) 
	{
		parent = p;
		label=_label; x=_x; y=_y; r1=5; r2=5; 
	}

	void addIncomingLink(Node n) {
		if(!inlinks.contains(n)) {
			inlinks.add(n);}}

	ArrayList<Node> getIncomingLinks(){
		return inlinks; }

	int getIncomingLinksCount() {
		return inlinks.size(); }

	void addOutgoingLink(Node n) {
		if(!outlinks.contains(n)) {
			outlinks.add(n);}}

	ArrayList<Node> getOutgoingLinks(){
		return outlinks; }

	int getOutgoingLinksCount() {
		return outlinks.size(); }

	float getShortestLinkLength() {
		if(inlinks.size()==0 && outlinks.size()==0) { return -1; }
		float l = 100;
		for(Node inode: inlinks) {
			int dx = inode.x-x;
			int dy = inode.y-y;
			float il = PApplet.sqrt(dx*dx + dy*dy);
			if(il<l) { l=il; }}
		for(Node onode: outlinks) {
			int dx = onode.x-x;
			int dy = onode.y-y;
			float ol = PApplet.sqrt(dx*dx + dy*dy);
			if(ol<l) { l=ol; }}
		return l; }

	boolean equals(Node other) {
		if(this==other) return true;
		return label.equals(other.label); }

	// visualisation-specific
	int x=0;
	int y=0;
	int r1=10;
	int r2=10;

	void setPosition(int _x, int _y) {
		x=_x; y=_y; }

	void setRadii(int _r1, int _r2) {
		r1=_r1; r2=_r2; }

	void draw() {
		parent.stroke(0);
		parent.fill(255);
		for(Node o: outlinks) {
			drawArrow(x,y,o.x,o.y); }
		parent.ellipse(x,y,r1*2,r2*2);
		parent.fill(50,50,255);
		parent.text(label,x+r1*2,y+r2*2);
	}

	int[] arrowhead = {0,-4,0,4,7,0};
	void drawArrow(int x, int y, int ox, int oy)
	{
		int dx=ox-x;
		int dy=oy-y;
		float angle = parent.getDirection(dx,dy);
		float vl = (float) (PApplet.sqrt(dx*dx+dy*dy) - PApplet.sqrt(r1*r1+r2*r2)*1.5);
		int[] end = parent.rotateCoordinate(vl, 0, angle);
		parent.line(x,y,x+end[0],y+end[1]);
		drawArrowHead(x+end[0], y+end[1], angle);
	}
	void drawArrowHead(int ox, int oy, float angle) {
		int[] rc1 = parent.rotateCoordinate(arrowhead[0], arrowhead[1], angle);
		int[] rc2 = parent.rotateCoordinate(arrowhead[2], arrowhead[3], angle);
		int[] rc3 = parent.rotateCoordinate(arrowhead[4], arrowhead[5], angle);
		int[] narrow = {ox+ rc1[0], oy+ rc1[1], ox+ rc2[0], oy+ rc2[1], ox+ rc3[0], oy+ rc3[1]};
		parent.stroke(0);
		parent.fill(255);
		parent.triangle(narrow[0], narrow[1], narrow[2], narrow[3], narrow[4], narrow[5]);
	}
}
