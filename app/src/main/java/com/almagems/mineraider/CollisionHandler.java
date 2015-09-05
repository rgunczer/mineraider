package com.almagems.mineraider;

import org.jbox2d.callbacks.*;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

public class CollisionHandler implements ContactListener {

    private Game game;

    public CollisionHandler(Game game) {
        this.game = game;
    }

	@Override
	public void beginContact(Contact arg0) {
		Body bodyA = arg0.getFixtureA().getBody();
		Body bodyB = arg0.getFixtureB().getBody();

		if ( ( (bodyA.m_userData == game.cart1) &&
			   (bodyB.m_userData == game.cart2) ) ||
			   
			   (bodyB.m_userData == game.cart1) &&
			   (bodyA.m_userData == game.cart2) ) {

		//	System.out.println("begin contact...");

			Vec2 pos1 = game.cart1.body.getPosition();
			Vec2 pos2 = game.cart2.body.getPosition();
			
			if (pos1.x < pos2.x) {
				game.cart1.onHitOtherCart();
			} else {
				game.cart2.onHitOtherCart();
			}

            /*
			WorldManifold worldManifold = new WorldManifold();
			arg0.getWorldManifold(worldManifold);
								
			for (Vec2 pos : worldManifold.points) {						
				//particleManager.addParticleEmitterAt(pos.x, pos.y, 0);
				break;
//				if (pos.x == 0.0f && pos.y == 0.0f) {
//					System.out.println("Our guy!");
//				}
				
			}
			*/
		}
	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		//System.out.println("end contact...");
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}
}
