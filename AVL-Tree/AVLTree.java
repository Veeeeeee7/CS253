// THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
// A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Victor Li

// AVLTree.java: this should be an AVL search tree.
// For homework, you only edit this file.
// Remember to add your name to the SPCA statement above.

// An AVLTree is a BST (binary search tree) which also maintains the
// "height balance condition": for every node in the tree, its two
// child subtrees have heights within one of each other.
//
// AVLTree inherits most of its methods from BST.  The only BST method
// you really need to change is fixup().  In BST, you can see fixup(t)
// is called on every node t along the path up from a modification
// point (insertion or removal) back up to the root.  In BST the
// fixup(t) method simply calls t.update(), and returns t.
// Method t.update() recomputes t.size and t.height.
//
// But in AVLTree, fixup(t) should also do one step of the "trinode
// restructuring" (one or two rotations) necessary to restore the
// balance condition.  It returns the root of the restructured
// subtree, which may be different from t.
//
// It is up to the callers of fixup(t) to call it on the way back up
// the tree after a modification, but this is already done in the BST
// insert and remove methods, so you don't need to override those.
//
// In fixup(t), you may use rotateRight, provided below.  You'll
// probably also need rotateLeft, which you can write yourself.

public class AVLTree<K extends Comparable<K>> extends BST<K> {
    // We inherit "root" field from BST.
    // All the public BST methods should work without modification.
    // We only need to revise this one method, to do the rebalancing.

    protected Node fixup(Node t) {
        t.update(); // update t.size and t.height

        int bal = height(t.left) - height(t.right);
        if (-1 <= bal && bal <= +1)
            // got lucky: balanced already, nothing to do!
            return t;

        // TODO: restore balance condition using an appropriate
        // trinode restructuring (either one or two rotations), as
        // illustrated in the slides.

        // In those illustrations, this unhappy node t is "z", t's
        // taller child is "y", and y's taller child is "x". In case
        // of a tie, where both children of y have the same height,
        // choose x so that you only have to do one rotation.

        // Left subtree is taller than right subtree
        if (bal > 1) {
            Node y = t.left;
            // Left-Left case: perform a single right rotation
            if (y.right == null || height(y.left) >= height(y.right)) {
                t = rotateRight(t);
            }
            // Left-Right case: perform a left rotation on the left child, then a right
            // rotation on the root
            else if (y.left == null || height(y.right) > height(y.left)) {
                t.left = rotateLeft(y);
                t = rotateRight(t);
            }
        }
        // Right subtree is taller than left subtree
        else if (bal < -1) {
            Node y = t.right;
            // Right-Right case: perform a single left rotation
            if (y.left == null || height(y.right) >= height(y.left)) {
                t = rotateLeft(t);
            }
            // Right-Left case: perform a right rotation on the right child, then a left
            // rotation on the root
            else if (y.right == null || height(y.left) > height(y.right)) {
                t.right = rotateRight(y);
                t = rotateLeft(t);
            }
        }

        // return the new root of this subtree (might not be t!)
        return t;
    }

    // rotateRight does a rotation: the left child becomes the new
    // root of this subtree, the old root becomes its right child.
    Node rotateRight(Node k2) {
        Node k1 = k2.left;
        k2.left = k1.right;
        k2.update(); // update size and height
        k1.right = k2;
        k1.update(); // update size and height
        return k1; // return new root of this subtreee
    }

    // rotateLeft does a left rotation: the right child becomes the new root of this
    // subtree and the old root becomes its left child
    Node rotateLeft(Node k1) {
        Node k2 = k1.right;
        k1.right = k2.left;
        k1.update();
        k2.left = k1;
        k2.update();
        return k2;
    }
}
