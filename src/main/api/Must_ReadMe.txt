July 2017, ModusBox

This readme file is here to communicate why the .api.xml and .raml files are still in this project.  

They are no longer used at this at this time because of the following reasons: 

During performance testing on all of the interop Mule projects, it was discovered that API Kit router contains a bug that prevents the Router from scaling.  Essentially, the scalability appears like Mule introduced a synchronized method someone in 
the Router code and therefore calling the applications with more "worker threads" or requests does not increase the throughput, scaling flat. 
So bad in fact at times that just using plain http connector without the API Kit router, can performs over 20X faster and scales as expected.

It was decided in late June 2017, to remove API kit/RAML from our application as it as a performance bottleneck.

At some future date, these api.xml and .raml files maybe removed.