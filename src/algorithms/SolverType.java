package algorithms;

import algorithms.fast.FastSolver;
import algorithms.fast_variants.FastSolverWithLim;
import algorithms.fast_variants.FastSolverWithXorSim;
import algorithms.fast_variants.FastSolverWithXorSimAndLim;
import algorithms.fast_variants.parallel.FastSolverParallel;
import algorithms.fast_variants.parallel.FastSolverParallelWithLim;
import algorithms.straightforward.lim.SFSolverLim;
import algorithms.straightforward.lim.SFSolverParallelLim;
import algorithms.straightforward.parallel.SFSolverParallel;
import algorithms.straightforward.SFSolver;
import algorithms.tree.TreeSolver;
import algorithms.tree.lim.TreeSolverLim;
import algorithms.tree.lim.TreeSolverParallelLim;
import algorithms.tree.parallel.TreeSolverParallel;
import visualizer.contents.ContentInfo;

public enum SolverType {
    FAST,
    FAST_LIM,
    FAST_XOR_SIM,
    FAST_XOR_SIM_AND_LIM,
    FAST_PAR,
    FAST_PAR_LIM,
    STRAIGHTFORWARD,
    STRAIGHTFORWARD_PAR,
    STRAIGHTFORWARD_LIM,
    STRAIGHTFORWARD_PAR_LIM,
    VISUALIZER,
    TREE,
    TREE_PAR,
    TREE_LIM,
    TREE_PAR_LIM;

    public static ISolver getSolver(SolverType type, String entry) {
        return switch (type) {
            case TREE -> new TreeSolver(entry);
            case TREE_PAR -> new TreeSolverParallel(entry);
            case TREE_LIM -> new TreeSolverLim(entry);
            case TREE_PAR_LIM -> new TreeSolverParallelLim(entry);
            case VISUALIZER -> new ContentInfo(entry);
            case FAST -> new FastSolver(entry);
            case FAST_LIM -> new FastSolverWithLim(entry);
            case FAST_XOR_SIM -> new FastSolverWithXorSim(entry);
            case FAST_XOR_SIM_AND_LIM -> new FastSolverWithXorSimAndLim(entry);
            case FAST_PAR -> new FastSolverParallel(entry);
            case FAST_PAR_LIM -> new FastSolverParallelWithLim(entry);
            case STRAIGHTFORWARD -> new SFSolver(entry);
            case STRAIGHTFORWARD_PAR -> new SFSolverParallel(entry);
            case STRAIGHTFORWARD_LIM -> new SFSolverLim(entry);
            case STRAIGHTFORWARD_PAR_LIM -> new SFSolverParallelLim(entry);
        };
    }

    public static SolverType getType(String name) {
        return switch (name.toLowerCase()) {
            case "tree" -> TREE;
            case "tree_par" -> TREE_PAR;
            case "tree_lim" -> TREE_LIM;
            case "tree_par_lim", "tree_lim_par" -> TREE_PAR_LIM;
            case "visualizer", "vis" -> VISUALIZER;
            case "fast_lim" -> FAST_LIM;
            case "fast_xor" -> FAST_XOR_SIM;
            case "fast_xor_lim", "fast_lim_xor" -> FAST_XOR_SIM_AND_LIM;
            case "fast_par" -> FAST_PAR;
            case "fast_par_lim", "fast_lim_par" -> FAST_PAR_LIM;
            case "sf" -> STRAIGHTFORWARD;
            case "sf_par" -> STRAIGHTFORWARD_PAR;
            case "sf_lim" -> STRAIGHTFORWARD_LIM;
            case "sf_par_lim", "sf_lim_par" -> STRAIGHTFORWARD_PAR_LIM;
            default -> FAST;
        };
    }
}
