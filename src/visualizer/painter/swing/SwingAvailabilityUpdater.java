package visualizer.painter.swing;

import visualizer.contents.utils.availabilityUpdater.AvailabilityUpdaterType;
import visualizer.contents.utils.availabilityUpdater.IAvailabilityUpdater;

import javax.swing.*;

public class SwingAvailabilityUpdater implements IAvailabilityUpdater {
    private final AbstractButton toUpdate;
    private final AvailabilityUpdaterType type;

    public SwingAvailabilityUpdater(AbstractButton item, AvailabilityUpdaterType type) {
        toUpdate = item;
        this.type = type;
    }

    @Override
    public void setEnabled(boolean value) {
        SwingUtilities.invokeLater(() -> toUpdate.setEnabled(value));
    }

    @Override
    public AvailabilityUpdaterType getType() {
        return type;
    }
}
