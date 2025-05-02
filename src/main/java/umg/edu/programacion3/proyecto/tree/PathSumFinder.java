/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umg.edu.programacion3.proyecto.tree;

/**
 *
 * @author Fernando
 */
import java.util.*;

public class PathSumFinder {

    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        findPaths(root, targetSum, currentPath, result);
        return result;
    }

    private void findPaths(TreeNode node, int remainingSum, List<Integer> currentPath, List<List<Integer>> result) {
        if (node == null) return;

        currentPath.add(node.val);

        // Si es hoja y cumple la suma
        if (node.left == null && node.right == null && remainingSum == node.val) {
            result.add(new ArrayList<>(currentPath)); // copia de la ruta
        } else {
            findPaths(node.left, remainingSum - node.val, currentPath, result);
            findPaths(node.right, remainingSum - node.val, currentPath, result);
        }

        currentPath.remove(currentPath.size() - 1); // backtrack
    }
}
