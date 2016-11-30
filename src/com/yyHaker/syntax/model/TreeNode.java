package com.yyHaker.syntax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeNode
 *存储节点数据
 * @author Le Yuan
 * @date 2016/10/31
 */
public class TreeNode {
    private String name;   //节点名称
    private List<TreeNode> subNodes; //子节点集合

    public TreeNode(String name) {
        this.name = name;
        this.subNodes=new ArrayList<>();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(List<TreeNode> subNodes) {
        this.subNodes = subNodes;
    }
}
