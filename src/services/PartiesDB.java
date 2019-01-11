package services;

import data.Party;
import exceptions.NoConnectionToDBException;

import java.util.Set;

public interface PartiesDB {
    Set<Party> getPartiesFromDB() throws NoConnectionToDBException;
}
