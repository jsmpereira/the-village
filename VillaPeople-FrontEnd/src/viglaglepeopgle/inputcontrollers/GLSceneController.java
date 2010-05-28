package viglaglepeopgle.inputcontrollers;

import org.lwjgl.input.Keyboard;

import viglaglepeopgle.display.Scene;
import villagepeople.entities.Player;

public class GLSceneController extends GLController {

	public GLSceneController(Player player, Scene scene) {
		super(player, scene);		
	}

	@Override
	public void handleInputController() {
		handleKeyboard();		
	}
	
	private void handleKeyboard() {
        while (Keyboard.next()) {
            int key = Keyboard.getEventKey();
            switch (key) {
            	case Keyboard.KEY_W:
                case Keyboard.KEY_UP:                                             
                	scene.getObserver().walk(0.1);
                	break;
                case Keyboard.KEY_S:	
                case Keyboard.KEY_DOWN:
                    scene.getObserver().walk(-0.1);
                    break;
                case Keyboard.KEY_A:
                case Keyboard.KEY_LEFT:
                    scene.getObserver().setRotation(0.1);                   
                    break;
                case Keyboard.KEY_D:
                case Keyboard.KEY_RIGHT:
                    scene.getObserver().setRotation(-0.1);                    
                    break;                    
            }
        }
        
    }

}
