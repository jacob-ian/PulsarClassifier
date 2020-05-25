# PulsarClassifier
## Description
PulsarClassifier is a Machine Learning classifier that determines whether an object detected through a radio telescope is a pulsar or not. It is derivative of the LOTAASClassifier tool created by Rob Lyon (https://github.com/scienceguyrob/LOTAASClassifier).

The intention of this software is:
* to implement the changes to LOTAASClassifier Version 1.0 (LC1) as discussed in Tan et al. (2017) that were included in LOTAASClassifier Version 2.0 (LC2), which was not released; and
* prepare the classifier to be used with the Murchison Widefield Array (MWA) and the Square Kilometre Array (SKA).

## Requirements
* Java 1.6 or later
* Feature extraced candidate data produced with [PulsarFeatureLab](https://github.com/scienceguyrob/PulsarFeatureLab)

## Usage
1. Clone this repository or download the file 'target/pulsarclassifier-1.0-jar-with-dependencies.jar"
1. Run the command
'''
$ java -jar pulsarclassifier-1.0-jar-with-dependencies.jar
'''

## License
PulsarClassifier is produced under the GNU General Public License Version 3.

## References
Lyon, R. J., B. W. Stappers, S. Cooper, J. M. Brooke, and J. D. Knowles. 2016. "Fifty years of pulsar candidate selection: from simple filters to a new principled real-time classification approach." *Monthly Notices of the Royal Astronomical Society* 459, no. 1 (April): 1104-1123. https://doi.org/10.1093/mnras/stw656.

Tan, C. M., R. J. Lyon, B. W. Stappers, S. Cooper, J. W. T. Hessels, V. I. Kondratiev, D. Michilli, and S. Sanidas. 2017. "Ensemble candidate classification for the LOTAAS pulsar survey." *Monthly Notices of the Royal Astronomical Society* 474(4): 4571-4583. https://doi.org/10.1093/mnras/stx3047
