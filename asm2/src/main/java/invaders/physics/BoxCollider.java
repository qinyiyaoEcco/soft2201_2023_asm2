package invaders.physics;

public class BoxCollider implements Collider {

    private double width;
    private double height;
    private Vector2D position;

    public BoxCollider(double width, double height, Vector2D position){
        this.height = height;
        this.width = width;
        this.position = position;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition(){
        return this.position;
    }


    public void setPosition(Vector2D newPosition) {
        this.position = newPosition;
    }

    public boolean collidesWithBottomBoundary(int screenBottomBoundary) {
        // Assuming that the collider's position is its top-left corner and height is its height
        return this.position.getY() + this.height>= screenBottomBoundary;
    }
}
