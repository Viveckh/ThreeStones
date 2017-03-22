package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/3/2017
*/

/* This class serves as a pouch in the game Board. It keeps a track of
* A) X-Y Coordinate of the pouch in the board
* B) Filled/Unfilled state of the pouch, stone contained (n(none), w(white), b(black), c(clear))
* C) Its surrounding pouches - left, right, top, bottom, topLeft, topRight, bottomLeft, bottomRight.
*/

public class Block {

    // Variables
    private int xAxis, yAxis;
    private boolean filled;
    private char stone;    // w for white, b for black, c for clear, n for none

    private Block left, right;
    private Block top, bottom;
    private Block topLeft, topRight, bottomLeft, bottomRight;

    // Constructor
    // assigns the passed coordinates, sets filled state to false.
    // Sets the default stone to 'n' i.e. none
    public Block(int xAxis, int yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        filled = false;
        stone = 'n';
        left = null;
        right = null;
        top = null;
        bottom = null;
        topLeft = null;
        topRight = null;
        bottomLeft = null;
        bottomRight = null;
    }

    //Co-ordinate getters
    public int getX() { return xAxis; }
    public int getY() { return yAxis; }

    //Getting/Setting Filled/Unfilled state and stone
    public boolean isFilled() { return filled == true; }
    public char getStone() { return stone; }
    public void setFilled() { filled = true; }
    public void setStone(char stone) { this.stone = stone; }

    //Setters for establishing relationship with surrounding pouches
    public void setLeft(Block x) { left = x; }
    public void setRight(Block x) { right = x; }
    public void setTop(Block x) { top = x; }
    public void setBottom(Block x) { bottom = x; }
    public void setTopLeft(Block x) { topLeft = x; }
    public void setTopRight(Block x) { topRight = x; }
    public void setBottomLeft(Block x) { bottomLeft = x; }
    public void setBottomRight (Block x) { bottomRight = x; }

    // Getters for the surrounding pouches
    public Block getLeft() { return left;}
    public Block getRight() { return right; }
    public Block getTop() { return top; }
    public Block getBottom() { return bottom; }
    public Block getTopLeft() { return topLeft; }
    public Block getTopRight() { return topRight; }
    public Block getBottomLeft() { return bottomLeft; }
    public Block getBottomRight () { return bottomRight; }

