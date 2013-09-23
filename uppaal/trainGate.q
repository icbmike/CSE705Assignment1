//This file was generated from (Academic) UPPAAL 4.0.13 (rev. 4577), September 2010

/*

*/
E<> (TrafficLight.Green and Gate.Open)

/*

*/
A[] TrafficLight.Green imply Gate.Open

/*

*/
A[] not (Gate.Closing and Gate.Opening)

/*

*/
A[](Gate.Open imply TrafficLight.Green)

/*

*/
A[](!deadlock)

/*

*/
A[] Gate.Closed imply TrafficLight.Red

/*

*/
A[] forall (i : trainId) Train(i).Crossing imply Gate.Closed
