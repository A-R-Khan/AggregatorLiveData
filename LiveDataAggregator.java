package com.omada.junction.utils.taskhandler;

import androidx.lifecycle.MediatorLiveData;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.OverridingMethodsMustInvokeSuper;


/*
Use this class when loading live data from multiple sources. Initialize the aggregator and call the
aggregator functions in the source observers when they are invoked

The template parameters define the type of streams you have, the data you are aggregating and the data you are publshing

D : an Enum enumerating the kinds of data you have or the types of data streams you have so that the incoming data 
    can be put into the same position in the HashMap
S : The type of incoming data from sources or streams 
    (if you have multiple types then make sure they extend the same base class and you can check the instance later in the abstract methods)
T : The type of data in the destination
    This is done for generality.
    
For example, Lets say you have 5 streams that each load an Image and in the output you want a list of Images and the kinds of streams are 
STREAM_REMOTE and STREAM_LOCAL.

Then, 

D : enum StreamType{STREAM_LOCAL, STREAM_REMOTE}
S : Image
T : List<Image>
 */

public abstract class LiveDataAggregator <D extends Enum<D>, S, T> {

    /*
    When data is loaded it goes into this map where the string denotes the type of data
    this is used later to check whether or not to aggregate and set value
    */
    protected final Map<D, S> dataOnHold = new HashMap<>();
    protected final MediatorLiveData<T> destinationLiveData;

    public LiveDataAggregator(MediatorLiveData<T> destination){
        destinationLiveData = destination;
    }

    @OverridingMethodsMustInvokeSuper
    public void holdData(D typeOfData, S data) {

        if(data == null){
            return;
        }

        if(dataOnHold.containsKey(typeOfData)){
            dataOnHold.put(typeOfData, mergeWithExistingData(typeOfData, dataOnHold.get(typeOfData), data));
        }
        else{
            dataOnHold.put(typeOfData, data);
        }

        if(checkDataForAggregability()){
            aggregateData();
        }
    }

    /*
    This functions combines data coming from the same source (or type) with already existing data by defining the merge strategy
    Remember to handle all types of streams here. If no data already exists, oldData is null
    */
    protected abstract S mergeWithExistingData(D typeofData, S oldData, S newData);

    /*
    This function checks if it is safe to combine values from the sources and set value so that all observers are notified of a change
    Use the data put in dataOnHold and check for integrity, completeness, etc according to your needs.
    return true if aggregate and notify should be done, false otherwise
    */
    protected abstract boolean checkDataForAggregability();

    /*
    This function combines the data stored on hold. call setValue from here into destinationLiveData after combining all your streams data
    and after doing all your necessary computations
    */
    protected abstract void aggregateData();



}
