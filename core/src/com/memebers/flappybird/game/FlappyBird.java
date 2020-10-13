package com.memebers.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import static com.badlogic.gdx.math.Interpolation.circle;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;

	// Init.
	Texture background;
	Texture bottomTube;
	Texture topTube;
	Texture[] birds;
	Texture gameOver;
	BitmapFont font;

	// Init Values.
	int flapState=0;
	int gameState=0;
	int numberOfTubes=4;
	int score=0;
	int scoringTube=0;

	float velocity=0;
	float birdY;
	float gravity=2;
	float gap=400;
	float maxTubeOffset=0;
	float tubeVelocity=4;

	float[] tubeOffset= new float[numberOfTubes];
	float[] tubeX= new float[numberOfTubes];

	float distantBtwTubes;

	Random randomGenerator;
	Circle birdCircle;

	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		// ShapeRender to the Objects.
		birdCircle= new Circle();
		topTubeRectangle= new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		// Adding Images.
		background= new Texture("bg.png");
		birds= new Texture[2];
		birds[0]= new Texture("bird.png");
		birds[1]= new Texture("bird2.png");
		bottomTube= new Texture("bottomtube.png");
		topTube= new Texture("toptube.png");
		gameOver= new Texture("gameover.png");

		// Setting Pipes.
		maxTubeOffset= Gdx.graphics.getHeight() / 2 - gap /2 - 100;
		distantBtwTubes=Gdx.graphics.getWidth() * 3/4;
		randomGenerator= new Random();

		font= new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		// Calling Functions.
		startGame();
	}

	public void startGame(){

		// Setting Bird Position
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() /2;

		for (int i=0; i < numberOfTubes; i++){

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (maxTubeOffset);

			tubeX[i] =Gdx.graphics.getWidth() / 2 - topTube.getWidth() /2 + Gdx.graphics.getWidth() +  i * distantBtwTubes;

			topTubeRectangle[i]= new Rectangle();
			bottomTubeRectangle[i]= new Rectangle();
		}
	}

	@Override
	public void render () {
		// This Function Keep calling itself. Again and Again.


		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// If game Starts.
			if (gameState == 1) {

				if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){

					// increment Score.
					score++;

					Gdx.app.log("Score",String.valueOf(score));

					if (scoringTube < numberOfTubes -1){

						scoringTube++;

					}else {

						scoringTube=0;
					}
				}

				if (Gdx.input.justTouched()){

					velocity=-30;
				}
				for (int i=0; i < numberOfTubes; i++) {

					if (tubeX[i] < - topTube.getWidth()) {

						tubeX[i] += numberOfTubes * distantBtwTubes;

						tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (maxTubeOffset);
					}
					else {

						tubeX[i] = tubeX[i] - tubeVelocity;
					}

					batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);

					batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

					topTubeRectangle[i]= new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

					bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
				}

				if (birdY > 0) {
					velocity = velocity + gravity;
					birdY -= velocity;
				}
				else {
					gameState=2;
				}

			} else
				if (gameState ==0){
					if (Gdx.input.justTouched()) {
					gameState = 1;
					}
		}    else
				if (gameState == 2){
					// If Game Over is over.

				batch.draw(gameOver,Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);

				if (Gdx.input.justTouched()) {
					gameState = 1;
					startGame();
					score=0;
					scoringTube=0;
					velocity=0;
				}

			}
			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		// have to work on Texture outlets and texture regions at GDx animations.

		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2,birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2 );

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		//This was just to check the collision of Rectangles and square */

		for (int i=0; i < numberOfTubes; i++){

			 // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			 // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle,topTubeRectangle[i]) || (Intersector.overlaps(birdCircle,bottomTubeRectangle[i]))) {

				// IF collision Occur's Game Ends Here.
				gameState=2;
			}
		}
			// shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
