# PulsarClassifier
## Description
PulsarClassifier is a Machine Learning classifier that determines whether an object detected through a radio telescope is a pulsar or not. It is derivative of the LOTAASClassifier tool created by Rob Lyon (https://github.com/scienceguyrob/LOTAASClassifier).

The intention of this software is:
* to implement the changes to LOTAASClassifier Version 1.0 (LC1) as discussed in Tan et al. (2017) that were included in LOTAASClassifier Version 2.0 (LC2), which was not released; and
* prepare the classifier to be used with the Murchison Widefield Array (MWA) and the Square Kilometre Array (SKA).

## Requirements
* Java 1.6 or later
* Feature extracted candidate data produced with [PulsarFeatureLab](https://github.com/scienceguyrob/PulsarFeatureLab).

## Usage

### Step 1:
*Option A:*
* Download this software by runnning: `git clone https://github.com/jacob-ian/PulsarClassifier.git`
* Navigate to the `target` directory: `cd PulsarClassifier/target`.

*Option B:*
* Download the `pulsarclassifier-1.0-full.jar` file from the `target` directory in this repository.

### Step 2 - *Train the Ensemble Classifier*:
* Use `PulsarFeatureLab` with the appended flags `--arff --meta` to extract the machine learning features from known pulsar and non-pulsar candidates. **NOTE:** Ensure that the feature type extracted is consistent across the training dataset and the data to be classified otherwise the machine learning classifier will not work.
* Edit the outputted file to remove the appended `?` character from each line and replace them with a `1` if the candidate is a pulsar, or a `0` if it a non-pulsar.
* Run the command:
```
$ java -jar pulsarclassifier-1.0-full.jar -t [PATH TO TRAINING DATASET ARFF FILE] -m [DIRECTORY TO STORE CLASSIFIER MODELS]
```

### Step 3 - *Make Predictions with the Ensemble Classifier*:
* Use `PulsarFeatureLab` with the appended flags `--arff --meta` to extract the machine learning features from your set of pulsar candidates. Do not edit the outputted file.
* Run the command:
```
$ java -jar pulsarclassifier-v1.0-full.jar -p [PATH TO DATASET ARFF FILE TO CLASSIFY] -m [DIRECTORY OF STORED CLASSIFIER MODELS]
```

### (Optional) Step 4 - *Testing and Validation*:
* Use [PulsarValidator](https://github.com/jacob-ian/PulsarValidator.git) to compare the list of pulsars in the testing/validation dataset with the output of the classifier.

## Output
PulsarClassifier will output 10 files in the same directory as the inputted `[output].arff` file. There are two files: a `[ouput]_[classifier].positive` file and an `[output]_[classifier].negative` file for each machine learning classifier, with the `.positive` file containing classified pulsars and the `.negative` file containing classified non-pulsars.

The ensemble classifier checks the result of the 4 machine learning classifiers and if a candidate is classified as a pulsar by 3 separate classifiers, it is classified as a pulsar by the ensemble classifier.


## License
PulsarClassifier is produced under the GNU General Public License Version 3.

## References
Lyon, R. J., B. W. Stappers, S. Cooper, J. M. Brooke, and J. D. Knowles. 2016. "Fifty years of pulsar candidate selection: from simple filters to a new principled real-time classification approach." *Monthly Notices of the Royal Astronomical Society* 459, no. 1 (April): 1104-1123. https://doi.org/10.1093/mnras/stw656.

Tan, C. M., R. J. Lyon, B. W. Stappers, S. Cooper, J. W. T. Hessels, V. I. Kondratiev, D. Michilli, and S. Sanidas. 2017. "Ensemble candidate classification for the LOTAAS pulsar survey." *Monthly Notices of the Royal Astronomical Society* 474(4): 4571-4583. https://doi.org/10.1093/mnras/stx3047
