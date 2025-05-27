package visualizer.contents.utils.availabilityUpdater;

public interface IAvailabilityUpdater {
    void setEnabled(boolean value);

    AvailabilityUpdaterType getType();
}
