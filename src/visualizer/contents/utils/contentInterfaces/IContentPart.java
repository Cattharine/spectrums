package visualizer.contents.utils.contentInterfaces;

public interface IContentPart extends IContent {
    int getSelected();

    void selectFromOtherContent(int value);
}
