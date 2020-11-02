package visualizer;

import logic.Piece;
import logic.Piece.PieceType;
import logic.Sprite;
import logic.World;

import java.util.ArrayList;

// This class has more high level interface for drawing the game
// It mostly uses the low level functions provided by the Renderer class
public class RenderingHelper {

    private final Renderer renderer;
    private final float stageWidth = 0.9f;
    private final float stageHeight = 1.8f;

    public RenderingHelper(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawWorld(World world) {
        // Reset
        renderer.reset();
        // Draw
        drawStage(world.getPlacedSquares());
        drawSprites(world.getSprites());
        // Swap color buffers
        renderer.draw();
    }

    private void drawStage(int[][] placedSquares) {

        // Black rectangle
        renderer.drawRectangle(
                -stageWidth/2,
                1,
                stageWidth,
                stageHeight + 1f-stageHeight/2,
                new float[]{0f, 0f, 0f, 1f}
        );

        // Draw placed squares
        for(int i = 0; i < placedSquares.length; i++) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                if(placedSquares[i][j] == 1) {
                    renderer.drawRectangle(
                            -stageWidth/2+stageWidth/World.WORLD_WIDTH*j,
                            -stageHeight/2+stageHeight/World.WORLD_HEIGHT*(i+1),
                            stageWidth/World.WORLD_WIDTH,
                            stageHeight/World.WORLD_HEIGHT,
                            new float[]{1f, 1f, 1f, 1f}
                    );
                }
            }
        }
    }

    private void drawSprites(ArrayList<Sprite> sprites) {

        for(Sprite sprite : sprites) {
            if(sprite instanceof Piece) {
                Piece piece = (Piece) sprite;
                PieceType pieceType = piece.getPieceType();
                int[][] pieceShape = piece.getPieceShape();
                // Center distance is 1 except in the O piece where it is 0
                int centerDistance = 1;
                if(pieceShape.length == 2) {
                    centerDistance = 0;
                }
                for(int i = 0; i < pieceShape.length; i++) {
                    for(int j = 0; j < pieceShape[0].length; j++) {
                        if(pieceShape[i][j] != 0) {
                            int x = sprite.getxPos() + (j - centerDistance) * World.GRID_SIZE;
                            int y = sprite.getyPos() + ((pieceShape.length - i) - centerDistance) * World.GRID_SIZE;
                            float[] coords = convertSpriteCoords(x, y);
                            renderer.drawRectangle(
                                    coords[0],
                                    coords[1],
                                    stageWidth/World.WORLD_WIDTH,
                                    stageHeight/World.WORLD_HEIGHT,
                                    new float[]{1f, 0f, 0f, 1f}
                            );
                        }
                    }
                }
            }
        }
    }

    private float[] convertSpriteCoords(int x, int y) {
        return new float[] {
                -stageWidth / 2f + (stageWidth / World.WORLD_WIDTH * ((float) x / World.GRID_SIZE)),
                -stageHeight / 2f + (stageHeight / World.WORLD_HEIGHT * ((float) y / World.GRID_SIZE))
        };
    }
}