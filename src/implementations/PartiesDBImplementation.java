package implementations;

import data.Party;
import services.PartiesDB;

import java.util.HashSet;
import java.util.Set;

public class PartiesDBImplementation implements PartiesDB {
    @Override
    public Set<Party> getPartiesFromDB() {
        return new HashSet<Party>() {{
            try {
                add(new Party("Cs"));
                add(new Party("JxCAT"));
                add(new Party("ERC"));
                add(new Party("PSC"));
                add(new Party("COMÚ PODEM"));
                add(new Party("CUP"));
                add(new Party("PP"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }};
    }
}
