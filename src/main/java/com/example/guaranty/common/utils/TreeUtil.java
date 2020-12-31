package com.example.guaranty.common.utils;


import com.example.guaranty.entity.TreeNode;
import com.example.guaranty.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造树形菜单
 *
 * @author ming
 * @date 2019-03-15 08:45
 **/
public class TreeUtil {

    public static <T> List<TreeNode<T>> build(List<TreeNode<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<TreeNode<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            Integer pid = node.getPid();
            if (pid == 0) {
                topNodes.add(node);
                return;
            }
            for (TreeNode<T> n : nodes) {
                Integer id = n.getMenuId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        });
        return topNodes;
    }
}
