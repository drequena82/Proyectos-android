package com.libgdxexamples.samples.graphics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.libgdxexamples.android.utils.Constants;

public class Border extends Actor
{
    private World world;
    private Body bodyEdgeScreen;
    private float factor;
    
    public Border(World world,float factor, float width, float heigth, int position)
    {
	this.world = world;
	this.factor = factor;
	
	this.generateBorder(position, width, heigth);
    }

    public void generateBorder(int position, float width, float heigth)
    {
	float vx1 = 0;
	float vy1 = 0;

	float vx2 = 0;
	float vy2 = 0;

	// Now the physics body of the bottom edge of the screen
	BodyDef bodyDefBorder = new BodyDef();
	bodyDefBorder.type = BodyDef.BodyType.StaticBody;

	bodyDefBorder.position.set(0, 0);
	FixtureDef fixtureDefBorder = new FixtureDef();

	// Se trata de un vector, una linea de un punto x,y a otro punto x,y
	EdgeShape edgeShape = new EdgeShape();

	switch (position)
	{
	case Constants.BORDER_BOTTOM:
	    vx1 = -width / 2;
	    vx2 = width / 2;
	    vy1 = -heigth / 2 + factor;
	    vy2 = -heigth / 2 + factor;
	    break;
	case Constants.BORDER_LEFT:
	    vx1 = -width / 2 + factor;
	    vx2 = -width / 2 + factor;
	    vy1 = -heigth / 2;
	    vy2 = heigth / 2;
	    break;
	case Constants.BORDER_RIGHT:
	    vx1 = width / 2 - factor;
	    vx2 = width / 2 - factor;
	    vy1 = -heigth / 2;
	    vy2 = heigth / 2;
	    break;
	case Constants.BORDER_TOP:
	    vx1 = -width / 2;
	    vx2 = width / 2;
	    vy1 = heigth / 2 - factor;
	    vy2 = heigth / 2 - factor;
	    break;
	default:
	    vx1 = -width / 2;
	    vx2 = width / 2;
	    vy1 = -heigth / 2;
	    vy2 = -heigth / 2;
	}

	edgeShape.set(vx1, vy1, vx2, vy2);
	fixtureDefBorder.shape = edgeShape;
	
	bodyEdgeScreen = world.createBody(bodyDefBorder);
	bodyEdgeScreen.createFixture(fixtureDefBorder);
	edgeShape.dispose();
    }

    public Body getBodyEdgeScreen()
    {
        return bodyEdgeScreen;
    }

    public void setBodyEdgeScreen(Body bodyEdgeScreen)
    {
        this.bodyEdgeScreen = bodyEdgeScreen;
    }

}
