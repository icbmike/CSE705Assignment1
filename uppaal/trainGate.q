//This file was generated from (Academic) UPPAAL 4.1.14 (rev. 5212), March 2013

/*

*/
A<> (TrafficLight.Green and Gate.Open)

/*

*/
A[] (Gate.Opening imply A<> (TrafficLight.Green))

/*

*/
A[] (TrafficLight.Red imply A<> (Gate.Closing))

/*

*/
A[] forall (i : trainId) Train(i).Crossing imply Gate.Closed
