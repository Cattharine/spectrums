package visualizer.contents.utils.elementInfo;

public class ElementInfo {
    public ElementType type;
    public ElementState state;

    public ElementInfo(ElementType elementType, ElementState elementState) {
        this.type = elementType;
        state = elementState;
    }
}
