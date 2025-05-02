/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umg.edu.programacion3.proyecto.tree;

import java.util.List;

/**
 *
 * @author Fernando
 */
public class Main {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.right = new TreeNode(8);

        root.left.left = new TreeNode(11);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);

        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.right.right.left = new TreeNode(5);
        root.right.right.right = new TreeNode(1);

        PathSumFinder finder = new PathSumFinder();
        List<List<Integer>> result = finder.pathSum(root, 22);

        System.out.println(result); // Output esperado: [[5, 4, 11, 2], [5, 8, 4, 5]]
    }
}

