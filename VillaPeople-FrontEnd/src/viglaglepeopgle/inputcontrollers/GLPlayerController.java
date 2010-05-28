package viglaglepeopgle.inputcontrollers;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import viglaglepeopgle.display.Scene;
import villagepeople.entities.Player;

public class GLPlayerController extends GLController {
		
	public GLPlayerController(Player player, Scene scene) {
		super(player, scene);				
	}

	private void handleKeyboard() {      	
        while (Keyboard.next()) {

            int key = Keyboard.getEventKey();            
            switch (key) {
            	case Keyboard.KEY_W:
                case Keyboard.KEY_UP:
                	player.getControllerEngine().onWalkUp().fire();                	                   	
                	break;
                case Keyboard.KEY_S:	
                case Keyboard.KEY_DOWN:
                	player.getControllerEngine().onWalkDown().fire();                	
                    break;
                case Keyboard.KEY_A:
                case Keyboard.KEY_LEFT:
                	player.getControllerEngine().onWalkLeft().fire();                	
                    break;
                case Keyboard.KEY_D:
                case Keyboard.KEY_RIGHT:         
                	player.getControllerEngine().onWalkRight().fire();                	
                    break;                                
                case Keyboard.KEY_G:
                	player.getControllerEngine().onSelectWeapon().fire();
                	break;
                case Keyboard.KEY_V:                   	
                	onUserCamera().fire();                	                
                	break;
                case Keyboard.KEY_TAB:                	
                	onWideCamera().fire();                	                
                	break;
            }
        }
        
    }
    
    private void handleMouse() {    	
    	if(!Mouse.isGrabbed())
    		Mouse.setGrabbed(true);
		while (Mouse.next())
		{			
			int x = Mouse.getDX();			
			if(x < 0 && x > -200) {
				player.getControllerEngine().onHeadingLeft().fire();				
			}
			else if(x > 0 && x < 200) {
				player.getControllerEngine().onHeadingRight().fire();				
			}	
			int key = Mouse.getEventButton();
			if(key == 0)
			{				
				player.getControllerEngine().onFire().fire();				
			}
		}		
	}

	@Override
	public void handleInputController() {
		handleMouse();
		handleKeyboard();			
	}      
}
