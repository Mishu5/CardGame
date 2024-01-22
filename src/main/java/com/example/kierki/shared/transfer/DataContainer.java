package com.example.kierki.shared.transfer;

import java.io.Serializable;

/**
 * Main container to send data between client and server
 */

public class DataContainer implements Serializable {
    private final DataTypes dataType;
    private DataInterface data;

    public DataContainer(DataTypes type, DataInterface data) {
        this.dataType = type;
        this.data = data;
    }

    public DataTypes getDataType() {
        return dataType;
    }

    public DataInterface getData() {
        return data;
    }

}
