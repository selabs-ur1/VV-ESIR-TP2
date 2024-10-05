import matplotlib.pyplot as plt
import numpy as np
import math

# Path to your TCC report
filename = '../reports/report_CommonsCollections.txt'
simple_filename = filename.split('_')[-1].split('.')[0]

TCC_values = []
with open(filename, 'r') as file:
    for line in file:
        value = line.split()[-1]
        try:
            TCC_values.append(float(value))
        except ValueError:
            TCC_values.append(float('nan'))  # Handle non-numeric values as NaN

valid_TCC_values = [v for v in TCC_values if not math.isnan(v)]
nan_count = len(TCC_values) - len(valid_TCC_values)

bins = np.arange(0, 1.01, 0.01)

plt.figure(figsize=(10, 6))
plt.hist(valid_TCC_values, bins=bins, color='skyblue', edgecolor='black', rwidth=0.8)

plt.title('TCC Distribution for ' + simple_filename, fontsize=16)
plt.xlabel('TCC', fontsize=14)
plt.ylabel('Frequency', fontsize=14)

plt.grid(True, linestyle='--', alpha=0.6)
plt.xticks(bins, rotation=90)

plt.tight_layout()
plt.show()

def count_occurences_total(lst):
    occurrences = {}
    for element in lst:
        if element in occurrences:
            occurrences[element] += 1
        else:
            occurrences[element] = 1
    return occurrences, sum(occurrences.values())

dic, total_nb_of_classes = count_occurences_total(valid_TCC_values)

print("===", simple_filename, "===")

print("Number of classes with a TCC value of NaN:", nan_count, "(=", str(round(nan_count / len(TCC_values) * 100, 1)) + "%)\n")

ranges = [(0.0, 0.05), (0.05, 0.1), (0.1, 0.2), (0.2, 0.5), (0.5, 1.0)]
for r in ranges:
    count_in_range = sum(1 for v in valid_TCC_values if r[0] <= v < r[1])
    print(f"Number of classes with TCC between {r[0]} and {r[1]}: {count_in_range} "
          f"(= {round(count_in_range / total_nb_of_classes * 100, 1)}%)")

try :
    print("\nNumber of classes with a TCC value equal to 0:", dic[0])
except :
    print("\nNumber of classes with a TCC value equal to 0: 0")

try :
    print("Number of classes with a TCC value equal to 1:", dic[1])
except :
    print("Number of classes with a TCC value equal to 1: 0")