    //Checks whether the left is favorable. Does the calculation based on two situations.
    // 1) What if my Stone is clear?
    // 2) What if my stone is colored (black/white)?
    boolean isLeftFavorable() {
        if (left != null) {
            if (left.getStone()!= 'n') {
                if (this.getStone() == 'c') {
                    if (right != null && right.getStone() != 'n') {
                        if ((left.getStone() == right.getStone()) && (left.getStone() != 'c' && right.getStone() != 'c')) {
                            return true;
                        }
                        if ((left.getStone() != right.getStone()) && (left.getStone() == 'c' || right.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (left.left != null && left.left.getStone() != 'n') {
                        if ((left.getStone() == left.left.getStone()) && (left.getStone() != 'c' && left.left.getStone() != 'c')) {
                            return true;
                        }
                        if ((left.getStone() != left.left.getStone()) && (left.getStone() == 'c' || left.left.getStone() == 'c')) {
                            return true;
                        }
                    }
                }
                else {
                    if ((left.getStone() == stone) || left.getStone() == 'c') return true;
                }
            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isRightFavorable() {
        if (right != null) {
            if (right.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (left != null && left.getStone() != 'n') {
                        if ((left.getStone() == right.getStone()) && (left.getStone() != 'c' && right.getStone() != 'c')) {
                            return true;
                        }
                        if ((left.getStone() != right.getStone()) && (left.getStone() == 'c' || right.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (right.right != null && right.right.getStone() != 'n') {
                        if ((right.right.getStone() == right.getStone()) && (right.right.getStone() != 'c' && right.getStone() != 'c')) {
                            return true;
                        }
                        if ((right.right.getStone() != right.getStone()) && (right.right.getStone() == 'c' || right.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((right.getStone() == stone) || right.getStone() == 'c') return true;
                }

            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isTopFavorable() {
        if (top != null) {
            if (top.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (bottom != null && bottom.getStone() != 'n') {
                        if ((top.getStone() == bottom.getStone()) && (top.getStone() != 'c' && bottom.getStone() != 'c')) {
                            return true;
                        }
                        if ((top.getStone() != bottom.getStone()) && (top.getStone() == 'c' || bottom.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (top.top != null && top.top.getStone() != 'n') {
                        if ((top.getStone() == top.top.getStone()) && (top.getStone() != 'c' && top.top.getStone() != 'c')) {
                            return true;
                        }
                        if ((top.getStone() != top.top.getStone()) && (top.getStone() == 'c' || top.top.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((top.getStone() == stone) || top.getStone() == 'c') return true;
                }
            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isBottomFavorable() {
        if (bottom != null) {
            if (bottom.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (top != null && top.getStone() != 'n') {
                        if ((top.getStone() == bottom.getStone()) && (top.getStone() != 'c' && bottom.getStone() != 'c')) {
                            return true;
                        }
                        if ((top.getStone() != bottom.getStone()) && (top.getStone() == 'c' || bottom.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (bottom.bottom != null && bottom.bottom.getStone() != 'n') {
                        if ((bottom.bottom.getStone() == bottom.getStone()) && (bottom.bottom.getStone() != 'c' && bottom.getStone() != 'c')) {
                            return true;
                        }
                        if ((bottom.bottom.getStone() != bottom.getStone()) && (bottom.bottom.getStone() == 'c' || bottom.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((bottom.getStone() == stone) || bottom.getStone() == 'c') return true;
                }
            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isTopLeftFavorable() {
        if (topLeft != null) {
            if (topLeft.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (bottomRight != null && bottomRight.getStone() != 'n') {
                        if ((topLeft.getStone() == bottomRight.getStone()) && (topLeft.getStone() != 'c' && bottomRight.getStone() != 'c')) {
                            return true;
                        }
                        if ((topLeft.getStone() != bottomRight.getStone()) && (topLeft.getStone() == 'c' || bottomRight.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (topLeft.topLeft != null && topLeft.topLeft.getStone() != 'n') {
                        if ((topLeft.getStone() == topLeft.topLeft.getStone()) && (topLeft.getStone() != 'c' && topLeft.topLeft.getStone() != 'c')) {
                            return true;
                        }
                        if ((topLeft.getStone() != topLeft.topLeft.getStone()) && (topLeft.getStone() == 'c' || topLeft.topLeft.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((topLeft.getStone() == stone) || topLeft.getStone() == 'c') return true;
                }
            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isTopRightFavorable() {
        if (topRight != null) {
            if (topRight.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (bottomLeft != null && bottomLeft.getStone() != 'n') {
                        if ((topRight.getStone() == bottomLeft.getStone()) && (topRight.getStone() != 'c' && bottomLeft.getStone() != 'c')) {
                            return true;
                        }
                        if ((topRight.getStone() != bottomLeft.getStone()) && (topRight.getStone() == 'c' || bottomLeft.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (topRight.topRight != null && topRight.topRight.getStone() != 'n') {
                        if ((topRight.getStone() == topRight.topRight.getStone()) && (topRight.getStone() != 'c' && topRight.topRight.getStone() != 'c')) {
                            return true;
                        }
                        if ((topRight.getStone() != topRight.topRight.getStone()) && (topRight.getStone() == 'c' || topRight.topRight.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((topRight.getStone() == stone) || topRight.getStone() == 'c') return true;
                }
            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isBottomLeftFavorable() {
        if (bottomLeft != null) {
            if (bottomLeft.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (topRight != null && topRight.getStone() != 'n') {
                        if ((topRight.getStone() == bottomLeft.getStone()) && (topRight.getStone() != 'c' && bottomLeft.getStone() != 'c')) {
                            return true;
                        }
                        if ((topRight.getStone() != bottomLeft.getStone()) && (topRight.getStone() == 'c' || bottomLeft.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (bottomLeft.bottomLeft != null && bottomLeft.bottomLeft.getStone() != 'n') {
                        if ((bottomLeft.bottomLeft.getStone() == bottomLeft.getStone()) && (bottomLeft.bottomLeft.getStone() != 'c' && bottomLeft.getStone() != 'c')) {
                            return true;
                        }
                        if ((bottomLeft.bottomLeft.getStone() != bottomLeft.getStone()) && (bottomLeft.bottomLeft.getStone() == 'c' || bottomLeft.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((bottomLeft.getStone() == stone) || bottomLeft.getStone() == 'c')
                        return true;
                }
            }
        }
        return false;
    }

    //Refer to the description of isLeftFavorable
    public boolean isBottomRightFavorable() {
        if (bottomRight != null) {
            if (bottomRight.getStone() != 'n') {
                if (this.getStone() == 'c') {
                    if (topLeft != null && topLeft.getStone() != 'n') {
                        if ((topLeft.getStone() == bottomRight.getStone()) && (topLeft.getStone() != 'c' && bottomRight.getStone() != 'c')) {
                            return true;
                        }
                        if ((topLeft.getStone() != bottomRight.getStone()) && (topLeft.getStone() == 'c' || bottomRight.getStone() == 'c')) {
                            return true;
                        }
                    }
                    if (bottomRight.bottomRight != null && bottomRight.bottomRight.getStone() != 'n') {
                        if ((bottomRight.bottomRight.getStone() == bottomRight.getStone()) && (bottomRight.bottomRight.getStone() != 'c' && bottomRight.getStone() != 'c')) {
                            return true;
                        }
                        if ((bottomRight.bottomRight.getStone() != bottomRight.getStone()) && (bottomRight.bottomRight.getStone() == 'c' || bottomRight.getStone() == 'c')) {
                            return true;
                        }
                    }
                } else {
                    if ((bottomRight.getStone() == stone) || bottomRight.getStone() == 'c')
                        return true;
                }
            }
        }
        return false;
    }
}
