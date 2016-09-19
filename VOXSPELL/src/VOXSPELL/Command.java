package VOXSPELL;
/**
 * This interface provides a common typing for all models to associate with, they all must all implement
 * the execute method.
 * @author Jacky Lo
 *
 */
public interface Command {
	public void execute();
	public void addGUI(GUI GUI);
}
