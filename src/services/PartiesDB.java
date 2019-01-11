package services;

import data.Party;

import java.util.Set;

public interface PartiesDB {
    Set<Party> getPartiesFromDB();
}
