package com.crazydev.physics.collisiondetction;

import java.util.ArrayList;

import com.crazydev.math.Rectangle;
import com.crazydev.physics.Body;

public class TreeNode {
	
	public boolean isChecked = false;
	public int level = 0;
	
	public TreeNode parent = null;
	public TreeNode left   = null;
	public TreeNode right  = null;
	
	public ArrayList<Body> nodeBodies = new ArrayList<Body>();
	public Rectangle area = new Rectangle();
	
	
}
