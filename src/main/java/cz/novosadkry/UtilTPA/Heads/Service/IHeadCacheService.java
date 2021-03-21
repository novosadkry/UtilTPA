package cz.novosadkry.UtilTPA.Heads.Service;

import cz.novosadkry.UtilTPA.Heads.IHeadProvider;

public interface IHeadCacheService extends IHeadProvider {
    void enqueueHead(String player);
}
