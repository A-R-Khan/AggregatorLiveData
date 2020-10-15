# AggregatorLiveData
This is a convenience class written in Java for Android architecture components to be able to aggregate data from multiple sources into one destination with safety checks and conditionally merge different streams at anytime you want. It supports defining custom strategies to merge data with already existing data of the same type or from the same stream, hold data, define types of data and to merge data from different streams very flexibly

The default behaviour of a mediator live data on android is to notify observers everytime data changes. Sometimes this is undesirable and you can use this class to help you.

Almost fully generic and can be suited to any general task where you need to merge data from asynchronous streams.
