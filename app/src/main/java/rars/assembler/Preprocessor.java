package rars.assembler;

import java.util.ArrayList;
import rars.simulator.SourceLine;;

public class Preprocessor {
    // TODO: change params for cycle in includes detection
    public static ArrayList<SourceLine> process(ArrayList<String> input, String filename) {
        ArrayList<SourceLine> ans = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ans.add(new SourceLine(input.get(i), filename, i + 1));
        }
        return ans;
    }
}
