package visualizer;

import algorithms.ISolver;
import algorithms.SolutionsManager;
import visualizer.extendedAlgorithms.ISolverExt;
import visualizer.extendedAlgorithms.MetaInfo;
import visualizer.painter.Dispatcher;

import java.util.ArrayList;

public class DispatchedSolutionsManager extends SolutionsManager {
    private final Dispatcher dispatcher;

    public DispatchedSolutionsManager(String inputFileName, Dispatcher dispatcher) {
        super(inputFileName);
        this.dispatcher = dispatcher;
    }

    @Override
    protected void solve(ISolver solver, String name, String input) {
        solver.getNewEntry(input);
        solver.setPrintingState(false);
        addInfoToDispatcher(((ISolverExt) solver).getInfo());
        solver.calculateSpectrum();
        var res = solver.getResult();
        if (map.containsKey(res))
            map.get(res).add(name);
        else map.put(res, new ArrayList<>() {
            {
                add(name);
            }
        });
    }

    private void addInfoToDispatcher(MetaInfo info) {
        dispatcher.addContentInfo(info);
    }
}
