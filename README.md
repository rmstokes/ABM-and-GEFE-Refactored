# ABM-and-GEFE-Refactored
Cleaned up version of ABM [Angle Based Metrics]
This is a project involving software biometrics and active authentication (being able to verify a user's identity
based on the way they interact with their mouse.
As of right now the program will read in event files that contain mouse movement, keystroke event, and mouse click data.
Log Reader extracts point and click events from files. Point and click data is continous mouse movement followed 
by a click (pac event). There may be many pac events within a single event file so we must separate them out.
The pac events are saved in secondary memory which will later be some other data store. A component called the Feed 
reads these files from memory in order to transform pac objects into mouse movement objects. The mouse movement objects
are sent to the Preprocessor where 3 different types of metrics are calculated and a cdf file will be generated for 
each pac event. CDF files are stored in secondary memory where they can be read by TemplateListGenerator in order to create
a list of templates or feature sets that represent the percentage values in the cdf files for each pac event.
Templates can be used in order to discern how similar events are to one another. The GalleryAndProbe class will use the 
templates in order to make comparisons and complete analysis which includes cmc rank and roc chart.
