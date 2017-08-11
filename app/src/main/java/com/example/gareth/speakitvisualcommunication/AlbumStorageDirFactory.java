package com.example.gareth.speakitvisualcommunication;

/**
 * Created by Gareth on 11/08/2017.
 */

import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}

