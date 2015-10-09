# Change Log

## 2015-10-09
### Added
- README.

## 2015-10-08
### Added
- Javadoc comments.

### Changed
- Improved the shuffle of a sequence to exclude final games correctly.

## 2015-10-05
### Added 
- Game id.

### Changed
- Earliest and latest start are stored per game instead of per team.
- Various names.

### Fixed
- Latest start penalty calculation.

## 2015-09-25
#### Added
- Range for neighbourhoods.

### Changed
- Improving stops after a given time limit instead of number of iterations.
- Command line argument #3 is now the time limit in seconds.
- Simplified evaluation and penalty calculation.

### Removed
- Unused Constraint

## 2015-09-17
### Added
- Neighbourhoods for sequences.
- Keeping track of best sequence found without using it as the sequence to be improved.
- Command line argument for number of shuffles to get a new sequence after reaching a locally optimal solution.

### Changed
- Split constraint in single and pair constraints for better evaluation.
- Sequence now save their score.
- Initial sequence is created by finding the best upcoming game, depending on violations of lags and starting time.
- Improving sequence by finding best sequence in neighbourhood.
- One iteration of improving corresponds to finding one locally optimal sequence.

### Removed
- Ordering by lag and creating a sequence based on it.
