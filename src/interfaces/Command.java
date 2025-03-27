package interfaces;

public interface Command {
    public void execute();
    public void undo();
    public void redo();
    public void CreateShapeCommand();
    public void MoveShapeCommand();
    public void ResizeShapeCommand();
    public void DeleteShapeCommand();
}
