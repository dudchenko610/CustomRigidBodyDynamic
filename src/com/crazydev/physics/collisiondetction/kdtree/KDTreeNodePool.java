package com.crazydev.physics.collisiondetction.kdtree;

import java.util.ArrayList;

import com.crazydev.physics.collisiondetction.TreeNode;

public class KDTreeNodePool {
	
	private ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
	private int index = 0;
	
	public KDTreeNodePool() {
		for (int i = 0; i < 500; i ++) {
			this.nodes.add(new TreeNode());
		}
	}
	
	public TreeNode getKDTreeNode() {
		TreeNode node = this.nodes.get(index ++);
		node.parent = null;
		node.left   = null;
		node.right  = null;
		node.isChecked = false;
		
		return node;
	}
	
	public void reset() {
		this.index = 0;
	}
	
}
