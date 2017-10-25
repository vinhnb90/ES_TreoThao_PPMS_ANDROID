package com.es.tungnv.interfaces;

import com.es.tungnv.entity.PpmsEntityTask;

import java.util.List;

/**
 * Created by VinhNB on 11/13/2016.
 */
public interface IPpmsTasks {
    public abstract void sendITaskArray(List<PpmsEntityTask> sListTask);
}
